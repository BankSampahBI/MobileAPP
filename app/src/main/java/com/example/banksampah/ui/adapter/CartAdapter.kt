package com.example.banksampah.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.CartItem

class CartAdapter(
    private val listCart: List<CartItem>,
    private val onCheckedChange: (CartItem, Boolean) -> Unit,
    private val onItemClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private val selectedItems = mutableSetOf<CartItem>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBarang: ImageView = itemView.findViewById(R.id.imgBarang)
        val tvNamaBarang: TextView = itemView.findViewById(R.id.tvNamaBarang)
        val tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCart[position]
        val penjualan = item.penjualan

        holder.tvNamaBarang.text = penjualan.namaBarang
        holder.tvHarga.text = "Rp ${penjualan.harga}"
        holder.tvJumlah.text = "Jumlah: ${item.jumlah}"

        Glide.with(holder.itemView.context)
            .load(penjualan.foto)
            .into(holder.imgBarang)

        holder.checkbox.setOnCheckedChangeListener(null)
        holder.checkbox.isChecked = selectedItems.contains(item)
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedItems.add(item) else selectedItems.remove(item)
            onCheckedChange(item, isChecked)
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = listCart.size

    fun getSelectedItems(): List<CartItem> = selectedItems.toList()
}


