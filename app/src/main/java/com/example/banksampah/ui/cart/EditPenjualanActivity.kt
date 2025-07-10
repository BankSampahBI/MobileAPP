package com.example.banksampah.ui.cart

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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.banksampah.data.Repository
import com.example.banksampah.data.Result
import com.example.banksampah.data.di.Injection
import com.example.banksampah.data.remote.response.PenjualanResponseItem
import com.example.banksampah.databinding.ActivityEditPenjualanBinding
import com.example.banksampah.ui.model.ViewModelFactory
import com.example.banksampah.utils.uriToFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditPenjualanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPenjualanBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var repository: Repository
    private lateinit var penjualan: PenjualanResponseItem
    private lateinit var token: String

    private var selectedImage: File? = null
    private var currentPhotoPath: String? = null

    companion object {
        private const val REQUEST_CAMERA = 100
        private const val REQUEST_GALLERY = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        penjualan = intent.getParcelableExtra("EXTRA_PENJUALAN") ?: return finish()

        binding.etNama.setText(penjualan.namaBarang)
        binding.etDeskripsi.setText(penjualan.deskripsi)
        binding.etStok.setText(penjualan.stok.toString())
        binding.etHarga.setText(penjualan.harga.toString())
        Glide.with(this).load(penjualan.foto).into(binding.imgPreview)

        Injection.provideUserPreference(this).getSession().asLiveData().observe(this) { user ->
            token = user.token
            repository = Injection.provideRepository(this, token)
            viewModel = ViewModelProvider(this, ViewModelFactory(repository))[CartViewModel::class.java]

            binding.btnSimpan.setOnClickListener {
                val nama = binding.etNama.text.toString()
                val deskripsi = binding.etDeskripsi.text.toString()
                val stok = binding.etStok.text.toString().toIntOrNull() ?: 0
                val harga = binding.etHarga.text.toString().toIntOrNull() ?: 0

                viewModel.updatePenjualan(token, penjualan.id, nama, deskripsi, stok, harga, selectedImage)
                    .observe(this) { result ->
                        when (result) {
                            is Result.Loading -> {}
                            is Result.Success -> {
                                val updated = PenjualanResponseItem(
                                    id = penjualan.id,
                                    userId = penjualan.userId,
                                    namaBarang = nama,
                                    deskripsi = deskripsi,
                                    stok = stok,
                                    harga = harga,
                                    foto = if (selectedImage != null) "file://${selectedImage?.path}" else penjualan.foto,
                                    status = penjualan.status,
                                    catatanAdmin = penjualan.catatanAdmin,
                                    createdAt = penjualan.createdAt,
                                    updatedAt = penjualan.updatedAt
                                )

                                AlertDialog.Builder(this)
                                    .setTitle("Sukses")
                                    .setMessage("Data berhasil diperbarui")
                                    .setPositiveButton("OK") { _, _ ->
                                        val intent = Intent()
                                        intent.putExtra("UPDATED_PENJUALAN", updated)
                                        setResult(RESULT_OK, intent)
                                        finish()
                                    }
                                    .show()
                            }
                            is Result.Error -> {
                                AlertDialog.Builder(this)
                                    .setTitle("Gagal")
                                    .setMessage("Gagal update data")
                                    .setPositiveButton("OK", null)
                                    .show()
                            }
                        }
                    }
            }
        }

        binding.btnPilihFoto.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Kamera", "Galeri")
        AlertDialog.Builder(this)
            .setTitle("Pilih Gambar Dari")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndOpen()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun checkCameraPermissionAndOpen() {
        val permission = android.Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            requestPermissions(arrayOf(permission), REQUEST_CAMERA)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File = createImageFile()
        val photoURI: Uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
        selectedImage = photoFile
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
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
                    selectedImage?.let {
                        binding.imgPreview.setImageURI(Uri.fromFile(it))
                    }
                }
                REQUEST_GALLERY -> {
                    val uri = data?.data ?: return
                    selectedImage = uriToFile(uri, this)
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

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
