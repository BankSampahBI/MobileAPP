package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.DataPenarikan
import java.text.NumberFormat

class ListRiwayatPenarikanAdapter :
    RecyclerView.Adapter<ListRiwayatPenarikanAdapter.ViewHolder>() {

    private val list = ArrayList<DataPenarikan>()

    fun setData(newList: List<DataPenarikan>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_riwayat_penarikan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(data)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvSaldo: TextView = view.findViewById(R.id.tv_saldo)
        private val tvTanggal: TextView = view.findViewById(R.id.tv_tanggal)

        fun bind(item: DataPenarikan) {
            tvSaldo.text = "Rp. ${NumberFormat.getInstance().format(item.jumlah)}"
            tvTanggal.text = item.createdAt.substring(0, 10) // ambil yyyy-MM-dd
        }
    }
}
