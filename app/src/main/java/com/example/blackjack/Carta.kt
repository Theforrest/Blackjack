package com.example.blackjack

class Carta(val numero: Int, val palo: Palo) {

    private val numerosMap: Map<Int, String> = mapOf(
        1 to "ace",
        2 to "two",
        3 to "three",
        4 to "four",
        5 to "five",
        6 to "six",
        7 to "seven",
        8 to "eight",
        9 to "nine",
        10 to "ten",
        11 to "jack",
        12 to "queen",
        13 to "king"
    )

    override fun toString(): String {
        val valorCarta = numerosMap.getValue(numero)
        return String.format("%s_%s", valorCarta, palo)
    }
}