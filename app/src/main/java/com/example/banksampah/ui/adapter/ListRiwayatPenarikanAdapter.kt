package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.ui.model.RiwayatPenarikan

class ListRiwayatPenarikanAdapter (private val listRiwayatPenarikan: ArrayList<RiwayatPenarikan>) : RecyclerView.Adapter<ListRiwayatPenarikanAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_riwayat_penarikan, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (jumlah,tanggal) = listRiwayatPenarikan[position]
        holder.tvJumlah.text = jumlah
        holder.tvTanggal.text = tanggal
    }

    override fun getItemCount(): Int = listRiwayatPenarikan.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJumlah: TextView = itemView.findViewById(R.id.tv_saldo)
        val tvTanggal: TextView = itemView.findViewById(R.id.tv_tanggal)

    }
}