package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.ui.Model.RiwayatPenarikan
import com.example.banksampah.ui.Model.RiwayatPenyetoran

class ListRiwayatPenyetoranAdapter  (private val listRiwayatPenyetoran: ArrayList<RiwayatPenyetoran>) : RecyclerView.Adapter<ListRiwayatPenyetoranAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_riwayat_penyetoran, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (jenis,berat,tanggal,harga) = listRiwayatPenyetoran[position]
        holder.tvJenis.text = jenis
        holder.tvBerat.text = berat
        holder.tvTanggal.text = tanggal
        holder.tvHarga.text = harga

    }

    override fun getItemCount(): Int = listRiwayatPenyetoran.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJenis: TextView = itemView.findViewById(R.id.tv_jenisSampah)
        val tvBerat: TextView = itemView.findViewById(R.id.tv_berat)
        val tvTanggal: TextView = itemView.findViewById(R.id.tv_tanggal)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga)

    }
}