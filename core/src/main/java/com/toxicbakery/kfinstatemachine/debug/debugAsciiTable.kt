package com.toxicbakery.kfinstatemachine.debug

/**
 * Print a single column ASCII table.
 */
internal fun createAsciiTable(items: List<String>): String {
    if (items.isEmpty()) throw IllegalArgumentException("Empty item list.")
    val tableWidth = items.fold(5, { fold: Int, item: String -> Math.max(fold, item.length) })
    return StringBuilder()
            .append(printCap(tableWidth))
            .append(printRow(tableWidth, items[0]))
            .append(printCap(tableWidth))
            .apply {
                items.subList(1, items.size)
                        .forEach { append(printRow(tableWidth, it)) }
            }
            .append(printCap(tableWidth))
            .trimEnd()
            .toString()
}

/**
 * Print an ASCII table cap row.
 *
 * eg. `+----+`
 */
private fun printCap(width: Int): String =
        StringBuilder("+-")
                .apply { for (i in 0 until width) append("-") }
                .append("-+\n")
                .toString()

/**
 * Print an ASCII table row.
 *
 * eg. `| item    |`
 */
private fun printRow(width: Int, value: String): String =
        StringBuilder("| ")
                .append(value)
                .apply {
                    val pad = width - value.length
                    for (i in 0 until pad) append(" ")
                }
                .append(" |\n")
                .toString()
