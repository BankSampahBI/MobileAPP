package com.example.banksampah.ui.add

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.banksampah.data.Result
import com.example.banksampah.data.di.Injection
import com.example.banksampah.databinding.ActivityAddPenjualanBinding
import com.example.banksampah.ui.model.AddPenjualanViewModel
import com.example.banksampah.ui.model.ViewModelFactory
import com.example.banksampah.utils.uriToFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddPenjualanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPenjualanBinding
    private lateinit var viewModel: AddPenjualanViewModel
    private lateinit var token: String

    private var selectedImageFile: File? = null
    private var cameraImageFile: File? = null
    private var currentPhotoPath: String? = null

    companion object {
        private const val REQUEST_CAMERA = 101
        private const val REQUEST_GALLERY = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil token dan setup ViewModel
        Injection.provideUserPreference(this).getSession().asLiveData()
            .observe(this) { user ->
                token = user.token
                val repo = Injection.provideRepository(this, token)
                viewModel = ViewModelProvider(this, ViewModelFactory(repo))[AddPenjualanViewModel::class.java]
            }

        binding.btnPilihFoto.setOnClickListener {
            showImagePickerDialog()
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

    private fun showImagePickerDialog() {
        val options = arrayOf("Kamera", "Galeri")
        AlertDialog.Builder(this)
            .setTitle("Pilih Gambar")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openCamera() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA)
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createImageFile()
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            photoFile
        )
        cameraImageFile = photoFile // simpan sementara
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    cameraImageFile?.let {
                        selectedImageFile = it
                        binding.imgPreview.setImageURI(Uri.fromFile(it))
                    }
                }
                REQUEST_GALLERY -> {
                    val uri = data?.data ?: return
                    selectedImageFile = uriToFile(uri, this)
                    binding.imgPreview.setImageURI(uri)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            Toast.makeText(this, "Izin kamera dibutuhkan", Toast.LENGTH_SHORT).show()
        }
    }
}
