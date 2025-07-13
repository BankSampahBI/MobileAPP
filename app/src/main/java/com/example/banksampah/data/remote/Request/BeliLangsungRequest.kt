package com.example.banksampah.data.remote.Request

import com.google.gson.annotations.SerializedName

data class BeliLangsungRequest(
    @SerializedName("penjualan_id")
    val penjualanId: Int,

    @SerializedName("jumlah")
    val jumlah: Int
)