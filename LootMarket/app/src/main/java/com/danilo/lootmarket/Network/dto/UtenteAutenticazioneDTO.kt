package com.danilo.lootmarket.Network.dto

data class UtenteAutenticazioneDTO(var nome: String,
                                   var cognome: String,
                                   var codiceFiscale: String,
                                   var password: String,
                                   var mail: String,
                                   var dataDiNascitaAnno: Int,
                                   var dataDiNascitaMese: Int,
                                   var dataDiNascitaGiorno: Int,
                                   var nazione: String,
                                   var numeroCellulare: String)
