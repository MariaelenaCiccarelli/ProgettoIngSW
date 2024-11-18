package com.danilo.lootmarket

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
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
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Network.RetrofitInstance
import com.danilo.lootmarket.Network.dto.AstaDTO
import com.danilo.lootmarket.databinding.ActivityAddAuctionBinding
import com.danilo.lootmarket.databinding.ActivityRecoveryBinding
import kotlinx.coroutines.async
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.ZoneId
import java.time.ZonedDateTime

class AddAuctionActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAddAuctionBinding
    lateinit var galleryUri: Uri

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        galleryUri = it!!

        try {
            binding.imageViewAddedImagePaginaAddAuction.setImageURI(galleryUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mail: String = intent.getStringExtra("mail").toString()
        val token: String = intent.getStringExtra("token").toString()
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
                Log.println(Log.INFO, "MyNetwork", "Inizio a costruire la richiesta")

                val fileDir = applicationContext.filesDir
                val imageFile = File(fileDir, "immagineProdotto.png")

                Log.println(Log.INFO, "MyNetwork", "IHo creato imageFIle")
                val inputStream = contentResolver.openInputStream(galleryUri)
                val outputStream = FileOutputStream(imageFile)


                inputStream!!.copyTo((outputStream))
                Log.println(Log.INFO, "MyNetwork", "Ho finito con gli stream")

                val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val partImmagineDTO = MultipartBody.Part.createFormData("immagineProdottoDTO", imageFile.name, requestBody)
                Log.println(Log.INFO, "MyNetwork", "Ho creato la multipart")




                //var immagineBitmap = immagine.toBitmap()
                //val stream = ByteArrayOutputStream()
                //immagineBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                //var immagineDTO: ByteArray = stream.toByteArray()
                //var immagineDTO: ByteArray = ByteArray(5)



                var momentoScadenza = ZonedDateTime.of(binding.datePickerScadenzaPaginaAddAuction.year, binding.datePickerScadenzaPaginaAddAuction.month+1, binding.datePickerScadenzaPaginaAddAuction.dayOfMonth, 23, 59,59,59, ZoneId.of("GMT") )

                var astaDTO = AstaDTO(0, mail, titolo, categoria, prezzo.toDouble(), momentoScadenza.year, momentoScadenza.month.value, momentoScadenza.dayOfMonth, descrizione, prezzo.toDouble(), sogliaMinima.toDouble(), tipo, false, "immagineInMultiPart")

                /*
                val requestBodyAsta: RequestBody = MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("TitoloAsta", astaDTO.titolo)
                                        .addFormDataPart("name", "name")
                                        .addFormDataPart(
                                        "immagineProdottoDTO",
                                        imageFile.name,
                                        RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                                             )
                                        .build()

                 */

                 */
                val requestBodyAsta = astaDTO.titolo.toRequestBody("text/plain".toMediaTypeOrNull())
                val headers: Headers
                val partTitoloAstaDTO = MultipartBody.Part.create( requestBodyAsta)
                Log.println(Log.INFO, "MyNetwork", "Ho creato la multipart")


                postAsta(partImmagineDTO, astaDTO, token)
                Toast.makeText(this, "Creazione Asta avvenuta con successo!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
    /*
    private fun postAsta(astaDTO: AstaDTO, token: String){

        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.postNuovaAsta(astaDTO, token)
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
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                finish()
                return@async
            }
        }
    }

     */

    private fun postAsta(partImmagine: MultipartBody.Part, astaDTO: AstaDTO, token: String){

        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.postNuovaAstaConImmagine(partImmagine, astaDTO, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return@async
            }catch (e: Exception){
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.e("MyNetwork", "Response is successful")
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