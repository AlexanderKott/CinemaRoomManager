package jba

import java.util.*
import kotlin.math.round


fun main() {

    val arrayOfSeats = intro()
    var totalIncome = 0
    var working = true

    val calcTicket1: (sum: Int, rowX: Int, rows: Int) -> Int = { sum, rowX, rows ->
        if (sum <= 60) {
            10
        } else {
            if (rowX < rows / 2) 10 else 8
        }
    }
    val calcTicket2: (sum: Int, seatsInR: Int, rows: Int) -> Int = { sum, seatsInR, rows ->
        if (sum <= 60) {
            10 * sum
        } else {
            (rows / 2 * seatsInR * 10) + ((rows - (rows / 2)) * 8 * seatsInR)
        }
    }

    while (working) {
        println(
            """1. Show the seats 
2. Buy a ticket
3. Statistics
0. Exit"""
        )
        when (readLine()?.toInt() ?: 0) {
            1 -> displaySeats(arrayOfSeats)
            2 -> {
                totalIncome += buyTicket(calcTicket1, arrayOfSeats)
            }
            3 -> calcStatistics(calcTicket2, arrayOfSeats, totalIncome)
            0 -> working = false
            else -> println("Unknown operation")
        }
    }


}

fun intro(): Array<Array<String>> {
    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()
    println("Enter the number of seats in each row:")
    val seatsInR = readLine()!!.toInt()

    val arrayOfSeats = Array(rows + 1) { Array<String>(seatsInR + 1) { " " } }
    fillInSeats(arrayOfSeats)
    return arrayOfSeats
}

fun calcStatistics(
    calc: (sum: Int, seatsInR: Int, rows: Int) -> Int,
    arrayOfSeats: Array<Array<String>>,
    currentIncome: Int
) {
    val sold = arrayOfSeats.contentDeepToString().count { it == 'B' }
    val income = calc(
        arrayOfSeats.size * arrayOfSeats[0].size,
        arrayOfSeats[0].size - 1,
        arrayOfSeats.size - 1
    )

    val percent = (sold.toDouble() / ((arrayOfSeats.size - 1) * (arrayOfSeats[0].size - 1))) * 100.0
    val prcRound = String.format(Locale.US, "%.2f", round(percent * 1000) / 1000)

    println()
    println("Number of purchased tickets: $sold")
    println("Percentage: $prcRound%")
    println("Current income: \$$currentIncome")
    println("Total income: \$$income")
    println()
}

fun buyTicket(
    calc: (sum: Int, rowX: Int, rows: Int) -> Int,
    arrayOfSeats: Array<Array<String>>
): Int {
    val sold = "B "
    var enterAgain = false
    var rowX: Int
    var rowY: Int

    do {
        println("Enter a row number:")
        rowX = readLine()!!.toInt()
        println("Enter a seat number in that row:")
        rowY = readLine()!!.toInt()


        enterAgain = (rowX !in 1 until arrayOfSeats.size || rowY !in 1 until arrayOfSeats[0].size)
        if (enterAgain) {
            println("Wrong input!\n")
            continue
        }

        enterAgain = (arrayOfSeats[rowX][rowY] == sold)
        if (enterAgain) {
            println("That ticket has already been purchased!\n")
            continue
        }
    } while (enterAgain)

    arrayOfSeats[rowX][rowY] = sold
    return calcPrice(calc, arrayOfSeats.size, arrayOfSeats[0].size, rowX)

}

fun calcPrice(
    calc: (sum: Int, rowX: Int, rows: Int) -> Int,
    rows: Int, seatsInR: Int, rowX: Int
): Int {
    var price = 0
    val sum = rows * seatsInR
    price = calc(sum, rowX, rows)
    println("\nTicket price: $$price")
    return price
}

fun fillInSeats(array: Array<Array<String>>) {
    array[0][0] = "  "
    for (i in 1 until array[0].size) {
        array[0][i] = "$i "
    }

    for (i in 1 until array.size) {
        array[i][0] = "$i "
        for (j in 1 until array[0].size) {
            array[i][j] = "S "
        }
    }
}


fun displaySeats(array: Array<Array<String>>) {
    println("\nCinema:")
    print(" ")
    println()
    for (i in 0 until array.size) {
        for (j in 0 until array[0].size) {
            print(array[i][j])
        }
        println()
    }
    println()
}