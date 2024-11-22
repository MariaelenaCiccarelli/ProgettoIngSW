package com.danilo.lootmarket

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.Date

data class Auction(val id: Int,
                   val titoloAsta: String,
                   val ultimaOfferta: Double,
                   val dataScadenza: ZonedDateTime,
                   val immagineProdotto: Bitmap,
                   val testoDescrizione: String,
                   val Categoria: String,
                   var tipoAsta: String )
