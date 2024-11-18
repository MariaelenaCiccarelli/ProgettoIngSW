package com.danilo.lootmarket.Network.dto

import com.danilo.lootmarket.Indirizzo
import okio.ByteString

data class UtenteDTO(
    var nome: String,
    var codiceFiscale: String,
    var mail: String,
    var dataDiNascitaAnno: Int,
    var dataDiNascitaMese: Int,
    var dataDiNascitaGiorno: Int,
    var nazione: String,
    var numeroCellulare: String,
    var indirizzoSpedizione: Indirizzo,
    var indirizzoFatturazione: Indirizzo,
    var sito: String,
    var socialFacebook: String,
    var socialInstagram: String,
    var biografia: String,
    var ragioneSociale: String,
    var partitaIva: String,
    var numeroAziendale: String,
    var immagineProfilo: String
)
