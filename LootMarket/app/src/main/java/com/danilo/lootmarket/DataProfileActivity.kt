package com.danilo.lootmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Network.RetrofitInstance
import com.danilo.lootmarket.Network.dto.AstaDTO
import com.danilo.lootmarket.Network.dto.MyToken
import com.danilo.lootmarket.Network.dto.UtenteAutenticazioneDTO
import com.danilo.lootmarket.Network.dto.UtenteDTO
import com.danilo.lootmarket.databinding.ActivityProfiledataBinding
import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

class DataProfileActivity: AppCompatActivity() {

    private var contesto: Context = this

    private lateinit var binding: ActivityProfiledataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email: String = intent.getStringExtra("email").toString()
        val password: String = intent.getStringExtra("password").toString()
        enableEdgeToEdge()
        binding = ActivityProfiledataBinding.inflate(layoutInflater)

        val spinner : Spinner = binding.spinnerNazionePaginaProfileData
        ArrayAdapter.createFromResource(
            this,
            R.array.countries_array,
            android.R.layout.simple_spinner_item
        ).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        binding.editTextCellularePaginaProfileData.customSelectionActionModeCallback = object : ActionMode.Callback {
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
        binding.bottoneConfermaPaginaProfileData.setOnClickListener{
            var nome = binding.editTextNomePaginaProfileData.text.toString()
            var cognome = binding.editTextCognomePaginaProfileData.text.toString()
            var codiceFiscale = binding.editTextCodiceFiscalePaginaProfileData.text.toString()
            var cellulare = binding.editTextCellularePaginaProfileData.text.toString()
            var dataNascita = LocalDate.of(binding.datePickerPaginaProfileData.year, binding.datePickerPaginaProfileData.month, binding.datePickerPaginaProfileData.dayOfMonth)
            val patternCodiceFiscale = Pattern.compile("^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]\$")
            val nazione = binding.spinnerNazionePaginaProfileData.selectedItem.toString()
            
            if((nome == "")|| (cognome =="")||(codiceFiscale == "")||(cellulare=="")){
                Toast.makeText(this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()

            }else if(!(patternCodiceFiscale.matcher(codiceFiscale).matches())){
                Toast.makeText(this, "Codice Fiscale non valido", Toast.LENGTH_SHORT).show()
            }else if((dataNascita.until(LocalDate.now(), ChronoUnit.YEARS))<18){
                Toast.makeText(this, "Devi essere maggiorenne per iscriverti!", Toast.LENGTH_SHORT).show()
            }else{

                var utenteAutenticazioneDTO =UtenteAutenticazioneDTO(nome, cognome, codiceFiscale, password,  email, dataNascita.year, dataNascita.monthValue, dataNascita.dayOfMonth, nazione, cellulare)
                postRegistraUtente(utenteAutenticazioneDTO)

            }
        }

        setContentView(binding.root)


    }

    private fun postRegistraUtente(utenteAutenticazioneDTO: UtenteAutenticazioneDTO){

        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.postRegistraUtente(utenteAutenticazioneDTO)
            }catch (e: IOException){
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                Toast.makeText(contesto, "Registrazione fallita!", Toast.LENGTH_SHORT).show()
                val intent = Intent(contesto, MainActivity::class.java)
                startActivity(intent)
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")

                Toast.makeText(contesto, "Registrazione fallita!", Toast.LENGTH_SHORT).show()
                val intent = Intent(contesto, MainActivity::class.java)
                startActivity(intent)
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.e("MyNetwork", "Response is successful")
                var jwtToken: MyToken = response.body()!!
                Toast.makeText(contesto, jwtToken.token, Toast.LENGTH_SHORT).show()
                Toast.makeText(contesto, "Registrazione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                val intent = Intent(contesto, HomeActivity::class.java)
                intent.putExtra("mail", utenteAutenticazioneDTO.mail)
                intent.putExtra("token", jwtToken.token)
                startActivity(intent)

                return@async
            }else{
                Log.e("MyNetwork", "Response not successful")

                Toast.makeText(contesto, "Registrazione fallita!", Toast.LENGTH_SHORT).show()
                val intent = Intent(contesto, MainActivity::class.java)
                startActivity(intent)

                return@async
            }
        }
    }


}