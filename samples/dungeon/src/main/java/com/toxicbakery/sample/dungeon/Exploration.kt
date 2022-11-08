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

    fun printMap(currentLocation: Point): String {
        val sb = StringBuilder()
        printCap(sb)
        for (y in -halfViewableSize..halfViewableSize) {
            sb.append("|")
            for (x in -halfViewableSize..halfViewableSize) {
                Point(currentLocation.x + x, currentLocation.y + y).translate
                    .also { translatedPoint ->
                        printMapPoint(
                            currentLocation = currentLocation,
                            plotLocation = translatedPoint
                        )
                            .also { sb.append(it) }
                    }
            }
            sb.appendln("|")
        }
        printCap(sb)
        return sb.toString()
    }

    private fun printCap(stringBuilder: StringBuilder) {
        for (i in 0 until viewableSize + 2) {
            if (i == 0 || viewableSize + 1 == i) stringBuilder.append("+")
            else stringBuilder.append("---")
        }
        stringBuilder.appendln()
    }

    private fun printMapPoint(
        currentLocation: Point,
        plotLocation: Point
    ): String =
        if (currentLocation == plotLocation) " x "
        else exploredArea[plotLocation.x][plotLocation.y].mapRepresentation

}