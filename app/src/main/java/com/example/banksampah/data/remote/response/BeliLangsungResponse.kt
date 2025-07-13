package com.example.banksampah.data.remote.response

import com.google.gson.annotations.SerializedName

data class BeliLangsungResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("pembelian_id")
	val pembelianId: Int
)
