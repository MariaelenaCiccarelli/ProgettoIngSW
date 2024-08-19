package com.danilo.lootmarket

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AuctionsAdapter (private var auctions: List<Auction>, context: Context): RecyclerView.Adapter<AuctionsAdapter.AuctionViewHolder>(){

    class AuctionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val testoTitoloView: TextView = itemView.findViewById(R.id.testoTitoloAstaAuctionItem)
        val testoDescrizioneView: TextView = itemView.findViewById(R.id.testoDescrizioneAuctionItem)
        val testoUltimaOffertaView: TextView = itemView.findViewById(R.id.testoUltimaOffertaAuctionItem)
        val testoTempoRimanenteView: TextView = itemView.findViewById(R.id.testoTempoRimanenteAuctionItem)
        val immagineProdottoView: ImageView = itemView.findViewById(R.id.immagineAuctionItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.auction_item, parent, false)
        return AuctionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuctionViewHolder, position: Int) {
        val auction = auctions[position]
        holder.testoTitoloView.text = auction.titoloAsta
        holder.testoDescrizioneView.text = auction.testoDescrizione
        holder.testoUltimaOffertaView.text = "Ultima Offerta: €"+ auction.ultimaOfferta.toString()
        if(auction.tempoRimanente>0){
            holder.testoTempoRimanenteView.text = ""+auction.tempoRimanente.toString()+"h rim."
        }else{
            holder.testoTempoRimanenteView.text =">1h rim."
        }
    }

    override fun getItemCount(): Int = auctions.size

    fun refreshData(newNotes: List<Auction>){
        auctions = newNotes
        this.notifyDataSetChanged()
    }
}