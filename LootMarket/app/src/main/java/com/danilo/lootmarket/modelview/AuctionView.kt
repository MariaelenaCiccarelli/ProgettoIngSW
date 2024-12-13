package com.danilo.lootmarket.modelview

import android.graphics.Bitmap
import java.time.ZonedDateTime

data class AuctionView(val id: Int,
                       val titoloAsta: String,
                       val ultimaOfferta: Double,
                       val dataScadenza: ZonedDateTime,
                       val immagineProdotto: Bitmap,
                       val testoDescrizione: String,
                       val categoria: String,
                       var tipoAsta: String )
