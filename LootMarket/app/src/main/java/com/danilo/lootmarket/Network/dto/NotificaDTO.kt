package com.danilo.lootmarket.Network.dto

class NotificaDTO(val id: Int,
    val tipo: Int,
    val Destinatario: String,
    var idAsta: Int,
    val titoloAsta: String
) {


    public fun getMessaggio(): String {
        when (tipo) {
            1 -> return "La tua asta \"$titoloAsta\" si è conclusa senza alcun vincitore."
            2 -> return "La tua asta \"$titoloAsta\" si è conclusa con successo! Congratulazioni!"
            3 -> return "L'asta \"$titoloAsta\" che seguivi è scaduta."
            4 -> return "Ti sei aggiudicato l'asta \"$titoloAsta\", congratulazioni!"
            5 -> return "La tua asta \"$titoloAsta\" ha ricevuto una nuova offerta!"
            6 -> return "L'asta \"$titoloAsta\" a cui hai presentato una offerta ha ricevuto una controfferta!"
            7 -> return "Non ti sei aggiudicato l'asta \"$titoloAsta\", perché la tua offerta non superava la soglia minima richiesta."
            8 -> return "La tua asta \"$titoloAsta\" si è conclusa senza alcun vincitore, poiché nessuna offerta superava la soglia minima."
            else -> {
                return "errore, tipo notifica non valido"
            }
        }
    }

    public fun getTitolo(): String {
        when (tipo) {
            1 -> return "Asta conclusa senza vincitore."
            2 -> return "Asta conclusa con successo!"
            3 -> return "Asta scaduta."
            4 -> return "Asta aggiudicata!"
            5 -> return "Nuova offerta!"
            6 -> return "Controfferta!"
            7 -> return "Asta non aggiudicata."
            8 -> return "Asta conclusa senza vincitore."
            else -> {
                return "errore, tipo notifica non valido"
            }
        }
    }
}
