package com.danilo.lootmarket

import android.graphics.drawable.Drawable
import java.time.LocalDate

data class UserBase(var id: Int, var immagine: Drawable, var nome: String, var codiceFiscale: String, var mail: String, var dataDiNascita: LocalDate, var nazione: String, var numeroCellulare: String, var indirizzoSpedizione: Indirizzo, var indirizzoFatturazione: Indirizzo, var sito: String, var socialFacebook: String, var socialInstagram: String, var Biografia: String)
