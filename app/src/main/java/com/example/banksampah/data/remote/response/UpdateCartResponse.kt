package com.example.banksampah.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateCartResponse(

	@field:SerializedName("data")
	val data: UpdatedCartItem,

	@field:SerializedName("message")
	val message: String
)

data class UpdatedCartItem(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("penjualan_id")
	val penjualanId: Int,

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("penjualan")
	val penjualan: Penjualan
)

