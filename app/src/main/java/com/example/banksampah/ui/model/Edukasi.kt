package com.example.banksampah.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Edukasi(
    val gambar : Int,
    val jenis: String,
    val harga : String,
    val deskripsi : String

):Parcelable
