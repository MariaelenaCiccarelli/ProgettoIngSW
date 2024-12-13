package com.danilo.lootmarket.modelview

import android.graphics.Bitmap
import java.time.ZonedDateTime

data class AuctionLiveView(val id: Int, val titoloAsta: String, val ultimaOfferta: Double, val dataScadenza: ZonedDateTime, val immagineProdotto: Bitmap, val autore: String, val offertaFatta: Boolean)
