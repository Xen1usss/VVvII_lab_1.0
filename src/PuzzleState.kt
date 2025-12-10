// Представление состояния поля 3x3
data class PuzzleState(
    val tiles: List<Int> // размер всегда 9, 0 = пустая клетка
) {
    init {
        require(tiles.size == 9) { "State must have 9 tiles" }
    }

    // Индекс пустой клетки (0)
    fun indexOfZero(): Int = tiles.indexOf(0)

    // Вывод 3x3
    fun prettyPrint() {
        for (row in 0 until 3) {
            val line = tiles.slice(row * 3 until row * 3 + 3)
            println(
                line.joinToString(" ") {
                    if (it == 0) "_" else it.toString()
                }
            )
        }
    }
}

// Возможные движения пустой клетки
enum class Move {
    UP, DOWN, LEFT, RIGHT
}

// Какие ходы допустимы из данного состояния
fun PuzzleState.possibleMoves(): List<Move> {
    val idx = indexOfZero()
    val row = idx / 3
    val col = idx % 3

    val moves = mutableListOf<Move>()
    if (row > 0) moves.add(Move.UP)
    if (row < 2) moves.add(Move.DOWN)
    if (col > 0) moves.add(Move.LEFT)
    if (col < 2) moves.add(Move.RIGHT)

    return moves
}

// Применение хода: возвращает новое состояние
fun PuzzleState.applyMove(move: Move): PuzzleState {
    val idx = indexOfZero()
    val row = idx / 3
    val col = idx % 3

    val newRow = when (move) {
        Move.UP -> row - 1
        Move.DOWN -> row + 1
        Move.LEFT -> row
        Move.RIGHT -> row
    }

    val newCol = when (move) {
        Move.LEFT -> col - 1
        Move.RIGHT -> col + 1
        Move.UP -> col
        Move.DOWN -> col
    }

    val newIdx = newRow * 3 + newCol

    val newTiles = tiles.toMutableList()
    // Меняем местами пустую клетку и фишку, в которую двигаемся
    newTiles[idx] = newTiles[newIdx]
    newTiles[newIdx] = 0

    return PuzzleState(newTiles)
}
