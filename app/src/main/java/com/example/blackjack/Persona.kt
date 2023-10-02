package com.example.blackjack

import java.lang.UnsupportedOperationException

abstract class Persona {

    private val manoCantidad: Int = 11

    protected var mano: Array<Carta?> = arrayOfNulls(manoCantidad)

    fun robarCarta(carta: Carta) {
        mano[encontrarVacia() ?: throw UnsupportedOperationException("ERROR: No hay espacio disponible en la mano")] = carta
    }

    private fun encontrarVacia(): Int? {
        for (i in mano.indices) {
            if (mano[i] == null) {
                return i
            }
        }
        return null
    }

    fun vaciarMano(): Array<Carta> {
        val cartasRestantes: MutableList<Carta> = mutableListOf()
        for (carta in mano) {
            if (carta != null) {
                cartasRestantes.add(carta)
            }
        }


        mano = arrayOfNulls(11)


        return cartasRestantes.toTypedArray()
    }

    fun valorMano(): Int {
        var valor = 0
        for (carta in mano) {
            if (carta != null) {
                valor += carta.numero
            }
        }
        return valor
    }
}