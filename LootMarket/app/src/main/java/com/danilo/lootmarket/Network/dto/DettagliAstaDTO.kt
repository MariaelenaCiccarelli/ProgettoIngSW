package com.danilo.lootmarket.Network.dto

data class DettagliAstaDTO(val idAsta: Int,
                           val emailCreatore: String,
                           val titolo: String,
                           val categoria: String,
                           val prezzoPartenza: Double,
                           val anno: Int,
                           val mese: Int,
                           val giorno: Int,
                           val descrizione: String,
                           val ultimaOfferta: Double,
                           val sogliaMinima: Int,
                           val tipoAsta: String,
                           val ultimaOffertaTua: Boolean,
                           val immagineAsta: String,
                           val nomeAutore: String,
                           val statusLegame: String)
