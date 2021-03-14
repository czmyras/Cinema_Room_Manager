package cinema

import java.lang.Exception
import java.lang.IllegalArgumentException
import javax.naming.CannotProceedException

fun main() {
    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()

    println("Enter the number of seats in each row:")
    val seatsInRow = readLine()!!.toInt()

    val cinema = Cinema(rows, seatsInRow)

    do {
        val option = menuOption()
        when (option) {
            1 -> println(cinema)
            2 -> {
                var price = 0
                do {
                    println("Enter a row number:")
                    val row = readLine()!!.toInt()

                    println("Enter a seat number in that row:")
                    val seat = readLine()!!.toInt()

                    try {
                        price = cinema.book(row, seat)
                    } catch (e: Exception) {
                        println(e.message)
                    }
                } while (price == 0)
                println("Ticket price: \$$price")
            }
            3 -> {
                println("Number of purchased tickets: ${cinema.purchasedTicketsNumber()}")
                println("Percentage: %.2f%%".format(cinema.coverage()))
                println("Current income: $${cinema.currentIncome}")
                println("Total income: $${cinema.totalIncome}")
            }
        }
    } while (option > 0)
}

fun menuOption(): Int {
    println("1. Show the seats")
    println("2. Buy a ticket")
    println("3. Statistics")
    println("0. Exit")
    return readLine()!!.toInt()
}

class Cinema(private val rows: Int, private val seatsInRow: Int) {
    companion object {
        const val FRONT_PRICE = 10
        const val BACK_PRICE = 8
    }

    private val seats = Array(rows) { Array(seatsInRow) { false } }
    private val totalSeats = rows * seatsInRow
    var currentIncome = 0

    private val frontRows = rows / 2
    private val backRows = rows - frontRows

    val totalIncome = if (totalSeats < 60) {
        totalSeats * FRONT_PRICE
    } else {
        (frontRows * seatsInRow * FRONT_PRICE) + (backRows * seatsInRow * BACK_PRICE)
    }

    fun book(row: Int, seat: Int): Int {
        if (row <= rows && seat <= seatsInRow) {
            if (seats[row - 1][seat - 1]) {
                throw CannotProceedException("That ticket has already been purchased!")
            }
            seats[row - 1][seat - 1] = true
        } else {
            throw IllegalArgumentException("Wrong input!")
        }
        val price = calculateTicketPrice(row)
        currentIncome += price
        return price
    }

    fun calculateTicketPrice(row: Int): Int {
        return if (totalSeats <= 60) FRONT_PRICE else {
            if (row <= frontRows) FRONT_PRICE else BACK_PRICE
        }
    }

    fun purchasedTicketsNumber(): Int {
        var tickets = 0
        for (seatsInRow in seats) {
            for (seat in seatsInRow) {
                tickets += if (seat) 1 else 0
            }
        }
        return tickets
    }

    fun coverage(): Double {
        return 100 * purchasedTicketsNumber().toDouble() / totalSeats
    }

    override fun toString(): String {
        var s = "Cinema:\n "
        for (i in 1..seatsInRow) {
            s += " $i"
        }
        s += "\n"
        for (i in 1..rows) {
            s += "$i"
            for (j in 1..seatsInRow) {
                s += if (seats[i - 1][j - 1]) " B" else " S"
            }
            s += "\n"
        }
        return s
    }
}
