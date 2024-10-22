package com.danilo.lootmarket

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Network.RetrofitInstance
import com.danilo.lootmarket.Network.dto.AstaDTO
import com.danilo.lootmarket.databinding.ActivityAddAuctionBinding
import com.danilo.lootmarket.databinding.ActivityRecoveryBinding
import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException
import java.time.ZoneId
import java.time.ZonedDateTime

class AddAuctionActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAddAuctionBinding

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try {
            binding.imageViewAddedImagePaginaAddAuction.setImageURI(galleryUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddAuctionBinding.inflate(layoutInflater)

        val spinnerTipo : Spinner = binding.spinnerTipoAstaPaginaAddAuction
        ArrayAdapter.createFromResource(
            this,
            R.array.auction_type_array,
            android.R.layout.simple_spinner_item
        ).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTipo.adapter = adapter
        }

        val spinnerCategoria : Spinner = binding.spinnerCategoriaAstaPaginaAddAuction
        ArrayAdapter.createFromResource(
            this,
            R.array.auction_category_array,
            android.R.layout.simple_spinner_item
        ).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategoria.adapter = adapter
        }



        setContentView(binding.root)

        binding.spinnerTipoAstaPaginaAddAuction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(binding.spinnerTipoAstaPaginaAddAuction.selectedItem== "Asta inversa"){
                    binding.editTextSogliaMinimaoAstaPaginaAddAuction.setText("0.00")
                    binding.editTextSogliaMinimaoAstaPaginaAddAuction.isEnabled = false
                }else{
                    binding.editTextSogliaMinimaoAstaPaginaAddAuction.setText("")
                    binding.editTextSogliaMinimaoAstaPaginaAddAuction.isEnabled = true
                }
            }

        }

        binding.editTextPrezzoAstaPaginaAddAuction.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onDestroyActionMode(mode: ActionMode) {}
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }
        }
        binding.editTextSogliaMinimaoAstaPaginaAddAuction.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onDestroyActionMode(mode: ActionMode) {}
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }
        }

        binding.imageViewAddImageButtonPaginaAddAuction.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.imageViewBackButtonPaginaAddAuction.setOnClickListener{
            finish()
        }
        binding.textViewBackHomePaginaAddAuction.setOnClickListener{
            finish()
        }



        binding.bottoneConfermaPaginaAddAuction.setOnClickListener{

            var titolo = binding.editTextTitoloAstaPaginaAddAuction.text.toString()
            var prezzo = binding.editTextPrezzoAstaPaginaAddAuction.text.toString()
            var descrizione = binding.editTextDescrizioneAstaPaginaAddAuction.text.toString()
            var immagine = binding.imageViewAddedImagePaginaAddAuction.drawable
            var categoria = binding.spinnerCategoriaAstaPaginaAddAuction.selectedItem.toString()
            var sogliaMinima = binding.editTextSogliaMinimaoAstaPaginaAddAuction.text.toString()
            var tipo = binding.spinnerTipoAstaPaginaAddAuction.selectedItem.toString()



            if((titolo == "")|| (prezzo =="")||(descrizione == "")||(immagine == null)){
                Toast.makeText(this, "Compila tutti i campi obbligatori!", Toast.LENGTH_SHORT).show()
            }else{
                var momentoScadenza = ZonedDateTime.of(binding.datePickerScadenzaPaginaAddAuction.year, binding.datePickerScadenzaPaginaAddAuction.month+1, binding.datePickerScadenzaPaginaAddAuction.dayOfMonth, 23, 59,59,59, ZoneId.of("GMT") )
                //var auction = Auction(10,titolo, prezzo.toDouble(), momentoScadenza, immagine, descrizione, categoria, tipo)
                var astaDTO = AstaDTO(0, "danilo@mail.it", titolo, categoria, prezzo.toDouble(), momentoScadenza.year, momentoScadenza.month.value, momentoScadenza.dayOfMonth, descrizione, prezzo.toDouble(), sogliaMinima.toDouble(), tipo)
                postAsta(astaDTO)
                Toast.makeText(this, "Creazione Asta avvenuta con successo!", Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, auction.ultimaOfferta.toString(), Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
    private fun postAsta(astaDTO: AstaDTO){

        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.postNuovaAsta(astaDTO)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.e("MyNetwork", "Response is successful")
                return@async
            }else{
                Log.e("MyNetwork", "Response not successful")
                return@async
            }
        }
    }
}