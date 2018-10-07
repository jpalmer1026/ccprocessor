package com.acme.main

import com.acme.display.CreditCardFormatter
import com.acme.process.CreditCardProcessor
import com.acme.process.FileProcessor

fun main(args: Array<String>) {
    val stdIn = readLine()
    if (args.isEmpty() && stdIn.isNullOrEmpty()) {
        println("Please provide a filename as a command line argument or standard input")

        return
    }

    val fileName = if (args.isEmpty()) stdIn!! else args.first()
    FileProcessor.readFile(fileName)
    CreditCardFormatter.displayBalances(CreditCardProcessor.getCreditCardMap())
}