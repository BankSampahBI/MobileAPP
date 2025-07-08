package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.PenjualanResponseItem

class ListPenjualanAdapter(private val list: List<PenjualanResponseItem>) :
    RecyclerView.Adapter<ListPenjualanAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.tv_nama)
        val harga: TextView = view.findViewById(R.id.tv_harga)
        val status: TextView = view.findViewById(R.id.tv_status)
        val foto: ImageView = view.findViewById(R.id.img_barang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_penjualan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.nama.text = item.namaBarang
        holder.harga.text = "Rp ${item.harga}"
        holder.status.text = when (item.status) {
            "ditolak" -> "❌ Ditolak"
            "diterima" -> "✅ Diterima"
            else -> "⏳ Menunggu Validasi"
        }

        if (!item.foto.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.foto)
                .error(R.drawable.botol_kaca)        // fallback kalau gagal
                .into(holder.foto)
        } else {
            holder.foto.setImageResource(R.drawable.botol_kaca) // default image kalau kosong
        }
    }
}
