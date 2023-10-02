package com.example.blackjack

enum class Palo(private val texto: String) {
    DIAMANTES("diamonds"),
    PICAS("spades"),
    TREBOLES("clubs"),
    CORAZONES("hearts");

    override fun toString(): String {
        return String.format("%s", texto)
    }
}