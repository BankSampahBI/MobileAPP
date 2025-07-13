package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banksampah.data.remote.response.DataItemRiwayatPenjualan
import com.example.banksampah.databinding.ListItemRiwayatPenjualanBinding

class RiwayatPenjualanAdapter :
    ListAdapter<DataItemRiwayatPenjualan, RiwayatPenjualanAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ListItemRiwayatPenjualanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemRiwayatPenjualan) {
            binding.tvNamaBarang.text = item.namaBarang
            binding.tvJumlah.text = "${item.jumlah} x Rp${item.harga}"
            binding.tvSubtotal.text = "Rp${item.subtotal}"
            binding.tvTanggal.text = item.tanggalPembelian

            Glide.with(binding.root.context)
                .load(item.fotoUrl)
                .into(binding.ivBarang)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemRiwayatPenjualanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemRiwayatPenjualan>() {
            override fun areItemsTheSame(
                oldItem: DataItemRiwayatPenjualan,
                newItem: DataItemRiwayatPenjualan
            ) = oldItem.penjualanId == newItem.penjualanId &&
                    oldItem.tanggalPembelian == newItem.tanggalPembelian

            override fun areContentsTheSame(
                oldItem: DataItemRiwayatPenjualan,
                newItem: DataItemRiwayatPenjualan
            ) = oldItem == newItem
        }
    }
}