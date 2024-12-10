package com.danilo.lootmarket

import android.graphics.Bitmap
import java.time.ZonedDateTime

data class AuctionViewHistory(val id: Int, val titoloAsta: String, val ultimaOfferta: Double, val dataScadenza: ZonedDateTime, val immagineProdotto: Bitmap, val autore: String, val offertaFatta: Boolean)
