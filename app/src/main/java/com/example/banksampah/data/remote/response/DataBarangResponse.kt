package com.example.banksampah.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class DataBarangResponse(
	@SerializedName("data")
	val data: List<PenjualanKonsumenItem>,

	@SerializedName("message")
	val message: String
)

@Parcelize
data class PenjualanKonsumenItem(

	@SerializedName("catatan_admin")
	val catatanAdmin: @RawValue Any?,

	@SerializedName("harga")
	val harga: Int,

	@SerializedName("foto")
	val foto: String,

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

	@SerializedName("user")
	val user: @RawValue Any?,

	@SerializedName("status")
	val status: String
):Parcelable

data class KonsumenUser(

	@SerializedName("role")
	val role: String,

	@SerializedName("no_hp")
	val noHp: Any?,

	@SerializedName("foto")
	val foto: Any?,

	@SerializedName("updated_at")
	val updatedAt: String,

	@SerializedName("name")
	val name: String,

	@SerializedName("created_at")
	val createdAt: String,

	@SerializedName("email_verified_at")
	val emailVerifiedAt: Any?,

	@SerializedName("id")
	val id: Int,

	@SerializedName("email")
	val email: String,

	@SerializedName("alamat")
	val alamat: Any?
)
