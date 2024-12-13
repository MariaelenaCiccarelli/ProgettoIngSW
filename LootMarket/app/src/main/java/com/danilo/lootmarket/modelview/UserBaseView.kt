package com.danilo.lootmarket.modelview

import android.graphics.Bitmap
import java.time.LocalDate

data class UserBaseView(var immagine: Bitmap, var nome: String, var codiceFiscale: String, var mail: String, var dataDiNascita: LocalDate, var nazione: String, var numeroCellulare: String, var indirizzoSpedizione: IndirizzoView, var indirizzoFatturazione: IndirizzoView, var sito: String, var socialFacebook: String, var socialInstagram: String, var biografia: String)
