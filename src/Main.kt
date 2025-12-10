fun main() {
    // начальное состояние e)
    val initialState = PuzzleState(
        listOf(
            7, 4, 2,
            3, 5, 8,
            6, 1, 0 // 0 = пустая клетка
        )
    )
//    есть решение
//    1, 2, 3,
//    4, 0, 7,
//    6, 8, 5,

    // Целевое состояние V)
    val goalState = PuzzleState(
        listOf(
            1, 2, 3,
            4, 5, 0,
            6, 7, 8
        )
    )

    println("Начальное состояние:")
    initialState.prettyPrint()
    println("Целевое состояние:")
    goalState.prettyPrint()

    println()
    println("Выбери стратегию поиска:")
    println("1 - поиск в ширину (BFS)")
    println("2 - поиск с ограничением глубины (DLS)")

    val strategy = readLine()?.trim()

    when (strategy) {
        "1" -> {
            val result = breadthFirstSearch(initialState, goalState, stepMode = false)
            if (result != null) {
                val path = result.buildPath()
                println("Найден путь длины ${path.size - 1} (BFS):")
                path.forEachIndexed { i, node ->
                    println("Шаг $i, ход=${node.action}")
                    node.state.prettyPrint()
                }
            }
        }

        "2" -> {
            print("Введи ограничение глубины (целое число): ")
            val limit = readLine()?.trim()?.toIntOrNull() ?: 10

            val result = depthLimitedSearch(
                initial = initialState,
                goal = goalState,
                limit = limit,
                stepMode = true
            )
            if (result != null) {
                val path = result.buildPath()
                println("Найден путь длины ${path.size - 1} (DLS, limit=$limit):")
                path.forEachIndexed { i, node ->
                    println("Шаг $i, ход=${node.action}")
                    node.state.prettyPrint()
                }
            }
        }

        else -> {
            println("Неизвестная стратегия.")
        }
    }
}
