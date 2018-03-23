package com.toxicbakery.kfinstatemachine.xml

import javax.xml.stream.XMLStreamReader

/**
 * Use implementation for XMLStreamReader wrapping close functionality.
 */
internal fun <T : XMLStreamReader?, R> T.use(block: (T) -> R): R {
    try {
        return block(this)
    } finally {
        when {
            this == null -> Unit
            else -> close()
        }
    }
}