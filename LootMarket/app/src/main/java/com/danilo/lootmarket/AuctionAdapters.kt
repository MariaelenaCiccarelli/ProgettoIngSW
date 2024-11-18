package com.danilo.lootmarket

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalUnit
import java.util.Date
import java.util.Locale

class AuctionsAdapter(public var auctions: ArrayList<Auction>, context: Context): RecyclerView.Adapter<AuctionsAdapter.AuctionViewHolder>(){

    class AuctionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val testoTitoloView: TextView = itemView.findViewById(R.id.testoTitoloAstaAuctionItem)
        val testoDescrizioneView: TextView = itemView.findViewById(R.id.testoDescrizioneAuctionItem)
        val testoUltimaOffertaView: TextView = itemView.findViewById(R.id.testoUltimaOffertaAuctionItem)
        val testoTempoRimanenteView: TextView = itemView.findViewById(R.id.testoTempoRimanenteAuctionItem)
        val immagineProdottoView: ImageView = itemView.findViewById(R.id.immagineAuctionItem)
        val imageViewBollinoView: MaterialCardView = itemView.findViewById(R.id.cardBollinoInversaAuctionItem)
    }

    var onItemClick: ((Auction)-> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.auction_item, parent, false)
        return AuctionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuctionViewHolder, position: Int) {
        val auction = auctions[position]
        Log.println(Log.INFO, "MyAdapter", auction.toString())
        holder.testoTitoloView.text = auction.titoloAsta
        holder.testoDescrizioneView.text = auction.testoDescrizione
        holder.testoUltimaOffertaView.text = "Ultima Offerta: €"+ "%,.2f".format(Locale.ITALIAN,auction.ultimaOfferta)

        //var tempoRimanente = (auction.DataScadenza.to - ZonedDateTime.now().toEpochSecond()) /360
        if(auction.dataScadenza.isBefore(ZonedDateTime.now())){
            holder.testoTempoRimanenteView.text = "Scaduta"
            holder.testoTempoRimanenteView.setTextColor(Color.parseColor("#4d251b"))
        }else {
            holder.testoTempoRimanenteView.setTextColor(Color.parseColor("#c01b1b"))
            var tempoRimanente = ZonedDateTime.now().until(auction.dataScadenza, ChronoUnit.HOURS)
            if(tempoRimanente >48) {
                holder.testoTempoRimanenteView.text =""+(tempoRimanente/24).toString() + "g rim."
            }else if(48>tempoRimanente && tempoRimanente>24){
                holder.testoTempoRimanenteView.text ="1g rim."
            }else if (tempoRimanente > 0) {
                holder.testoTempoRimanenteView.text = "" + tempoRimanente.toString() + "h rim."
            } else {
                holder.testoTempoRimanenteView.text = ">1h rim."
            }
        }

        holder.immagineProdottoView.setImageBitmap(auction.immagineProdotto)
        if(auction.tipoAsta == "Asta Inversa"){
            holder.imageViewBollinoView.isVisible= true
        }
        if(auction.tipoAsta == "Asta a Tempo Fisso"){
            holder.imageViewBollinoView.isVisible= false
        }

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(auction)
        }
    }

    override fun getItemCount(): Int = auctions.size

    fun refreshData(newAuctions: ArrayList<Auction>){

        auctions = newAuctions
        this!!.notifyDataSetChanged()
    }
}