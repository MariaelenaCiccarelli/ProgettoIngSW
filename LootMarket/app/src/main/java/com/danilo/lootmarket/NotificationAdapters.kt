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

class NotificationAdapters (public var NotificationViewHistory: List<Notification>, context: Context): RecyclerView.Adapter<NotificationAdapters.NotificationViewHolder>(){

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titoloNotificaView:TextView = itemView.findViewById(R.id.textViewTitoloNotificationItem)
        val immagineNotificaView: ImageView = itemView.findViewById(R.id.immagineNotificationItem)
        val testoNotificaView: TextView = itemView.findViewById(R.id.textViewTestoNotificationItem)

    }

    var onItemClick: ((Notification)-> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        val notifica = NotificationViewHistory[position]
        holder.titoloNotificaView.text = notifica.titolo
        holder.immagineNotificaView.setImageDrawable(notifica.immagine)
        holder.testoNotificaView.text = notifica.testo

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(notifica)
        }
    }

    override fun getItemCount(): Int = NotificationViewHistory.size

    fun refreshData(newNotifications: List<Notification>){
        NotificationViewHistory = newNotifications
        this.notifyDataSetChanged()
    }
}