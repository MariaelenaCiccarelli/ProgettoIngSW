package com.danilo.lootmarket.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danilo.lootmarket.modelview.NotificationView
import com.danilo.lootmarket.R


class NotificationAdapters(public var notificationViewViewHistory: List<NotificationView>): RecyclerView.Adapter<NotificationAdapters.NotificationViewHolder>(){

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titoloNotificaView:TextView = itemView.findViewById(R.id.textViewTitoloNotificationItem)
        val immagineNotificaView: ImageView = itemView.findViewById(R.id.immagineNotificationItem)
        val testoNotificaView: TextView = itemView.findViewById(R.id.textViewTestoNotificationItem)
    }

    var onItemClick: ((NotificationView)-> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }


    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notifica = notificationViewViewHistory[position]
        holder.titoloNotificaView.text = notifica.titolo
        holder.immagineNotificaView.setImageDrawable(notifica.immagine)
        holder.testoNotificaView.text = notifica.testo
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(notifica)
        }
    }


    override fun getItemCount(): Int = notificationViewViewHistory.size


    fun refreshData(newNotificationViews: List<NotificationView>){
        notificationViewViewHistory = newNotificationViews
        this.notifyDataSetChanged()
    }


}