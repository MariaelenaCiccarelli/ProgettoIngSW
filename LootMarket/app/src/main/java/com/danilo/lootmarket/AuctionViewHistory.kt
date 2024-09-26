package com.danilo.lootmarket

import android.graphics.drawable.Drawable
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.Date

data class AuctionViewHistory(val id: Int, val titoloAsta: String, val ultimaOfferta: Double, val dataScadenza: ZonedDateTime, val immagineProdotto: Drawable, val autore: String, val offertaFatta: Boolean)
