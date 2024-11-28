package com.danilo.lootmarket

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.Network.RetrofitInstance
import com.danilo.lootmarket.Network.dto.NotificaDTO
import com.danilo.lootmarket.databinding.ActivityNotificationBinding
import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException

class NotificationActivity(): AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    private lateinit var mail: String
    private lateinit var token: String
    private lateinit var arrayNotificheUtente: List<Notification>
    private lateinit var notificationAdapter: NotificationAdapters
    private var contesto = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)

        mail= intent.getStringExtra("mail").toString()
        token= intent.getStringExtra("token").toString()

        setContentView(binding.root)


        arrayNotificheUtente = listOf()
        getNotificheUtente(mail)



        notificationAdapter = NotificationAdapters(arrayNotificheUtente, this)

        binding.RecyclerViewFrammentoHome.layoutManager = LinearLayoutManager(this)
        binding.RecyclerViewFrammentoHome.adapter = notificationAdapter


        notificationAdapter.onItemClick = {
            postEliminaNotificaUtente(it.idNotifica, it.idAsta)

        }

        binding.imageViewBackButtonPaginaNotification.setOnClickListener{
            finish()
        }
        binding.textViewBackHomePaginaNotification.setOnClickListener{
            finish()
        }

    }

    private fun getNotificheUtente(email: String){
        var notificheCaricate = ArrayList<Notification>()
        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.getNotificheUtente(email, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO,"MyNetwork", "Response is successful")
                var notificheRecuperate: List<NotificaDTO> = response.body()!!
                for(notifica in notificheRecuperate){
                    Log.println(Log.INFO, "MyNetwork", notifica.titoloAsta)
                    var testoNotifica = notifica.getMessaggio()
                    var titoloNotifica = notifica.getTitolo()
                    var immagineNotifica: Drawable
                    when(notifica.tipo) {
                        1-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_heart_broken_24, null)!!
                        2-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_celebration_24, null)!!
                        3-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_hourglass_bottom_24, null)!!
                        4-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_auto_awesome_24, null)!!
                        5-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_gavel_24_notific, null)!!
                        6-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_hail_24, null)!!
                        7-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_mood_bad_24, null)!!
                        8-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_heart_broken_24, null)!!
                        else ->{
                            immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_report_gmailerrorred_24, null)!!
                        }

                    }
                    var notification = Notification(notifica.id, notifica.idAsta, immagineNotifica, titoloNotifica, testoNotifica)
                    notificheCaricate.add(notification)
                }
                arrayNotificheUtente = arrayNotificheUtente + notificheCaricate
                notificationAdapter.NotificationViewHistory = arrayNotificheUtente
                notificationAdapter!!.notifyDataSetChanged()

                return@async
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                finish()
                return@async
            }
        }

    }


    private fun postEliminaNotificaUtente(idNotifica: Int, idAsta: Int){
        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.postEliminaNotifica(idNotifica, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO,"MyNetwork", "Response is successful")
                val intent = Intent(contesto, HomeActivity::class.java)
                intent.putExtra("mail", mail)
                intent.putExtra("token", token)
                intent.putExtra("detailsAsta", true)
                intent.putExtra("idAstaDetails", idAsta)
                startActivity(intent)

                return@async
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                finish()
                return@async
            }
        }

    }


}