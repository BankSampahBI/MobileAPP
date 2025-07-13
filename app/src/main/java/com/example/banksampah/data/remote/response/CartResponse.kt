package com.example.banksampah.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class CartResponse(

	@field:SerializedName("data")
	val data: List<CartItem>,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class CartItem(

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("penjualan")
	val penjualan: Penjualan,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("penjualan_id")
	val penjualanId: Int
): Parcelable

@Parcelize
data class Penjualan(

	@field:SerializedName("catatan_admin")
	val catatanAdmin: @RawValue Any? = null,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("nama_barang")
	val namaBarang: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("stok")
	val stok: Int,

	@field:SerializedName("status")
	val status: String

) : Parcelable
