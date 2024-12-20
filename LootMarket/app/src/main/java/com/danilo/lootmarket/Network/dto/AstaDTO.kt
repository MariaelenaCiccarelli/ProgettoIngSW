package com.danilo.lootmarket.network.dto


data class AstaDTO(val idAsta: Int,
                   val emailCreatore: String,
                   val titolo: String ,
                   val categoria: String ,
                   val prezzoPartenza: Double,
                   val anno: Int,
                   val mese: Int,
                   val giorno: Int,
                   val descrizione: String,
                   val ultimaOfferta: Double,
                   val sogliaMinima: Double,
                   val tipoAsta: String,
                   var offertaFatta: Boolean,
                   val immagineAsta: String
)
