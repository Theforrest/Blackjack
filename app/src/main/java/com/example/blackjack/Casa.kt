package com.example.blackjack

class Casa : Persona() {

    fun getCartaBocabajo(): Carta? {
        return mano[0]
    }

}