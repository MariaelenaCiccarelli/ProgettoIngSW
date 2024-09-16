package com.danilo.lootmarket

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.databinding.ActivityNotificationBinding
import com.danilo.lootmarket.databinding.ActivityProfiledataBinding
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

class NotificationActivity: AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    private lateinit var notificationAdapter: NotificationAdapters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var notifica1 = Notification((ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable), "L'asta sta per scadere!", "L’asta a cui sei iscritto sta per concludersi. Fai un’offerta per non perdere questa occasione!" )
        var notifica2 = Notification((ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable), "Rossi ti ha fatto un'offerta!", "La tua asta ha ricevuto un’offerta! Vedi di cosa si tratta!")
        var notifica3 = Notification((ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable), "Asta aggiudicata!", "Congratulazioni! L’asta a cui hai fatto un’offerta è terminata e te la sei aggiudicata!")
        var notifica4 = Notification((ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable), "Peccato", "L’asta a cui hai fatto un’offerta è terminata, ma... che peccato! La tua offerta era troppo bassa.")

        var notifiche: List<Notification>
        notifiche = listOf(notifica1, notifica2, notifica3, notifica4)


        notificationAdapter = NotificationAdapters(notifiche, this)

        binding.RecyclerViewFrammentoHome.layoutManager = LinearLayoutManager(this)
        binding.RecyclerViewFrammentoHome.adapter = notificationAdapter






















        binding.imageViewBackButtonPaginaNotification.setOnClickListener{
            finish()
        }
        binding.textViewBackHomePaginaNotification.setOnClickListener{
            finish()
        }

    }
}