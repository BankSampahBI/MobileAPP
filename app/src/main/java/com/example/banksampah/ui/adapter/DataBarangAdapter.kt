package com.example.banksampah.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.PenjualanKonsumenItem

class DataBarangAdapter(
    private val listBarang: ArrayList<PenjualanKonsumenItem>,
    private val onItemClick: (PenjualanKonsumenItem) -> Unit
) : RecyclerView.Adapter<DataBarangAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBarang: ImageView = itemView.findViewById(R.id.img_barang)
        val tvNamaBarang: TextView = itemView.findViewById(R.id.tv_nama_barang)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_tem_databarang, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listBarang.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val barang = listBarang[position]
        holder.tvNamaBarang.text = barang.namaBarang
        holder.tvHarga.text = "Rp ${barang.harga}"

        Glide.with(holder.itemView.context)
            .load(barang.foto)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imgBarang)

        // Pasang click listener
        holder.itemView.setOnClickListener {
            onItemClick(barang)
        }
    }

    fun setData(newData: List<PenjualanKonsumenItem>) {
        listBarang.clear()
        listBarang.addAll(newData)
        notifyDataSetChanged()
    }
}

