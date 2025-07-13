package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.PembelianDetailItem

class DetailRiwayatPembelianAdapter(private var list: List<PembelianDetailItem>) :
    RecyclerView.Adapter<DetailRiwayatPembelianAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFotoBarang: ImageView = itemView.findViewById(R.id.imgFotoBarang)
        val tvNamaBarang: TextView = itemView.findViewById(R.id.tvNamaBarang)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
    }

    fun submitList(data: List<PembelianDetailItem>) {
        list = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_detail_riwayat_pembelian, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tvNamaBarang.text = item.penjualan.namaBarang
        holder.tvJumlah.text = "Jumlah: ${item.jumlah}"
        holder.tvSubtotal.text = "Subtotal: Rp${item.subtotal}"

        val foto = item.penjualan.foto
        if (!foto.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(foto)
                .error(R.drawable.botol_kaca)
                .into(holder.imgFotoBarang)
        } else {
            holder.imgFotoBarang.setImageResource(R.drawable.botol_kaca)
        }
    }
}
