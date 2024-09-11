package com.danilo.lootmarket

import android.graphics.drawable.Drawable
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.Date

data class AuctionViewDetailed(val id: Int, val titoloAsta: String, val ultimaOfferta: Float, val DataScadenza: ZonedDateTime, val immagineProdotto: Drawable, val testoDescrizione: String, val Categoria: String )
