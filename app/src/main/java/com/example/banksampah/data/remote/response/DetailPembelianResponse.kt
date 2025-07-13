package com.example.banksampah.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailPembelianResponse(

	@field:SerializedName("data")
	val data: DataItemRiwayat,

	@field:SerializedName("message")
	val message: String
)

//data class User(
//
//	@field:SerializedName("role")
//	val role: String,
//
//	@field:SerializedName("no_hp")
//	val noHp: Any,
//
//	@field:SerializedName("foto")
//	val foto: Any,
//
//	@field:SerializedName("updated_at")
//	val updatedAt: String,
//
//	@field:SerializedName("name")
//	val name: String,
//
//	@field:SerializedName("created_at")
//	val createdAt: String,
//
//	@field:SerializedName("email_verified_at")
//	val emailVerifiedAt: Any,
//
//	@field:SerializedName("id")
//	val id: Int,
//
//	@field:SerializedName("email")
//	val email: String,
//
//	@field:SerializedName("alamat")
//	val alamat: Any
//)

data class DetailItem(

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("subtotal")
	val subtotal: Int,

	@field:SerializedName("penjualan")
	val penjualan: Penjualan,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("pembelian_id")
	val pembelianId: Int,

	@field:SerializedName("penjualan_id")
	val penjualanId: Int
)

//data class Penjualan(
//
//	@field:SerializedName("catatan_admin")
//	val catatanAdmin: Any,
//
//	@field:SerializedName("harga")
//	val harga: Int,
//
//	@field:SerializedName("foto")
//	val foto: String,
//
//	@field:SerializedName("updated_at")
//	val updatedAt: String,
//
//	@field:SerializedName("user_id")
//	val userId: Int,
//
//	@field:SerializedName("created_at")
//	val createdAt: String,
//
//	@field:SerializedName("nama_barang")
//	val namaBarang: String,
//
//	@field:SerializedName("id")
//	val id: Int,
//
//	@field:SerializedName("deskripsi")
//	val deskripsi: String,
//
//	@field:SerializedName("stok")
//	val stok: Int,
//
//	@field:SerializedName("user")
//	val user: User,
//
//	@field:SerializedName("status")
//	val status: String
//)

//data class Data(
//
//	@field:SerializedName("updated_at")
//	val updatedAt: String,
//
//	@field:SerializedName("user_id")
//	val userId: Int,
//
//	@field:SerializedName("pembelian_detail")
//	val pembelianDetail: List<PembelianDetailItem>,
//
//	@field:SerializedName("created_at")
//	val createdAt: String,
//
//	@field:SerializedName("total_harga")
//	val totalHarga: Int,
//
//	@field:SerializedName("id")
//	val id: Int,
//
//	@field:SerializedName("status")
//	val status: String
//)
