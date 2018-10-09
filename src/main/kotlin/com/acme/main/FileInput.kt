package com.acme.main

import com.acme.process.CreditCardProcessor
import com.acme.process.FileProcessor

fun main(args: Array<String>) {
    print("Please enter a filename, or hit enter to provide the filename as a command line argument: ")
    val stdIn = readLine()
    if (args.isEmpty() && stdIn.isNullOrEmpty()) {
        println("Please provide a filename as a command line argument or as standard input")

        return
    }

    val fileName = if (args.isEmpty()) stdIn!! else args.first()
    FileProcessor.readFile(fileName)
    CreditCardProcessor.generateSummaryReport()
}