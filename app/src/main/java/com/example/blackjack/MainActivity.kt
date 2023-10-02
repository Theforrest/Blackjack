package com.example.blackjack

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.blackjack.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val blackjack: Int = 21
    private val dineroInicial: Int = 100

    private val ganador: String = "GANADOR! \uD83D\uDE0E"
    private val perdedor: String = "PERDEDOR \uD83D\uDE2D"
    private val empate: String = "EMPATE \uD83D\uDE10"
    private val endeudado: String = "SIN BLANCA... \uD83D\uDC80"
    private val blackjackResultado: String = "BLACKJACK! \uD83E\uDD11"

    private var mazo: MutableList<Carta> = mutableListOf()

    private lateinit var jugador: Jugador
    private lateinit var casa: Casa

    private lateinit var manoJugadorView: Array<ImageView>
    private lateinit var manoCasaView: Array<ImageView>


    private var numCartaJugador: Int = 0
    private var numCartaCasa: Int = 0

    private var valorManoJugador: Int = 0
    private var valorManoCasa: Int = 0

    private var apuesta: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        crearMazo()

        asignarManos()

        setListeners()

    }

    private fun asignarManos() {
        manoJugadorView = arrayOf(
            binding.ivPlayer1,
            binding.ivPlayer2,
            binding.ivPlayer3,
            binding.ivPlayer4,
            binding.ivPlayer5,
            binding.ivPlayer6,
            binding.ivPlayer7,
            binding.ivPlayer8,
            binding.ivPlayer9,
            binding.ivPlayer10,
            binding.ivPlayer11
        )

        manoCasaView = arrayOf(
            binding.ivHouse1,
            binding.ivHouse2,
            binding.ivHouse3,
            binding.ivHouse4,
            binding.ivHouse5,
            binding.ivHouse6,
            binding.ivHouse7,
            binding.ivHouse8,
            binding.ivHouse9,
            binding.ivHouse10,
            binding.ivHouse11
        )
    }

    private fun crearMazo() {
        for (i in 1..13) {
            for (palo in Palo.values()) {
                mazo.add(Carta(i, palo))
            }
        }
    }

    private fun empezarPartida() {

        jugador = Jugador(dineroInicial)
        casa = Casa()
        binding.tvDinero.text = jugador.dinero.toString()
        devolverCartasAlMazo()

        binding.tvResultado.visibility = View.INVISIBLE
        binding.btnJugar.visibility = View.INVISIBLE
        binding.btnEmpezar.visibility = View.VISIBLE
        binding.etApuesta.visibility = View.VISIBLE
        binding.etApuesta.isEnabled = true
        binding.tvDinero.visibility = View.VISIBLE


    }

    private fun empezarRonda() {
        val textoApuesta = binding.etApuesta.text

        if (textoApuesta.isNotEmpty() && textoApuesta.toString().toInt() <= jugador.dinero
            && textoApuesta.toString().toInt() > 0
        ) {
            apuesta = textoApuesta.toString().toInt()
        } else {
            binding.etApuesta.setText("1")
        }

        binding.etApuesta.isEnabled = false
        binding.tvResultado.visibility = View.INVISIBLE
        binding.btnEmpezar.visibility = View.INVISIBLE
        binding.btnPlantarse.visibility = View.VISIBLE
        binding.btnRobar.visibility = View.VISIBLE

        resetearRonda()
    }

    private fun terminarRonda() {
        if (jugador.dinero > 0) {
            binding.etApuesta.isEnabled = true
            binding.btnEmpezar.visibility = View.VISIBLE

        } else {
            binding.tvResultado.text = endeudado
            binding.btnJugar.visibility = View.VISIBLE
            binding.etApuesta.visibility = View.INVISIBLE

        }

        binding.tvResultado.visibility = View.VISIBLE
        binding.btnPlantarse.visibility = View.INVISIBLE
        binding.btnRobar.visibility = View.INVISIBLE
        binding.tvDinero.text = jugador.dinero.toString()

        revelarCartaCasaBocabajo()
    }

    private fun setListeners() {
        binding.btnRobar.setOnClickListener {
            robarCartaJugador(cartaRandom())
            comprobarManoJugador()
        }

        binding.btnPlantarse.setOnClickListener {
            var ningunaOcurrencia = true
            while (valorManoCasa <= 16 && ningunaOcurrencia) {
                robarCartaCasa(cartaRandom())
                ningunaOcurrencia = comprobarManoCasa()
            }
            if (ningunaOcurrencia) {
                comprobarGanador()
            }

        }

        binding.btnJugar.setOnClickListener {
            empezarPartida()
        }
        binding.btnEmpezar.setOnClickListener {
            empezarRonda()
        }
    }

    private fun cartaRandom(): Carta {
        val random = Random.nextInt(0, mazo.size - 1)
        return mazo[random]
    }

    private fun comprobarManoJugador() {
        if (valorManoJugador > blackjack) {
            partidaPerdida()
        } else if (valorManoJugador == blackjack) {
            partidaGanadaBlackjack()
        }
    }

    private fun comprobarManoCasa(): Boolean {
        var ningunaOcurrencia = true
        if (valorManoCasa > blackjack) {
            if (valorManoJugador <= blackjack) {
                partidaGanada()
                ningunaOcurrencia = false
            }
        } else if (valorManoCasa == blackjack) {
            if (valorManoCasa != valorManoJugador) {
                partidaPerdida()
            } else {
                partidaEmpatada()
            }
            ningunaOcurrencia = false
        }
        return ningunaOcurrencia
    }

    private fun comprobarGanador() {
        if (valorManoJugador > valorManoCasa) {
            partidaGanada()
        } else if (valorManoJugador < valorManoCasa) {
            partidaPerdida()
        } else {
            partidaEmpatada()
        }
    }

    private fun partidaGanada() {
        jugador.dinero += apuesta
        binding.tvResultado.text = ganador
        terminarRonda()
    }

    private fun partidaGanadaBlackjack() {
        jugador.dinero += apuesta * 2
        binding.tvResultado.text = blackjackResultado
        terminarRonda()
    }

    private fun partidaPerdida() {
        jugador.dinero -= apuesta
        binding.tvResultado.text = perdedor
        terminarRonda()
    }

    private fun partidaEmpatada() {
        binding.tvResultado.text = empate
        terminarRonda()
    }

    private fun robarCartaJugador(carta: Carta) {

        val cartaView = manoJugadorView[numCartaJugador]

        robarCarta(jugador, carta, cartaView)

        numCartaJugador++
        valorManoJugador = jugador.valorMano()

    }

    private fun robarCartaCasa(carta: Carta) {

        val cartaView = manoCasaView[numCartaCasa]

        robarCarta(casa, carta, cartaView)

        numCartaCasa++
        valorManoCasa = casa.valorMano()

    }

    private fun robarCarta(persona: Persona, carta: Carta, cartaView: ImageView) {
        try {
            val nombreCarta: String = carta.toString()
            persona.robarCarta(carta)

            cartaView.setImageResource(obtenerIdNombre(nombreCarta))
            cartaView.visibility = View.VISIBLE

            mazo.remove(carta)
        } catch (e: UnsupportedOperationException) {
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun robarCartaCasaBocabajo(carta: Carta) {
        try {
            val cartaView = manoCasaView[numCartaCasa]

            casa.robarCarta(carta)

            cartaView.setImageResource(R.drawable.card_back)
            cartaView.visibility = View.VISIBLE

            mazo.remove(carta)
            numCartaCasa++
        } catch (e: UnsupportedOperationException) {
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
        }


    }

    private fun revelarCartaCasaBocabajo() {
        val carta = casa.getCartaBocabajo()
        if (carta != null) {
            val cartaView = manoCasaView[0]
            val nombreCarta: String = carta.toString()

            cartaView.setImageResource(obtenerIdNombre(nombreCarta))
        }
    }

    private fun resetearRonda() {
        devolverCartasAlMazo()

        resetearVariables()

        robarCartasIniciales()

        if (comprobarManoCasa()) {
            comprobarManoJugador()
        }
    }

    private fun robarCartasIniciales() {
        for (i in 0..1) {
            val carta = cartaRandom()
            robarCartaJugador(carta)
        }

        robarCartaCasaBocabajo(cartaRandom())

        robarCartaCasa(cartaRandom())
    }

    private fun resetearVariables() {
        numCartaJugador = 0
        numCartaCasa = 0
        valorManoJugador = 0
        valorManoCasa = 0
    }

    private fun devolverCartasAlMazo() {
        mazo.addAll(jugador.vaciarMano())
        mazo.addAll(casa.vaciarMano())

        esconderCartas()
    }

    private fun esconderCartas() {
        for (carta in manoJugadorView) {
            if (carta.isVisible) {
                carta.visibility = View.INVISIBLE
            }
        }
        for (carta in manoCasaView) {
            if (carta.isVisible) {
                carta.visibility = View.INVISIBLE
            }
        }
    }

    private fun obtenerIdNombre(nombre: String): Int {
        val resources: Resources = resources
        return resources.getIdentifier(
            nombre, "drawable",
            packageName
        )
    }
}