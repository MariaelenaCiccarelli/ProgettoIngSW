package com.danilo.lootmarket.view.home

import android.content.Intent

import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.Controller.Controller
import com.danilo.lootmarket.view.adapters.NotificationAdapters

import com.danilo.lootmarket.databinding.ActivityNotificationBinding
import kotlinx.coroutines.async


class NotificationActivity: AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var controller: Controller
    private lateinit var mail: String
    private lateinit var token: String
    private lateinit var notificationAdapter: NotificationAdapters
    private var contesto = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        mail= intent.getStringExtra("mail").toString()
        token= intent.getStringExtra("token").toString()
        setContentView(binding.root)
        controller = Controller()

        notificationAdapter = NotificationAdapters((listOf()))
        lifecycleScope.async {
            if(!(controller.getNotificheUtente(mail, token, notificationAdapter, resources))){
                finish()
            }
        }



        binding.RecyclerViewFrammentoHome.layoutManager = LinearLayoutManager(this)
        binding.RecyclerViewFrammentoHome.adapter = notificationAdapter


        notificationAdapter.onItemClick = {
            lifecycleScope.async {
                if(controller.postEliminaNotificaUtente(it.idNotifica, it.idAsta, token)){
                    val intent = Intent(contesto, HomeActivity::class.java)
                    intent.putExtra("mail", mail)
                    intent.putExtra("token", token)
                    intent.putExtra("detailsAsta", true)
                    intent.putExtra("idAstaDetails", it.idAsta)
                    startActivity(intent)
                }else{
                    finish()
                }
            }
        }

        binding.imageViewBackButtonPaginaNotification.setOnClickListener{
            finish()
        }
        binding.textViewBackHomePaginaNotification.setOnClickListener{
            finish()
        }
    }




}