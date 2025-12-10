// Узел дерева поиска
data class Node(
    val state: PuzzleState, // само поле
    val parent: Node?,
    val action: Move?,      // каким ходом дошли до этого узла
    val pathCost: Int,      // сколько ходов от начала (g(n))
    val depth: Int          // глубина в дереве
)

// Восстановление пути от корня до этого узла
fun Node.buildPath(): List<Node> {
    val result = mutableListOf<Node>()
    var current: Node? = this
    while (current != null) {
        result.add(current)
        current = current.parent
    }
    return result.reversed()
}

// Поиск в ширину (BFS)
fun breadthFirstSearch(
    initial: PuzzleState,
    goal: PuzzleState,
    stepMode: Boolean = true
): Node? {
    val root = Node(
        state = initial,
        parent = null,
        action = null,
        pathCost = 0,
        depth = 0
    )

    val frontier: ArrayDeque<Node> = ArrayDeque() // очередь
    frontier.add(root)

    val visited = mutableSetOf<PuzzleState>() // множество уже увиденных состояний
    visited.add(initial)

    var expandedCount = 0
    var maxFrontierSize = frontier.size

    while (frontier.isNotEmpty()) {
        val current = frontier.removeFirst()
        expandedCount++

        println("=== BFS: шаг $expandedCount ===")
        println("Текущая вершина (depth=${current.depth}, g=${current.pathCost}):")
        current.state.prettyPrint()

        if (current.state == goal) {
            println(">>> Цель достигнута (BFS)")
            println("Раскрыто вершин: $expandedCount")
            println("Максимальный размер каймы: $maxFrontierSize")
            return current
        }

        val newNodes = mutableListOf<Node>()
        val repeatedNodes = mutableListOf<Node>()

        for (move in current.state.possibleMoves()) {
            val newState = current.state.applyMove(move)
            val newNode = Node(
                state = newState,
                parent = current,
                action = move,
                pathCost = current.pathCost + 1,
                depth = current.depth + 1
            )

            if (visited.contains(newState)) {
                repeatedNodes.add(newNode)
            } else {
                visited.add(newState)
                newNodes.add(newNode)
                frontier.addLast(newNode)
            }
        }

//        println("Новые вершины (${newNodes.size}):")
//        newNodes.forEach { node ->
//            println("- move=${node.action}, depth=${node.depth}")
//            node.state.prettyPrint()
//        }
//
//        println("Повторные вершины (${repeatedNodes.size}):")
//        repeatedNodes.forEach { node ->
//            println("- move=${node.action}")
//            node.state.prettyPrint()
//        }
//
//        println("Кайма (frontier), size=${frontier.size}:")
//        frontier.forEach { node ->
//            print("d=${node.depth}, g=${node.pathCost}; ")
//        }
//        println()

        if (frontier.size > maxFrontierSize) {
            maxFrontierSize = frontier.size
        }

        if (stepMode) {
            println("Нажми Enter для следующего шага (BFS)...")
            readLine()
        }
    }

    println("Решение BFS не найдено.")
    return null
}

// Статистика для DLS
data class DlsStats(
    var expandedCount: Int = 0
)

// Вспомогательная рекурсивная функция для DLS
fun depthLimitedSearchRecursive(
    node: Node,
    goal: PuzzleState,
    limit: Int,
    visited: MutableSet<PuzzleState>,
    stats: DlsStats,
    stepMode: Boolean
): Node? {
    println("=== DLS: шаг ${++stats.expandedCount} ===")
    println("Текущая вершина (depth=${node.depth}, g=${node.pathCost}):")
    node.state.prettyPrint()

    if (node.state == goal) {
        println(">>> Цель достигнута (DLS)")
        return node
    }

    if (node.depth >= limit) {
        println("Достигнут предел глубины $limit, дальше не углубляемся.")
        return null
    }

    val newNodes = mutableListOf<Node>()
    val repeatedNodes = mutableListOf<Node>()

    for (move in node.state.possibleMoves()) {
        val newState = node.state.applyMove(move)
        val newNode = Node(
            state = newState,
            parent = node,
            action = move,
            pathCost = node.pathCost + 1,
            depth = node.depth + 1
        )

        if (visited.contains(newState)) {
            repeatedNodes.add(newNode)
        } else {
            visited.add(newState)
            newNodes.add(newNode)
        }
    }

    println("Новые вершины (${newNodes.size}):")
    newNodes.forEach { child ->
        println("- move=${child.action}, depth=${child.depth}")
        child.state.prettyPrint()
    }

    println("Повторные вершины (${repeatedNodes.size}):")
    repeatedNodes.forEach { child ->
        println("- move=${child.action}")
        child.state.prettyPrint()
    }

    if (stepMode) {
        println("Нажми Enter для следующего шага (DLS)...")
        readLine()
    }

    for (child in newNodes) {
        val result = depthLimitedSearchRecursive(
            child, goal, limit, visited, stats, stepMode
        )
        if (result != null) return result
    }

    return null
}

// Обёртка для DLS
fun depthLimitedSearch(
    initial: PuzzleState,
    goal: PuzzleState,
    limit: Int,
    stepMode: Boolean = true
): Node? {
    val root = Node(
        state = initial,
        parent = null,
        action = null,
        pathCost = 0,
        depth = 0
    )

    val visited = mutableSetOf<PuzzleState>()
    visited.add(initial)

    val stats = DlsStats()

    val result = depthLimitedSearchRecursive(
        node = root,
        goal = goal,
        limit = limit,
        visited = visited,
        stats = stats,
        stepMode = stepMode
    )

    println("DLS: раскрыто вершин: ${stats.expandedCount}")
    println("Число посещённых состояний: ${visited.size}")

    if (result == null) {
        println("Решение DLS не найдено при ограничении глубины $limit.")
    }

    return result
}
