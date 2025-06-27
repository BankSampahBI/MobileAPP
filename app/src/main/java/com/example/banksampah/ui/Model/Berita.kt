package com.example.banksampah.ui.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Berita(
    val foto : Int,
    val judul : String,
    val isi : String

) :Parcelable
