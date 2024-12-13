package com.danilo.lootmarket.modelview

import java.time.ZonedDateTime

data class DettagliAstaView(val idAsta: Int,
                           val emailCreatore: String,
                           val titolo: String,
                           val categoria: String,
                           val prezzoPartenza: Double,
                           val dataScadenza: ZonedDateTime,
                           val descrizione: String,
                           val ultimaOfferta: Double,
                           val sogliaMinima: Int,
                           val tipoAsta: String,
                           val ultimaOffertaTua: Boolean,
                           val immagineAsta: String,
                           val nomeAutore: String,
                           val statusLegame: String)
