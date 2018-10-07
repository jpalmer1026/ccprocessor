package com.acme.process

import java.io.File

object FileProcessor {

    fun readFile(fileName: String) {
        val file = File(fileName)
        file.forEachLine { CreditCardProcessor.processLine(it) }
    }
}
