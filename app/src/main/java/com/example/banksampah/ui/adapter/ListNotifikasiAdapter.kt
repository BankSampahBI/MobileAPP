package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.ui.model.Notifikasi

class ListNotifikasiAdapter(private val listNotikasi: ArrayList<Notifikasi>) : RecyclerView.Adapter<ListNotifikasiAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_notifikasi, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (message,saldo) = listNotikasi[position]
        holder.tvMessage.text = message
        holder.tvSaldo.text = saldo
    }

    override fun getItemCount(): Int = listNotikasi.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tv_pesan)
        val tvSaldo: TextView = itemView.findViewById(R.id.tv_saldo)

    }
}