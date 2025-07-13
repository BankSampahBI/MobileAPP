package com.example.banksampah.data.remote.response

import com.google.gson.annotations.SerializedName

data class PenarikanResponse(

	@field:SerializedName("data")
	val data: DataPenarikan,

	@field:SerializedName("message")
	val message: String
)

data class DataPenarikan(

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("status")
	val status: String
)

data class RiwayatPenarikanResponse(
	@SerializedName("data")
	val data: List<DataPenarikan>
)