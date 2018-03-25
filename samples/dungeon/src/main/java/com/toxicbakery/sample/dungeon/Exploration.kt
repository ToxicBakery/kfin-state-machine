package com.toxicbakery.sample.dungeon

class Exploration(
        private val size: Int,
        private val viewableSize: Int
) {

    init {
        if (viewableSize % 2 == 0) throw Exception("Viewable size must be odd.")
    }

    private val halfViewableSize = viewableSize / 2

    private val exploredArea: Array<Array<Boolean>> =
            Array(size, { _ ->
                Array(size, { _ -> false })
            })

    private val Boolean.mapRepresentation: String
        get() = if (this) " * " else "   "

    private val Point.translate: Point
        get() = Point(translate(x), translate(y))

    private fun translate(value: Int): Int =
            if (value < 0) value + size
            else value % size

    fun addExploredPoint(point: Point) {
        exploredArea[point.x][point.y] = true
    }

    fun printMap(currentLocation: Point) {
        printCap()
        for (y in -halfViewableSize..halfViewableSize) {
            print("|")
            for (x in -halfViewableSize..halfViewableSize) {
                printMapPoint(currentLocation, Point(currentLocation.x + x, currentLocation.y + y).translate)
            }
            println("|")
        }
        printCap()
    }

    private fun printCap() {
        for (i in 0 until viewableSize + 2) {
            if (i == 0 || viewableSize + 1 == i) print("+")
            else print("---")
        }
        println()
    }

    private fun printMapPoint(
            currentLocation: Point,
            plotLocation: Point
    ) {
        if (currentLocation == plotLocation) print(" x ")
        else print(exploredArea[plotLocation.x][plotLocation.y].mapRepresentation)
    }

}