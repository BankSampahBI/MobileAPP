package com.example.banksampah.ui.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.banksampah.data.Result
import com.example.banksampah.data.di.Injection
import com.example.banksampah.databinding.ActivityAddPenjualanBinding
import com.example.banksampah.ui.model.AddPenjualanViewModel
import com.example.banksampah.ui.model.ViewModelFactory
import java.io.File

class AddPenjualanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPenjualanBinding
    private lateinit var viewModel: AddPenjualanViewModel
    private lateinit var token: String
    private var selectedImageFile: File? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val imageUri = result.data!!.data
            imageUri?.let {
                selectedImageFile = uriToFile(it)
                binding.imgPreview.setImageURI(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Injection.provideUserPreference(this).getSession().asLiveData()
            .observe(this) { user ->
                token = user.token
                val repo = Injection.provideRepository(this, token)
                viewModel = ViewModelProvider(this, ViewModelFactory(repo))[AddPenjualanViewModel::class.java]
            }

        binding.btnPilihFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }

        binding.btnSubmit.setOnClickListener {
            val jenis = binding.inputJenis.text.toString().trim()
            val deskripsi = binding.inputDeskripsi.text.toString().trim()
            val stokStr = binding.inputStok.text.toString().trim()
            val hargaStr = binding.inputHarga.text.toString().trim()

            val stok = stokStr.toIntOrNull()
            val harga = hargaStr.toIntOrNull()
            val foto = selectedImageFile

            if (jenis.isEmpty() || deskripsi.isEmpty() || stok == null || harga == null || foto == null) {
                Toast.makeText(this, "Lengkapi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = stok * harga

            viewModel.submitPenjualan(token, jenis, deskripsi, stok, harga, foto)

                .observe(this) { result ->
                    when (result) {
                        is Result.Loading -> Toast.makeText(this, "Mengirim data...", Toast.LENGTH_SHORT).show()
                        is Result.Success -> {
                            Toast.makeText(this, "Berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        is Result.Error -> Toast.makeText(this, "Gagal mengirim data!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0]) ?: 0
        val filePath = cursor?.getString(columnIndex)
        cursor?.close()
        return File(filePath ?: "")
    }
}
