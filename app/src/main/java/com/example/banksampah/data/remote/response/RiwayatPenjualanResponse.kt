package com.example.banksampah.data.remote.response

import com.google.gson.annotations.SerializedName

data class RiwayatPenjualanResponse(

	@field:SerializedName("data")
	val data: List<DataItemRiwayatPenjualan>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataItemRiwayatPenjualan(

	@field:SerializedName("tanggal_pembelian")
	val tanggalPembelian: String,

	@field:SerializedName("foto_url")
	val fotoUrl: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("subtotal")
	val subtotal: Int,

	@field:SerializedName("nama_barang")
	val namaBarang: String,

	@field:SerializedName("penjualan_id")
	val penjualanId: Int
)
