package com.danilo.lootmarket

import android.graphics.Bitmap
import java.time.ZonedDateTime

data class Auction(val id: Int,
                   val titoloAsta: String,
                   val ultimaOfferta: Double,
                   val dataScadenza: ZonedDateTime,
                   val immagineProdotto: Bitmap,
                   val testoDescrizione: String,
                   val Categoria: String,
                   var tipoAsta: String )
