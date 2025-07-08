package com.example.banksampah.data.remote.response

import com.google.gson.annotations.SerializedName


class PenjualanResponse (
	@field:SerializedName("data")
	val data: List<PenjualanResponseItem>
)

data class PenjualanResponseItem(
	@SerializedName("catatan_admin")
	val catatanAdmin: Any?,

	@SerializedName("harga")
	val harga: Int,

	@SerializedName("foto_url")
	val foto: String?,

	@SerializedName("updated_at")
	val updatedAt: String,

	@SerializedName("user_id")
	val userId: Int,

	@SerializedName("created_at")
	val createdAt: String,

	@SerializedName("nama_barang")
	val namaBarang: String,

	@SerializedName("id")
	val id: Int,

	@SerializedName("deskripsi")
	val deskripsi: String,

	@SerializedName("stok")
	val stok: Int,

	@SerializedName("status")
	val status: String
)
