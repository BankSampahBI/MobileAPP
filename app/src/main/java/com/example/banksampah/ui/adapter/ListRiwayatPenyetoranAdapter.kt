package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.DetailsItem

class ListRiwayatPenyetoranAdapter :
    RecyclerView.Adapter<ListRiwayatPenyetoranAdapter.ViewHolder>() {

    private var listData = listOf<Triple<DetailsItem, String, Int>>() // detail, tanggal, saldo

    fun setData(newList: List<Triple<DetailsItem, String, Int>>) {
        listData = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJenis: TextView = itemView.findViewById(R.id.tv_jenisSampah)
        val tvBerat: TextView = itemView.findViewById(R.id.tv_berat)
        val tvTanggal: TextView = itemView.findViewById(R.id.tv_tanggal)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_riwayat_penyetoran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (detail, tanggal, _) = listData[position]
        holder.tvJenis.text = detail.namaSampah
        holder.tvBerat.text = "${detail.berat} kg"
        holder.tvTanggal.text = tanggal
        holder.tvHarga.text = "Rp${detail.subtotal}"
    }

    override fun getItemCount(): Int = listData.size
}
