package com.toxicbakery.kfinstatemachine.xml

import java.io.InputStream

interface StreamParser<out T> {

    fun parseStream(inputStream: InputStream): T

}