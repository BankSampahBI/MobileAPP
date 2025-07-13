package com.example.banksampah.data.remote.response

import com.google.gson.annotations.SerializedName

data class SaldoResponse(

	@field:SerializedName("saldo")
	val saldo: Int
)
