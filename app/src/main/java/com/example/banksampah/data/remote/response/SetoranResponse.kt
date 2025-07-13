package com.example.banksampah.data.remote.response

import com.google.gson.annotations.SerializedName

data class SetoranResponse(

	@field:SerializedName("data")
	val data: List<Data>,

	@field:SerializedName("message")
	val message: String


)

data class DetailsItem(

	@field:SerializedName("transaksi_id")
	val transaksiId: Int,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("nama_sampah")
	val namaSampah: String,

	@field:SerializedName("berat")
	val berat: Any,

	@field:SerializedName("subtotal")
	val subtotal: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
)

data class Data(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("details")
	val details: List<DetailsItem>,

	@field:SerializedName("tanggal")
	val tanggal: String,

	@field:SerializedName("saldo")
	val saldo: Int,

	@field:SerializedName("id")
	val id: Int
)
