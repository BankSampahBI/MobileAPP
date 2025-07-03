// ListKatalogAdapter.kt
package com.example.banksampah.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.DataItem
import com.example.banksampah.ui.detail.DetailKatalogActivity

class ListKatalogAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var fullList: List<DataItem> = listOf()
    private var showAll = false
    private val PREVIEW_COUNT = 3
    private val TYPE_ITEM = 0
    private val TYPE_VIEW_MORE = 1

    fun submitList(list: List<DataItem>) {
        fullList = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (!showAll && position == minOf(PREVIEW_COUNT, fullList.size)) {
            TYPE_VIEW_MORE
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_edukasi, parent, false)
            KatalogViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_more, parent, false)
            ViewMoreViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if (showAll || fullList.size <= PREVIEW_COUNT) {
            fullList.size
        } else {
            PREVIEW_COUNT + 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is KatalogViewHolder) {
            val item = fullList[position]
            Glide.with(holder.itemView.context)
                .load(item.foto)
                .into(holder.ivSampah)
            holder.tvJenis.text = item.nama
            holder.tvHarga.text = "Rp. ${item.harga}"

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, DetailKatalogActivity::class.java)
                intent.putExtra("extra_katalog", item)
                context.startActivity(intent)
            }
        } else if (holder is ViewMoreViewHolder) {
            holder.tvViewMore.setOnClickListener {
                showAll = true
                notifyDataSetChanged()
            }
        }

    }

    class KatalogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSampah: ImageView = itemView.findViewById(R.id.iv_sampah)
        val tvJenis: TextView = itemView.findViewById(R.id.tv_jenisSampah)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga)
    }

    class ViewMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvViewMore: TextView = itemView.findViewById(R.id.tv_view_more)
    }
}
