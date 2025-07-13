package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.CartItem

class CheckoutAdapter(
    private var items: List<CartItem>
) : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaBarang)
        val tvJumlah: TextView = view.findViewById(R.id.tvJumlah)
        val tvSubtotal: TextView = view.findViewById(R.id.tvSubtotal)
        val imgBarang: ImageView = view.findViewById(R.id.img_barang_checkout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_checkout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvNama.text = item.penjualan.namaBarang
        holder.tvJumlah.text = "Jumlah: ${item.jumlah}"
        holder.tvSubtotal.text = "Subtotal: Rp${item.jumlah * item.penjualan.harga}"

        Glide.with(holder.itemView.context)
            .load(item.penjualan.foto)
            .into(holder.imgBarang)
    }

    fun updateData(newItems: List<CartItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}
