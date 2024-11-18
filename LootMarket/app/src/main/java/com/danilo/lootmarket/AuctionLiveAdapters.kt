package com.danilo.lootmarket

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalUnit
import java.util.Date
import java.util.Locale

class AuctionsLiveAdapter (public var auctionsViewHistory: List<AuctionViewHistory>, context: Context, var autoreVisibile: Boolean = true): RecyclerView.Adapter<AuctionsLiveAdapter.AuctionViewHolder>(){

    class AuctionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val testoTitoloView: TextView = itemView.findViewById(R.id.testoTitoloAstaAuctionLiveItem)
        val testoUltimaOffertaView: TextView = itemView.findViewById(R.id.testoUltimaOffertaAuctionLiveItem)
        val testoTempoRimanenteView: TextView = itemView.findViewById(R.id.testoTempoRimanenteAuctionLiveItem)
        val immagineProdottoView: ImageView = itemView.findViewById(R.id.immagineAuctionLiveItem)
        val testoAutoreView: TextView = itemView.findViewById(R.id.testoAutoreAuctionLiveItem)
        val bollinoView: CardView = itemView.findViewById(R.id.cardBollinoAuctionLiveItem)

    }

    var onItemClick: ((AuctionViewHistory)-> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.auction_item_live, parent, false)
        return AuctionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuctionViewHolder, position: Int) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val auction = auctionsViewHistory[position]
        holder.testoTitoloView.text = auction.titoloAsta
        holder.testoUltimaOffertaView.text = "Ultima Offerta: €"+ "%,.2f".format(Locale.ITALIAN,auction.ultimaOfferta)
        if(autoreVisibile) {
            holder.testoAutoreView.text = auction.autore
        }else{
            holder.testoAutoreView.isVisible = false
        }
        holder.immagineProdottoView.setImageBitmap(auction.immagineProdotto)
        if(!auction.offertaFatta){
            holder.bollinoView.isVisible = false
        }else{
            holder.bollinoView.isVisible = true
        }

        if(auction.dataScadenza.isBefore(ZonedDateTime.now())){
            holder.testoTempoRimanenteView.text = "Scaduta il: "+auction.dataScadenza.format(formatter)
            holder.testoTempoRimanenteView.setTextColor(Color.parseColor("#4d251b"))
        }else {
            holder.testoTempoRimanenteView.setTextColor(Color.parseColor("#c01b1b"))
            var tempoRimanente = ZonedDateTime.now().until(auction.dataScadenza, ChronoUnit.HOURS)
            if(tempoRimanente >48) {
                holder.testoTempoRimanenteView.text =""+(tempoRimanente/24).toString() + " giorni rimanenti"
            }else if(48>tempoRimanente && tempoRimanente>24){
                holder.testoTempoRimanenteView.text ="1 giorno rimanente"
            }else if (tempoRimanente > 0) {
                holder.testoTempoRimanenteView.text = "" + tempoRimanente.toString() + "h rimanenti"
            } else {
                holder.testoTempoRimanenteView.text = ">1h rimanente"
            }
        }
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(auction)
        }
    }


    override fun getItemCount(): Int = auctionsViewHistory.size

    fun refreshData(newAuctions: List<AuctionViewHistory>){
        auctionsViewHistory = newAuctions
        this.notifyDataSetChanged()
    }
}