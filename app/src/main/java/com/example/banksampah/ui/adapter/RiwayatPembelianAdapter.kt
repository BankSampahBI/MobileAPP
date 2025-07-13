package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.DataItemRiwayat

class RiwayatPembelianAdapter(
    private var list: List<DataItemRiwayat>,
    private val onItemClick: (DataItemRiwayat) -> Unit
) : RecyclerView.Adapter<RiwayatPembelianAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvNamaBarang: TextView = itemView.findViewById(R.id.tvNamaBarang)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val imgFotoBarang: ImageView = itemView.findViewById(R.id.imgFotoBarang)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(list[position])
                }
            }
        }
    }

    fun submitList(data: List<DataItemRiwayat>) {
        list = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_riwayat_pembelian, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tvTanggal.text = item.createdAt.substring(0, 10)
        holder.tvTotal.text = "Rp${item.totalHarga}"
        holder.tvStatus.text = item.status

        val namaBarang = item.pembelianDetail.joinToString(", ") {
            it.penjualan.namaBarang
        }
        holder.tvNamaBarang.text = namaBarang

        val foto = item.pembelianDetail.firstOrNull()?.penjualan?.foto

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
