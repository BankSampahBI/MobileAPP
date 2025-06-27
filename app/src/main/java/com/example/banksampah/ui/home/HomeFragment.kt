package com.example.banksampah.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.ui.Model.Berita
import com.example.banksampah.ui.Model.Edukasi
import com.example.banksampah.ui.adapter.ListBeritaAdapter
import com.example.banksampah.ui.adapter.ListEdukasiAdapter
import com.example.banksampah.ui.notifikasi.NotifikasiActivity
import com.example.banksampah.ui.penarikan.PenarikanActivity
import com.example.banksampah.ui.riwayat.RiwayatPenarikanActivity

class HomeFragment : Fragment() {

    private lateinit var rvEdukasi: RecyclerView
    private lateinit var rvBerita: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView Edukasi
        rvEdukasi = view.findViewById(R.id.rv_edukasi)
        rvEdukasi.setHasFixedSize(true)
        rvEdukasi.layoutManager = LinearLayoutManager(requireContext())

        // RecyclerView Berita
        rvBerita = view.findViewById(R.id.rv_berita)
        rvBerita.setHasFixedSize(true)
        rvBerita.layoutManager = LinearLayoutManager(requireContext())

        // Isi data Edukasi
        val edukasiList = getListEdukasiFromResources()
        val adapter = ListEdukasiAdapter(edukasiList)
        rvEdukasi.adapter = adapter

        // Isi data Berita
        val beritaList = getListBeritaFromResources()
        val adapterBerita = ListBeritaAdapter(beritaList)
        rvBerita.adapter = adapterBerita

        // Tombol navigasi
        view.findViewById<View>(R.id.btn_notification).setOnClickListener {
            startActivity(Intent(requireContext(), NotifikasiActivity::class.java))
        }

        view.findViewById<View>(R.id.btn_tarik).setOnClickListener {
            startActivity(Intent(requireContext(), PenarikanActivity::class.java))
        }

        view.findViewById<View>(R.id.btn_riwayat).setOnClickListener {
            startActivity(Intent(requireContext(), RiwayatPenarikanActivity::class.java))
        }
    }

    private fun getListEdukasiFromResources(): ArrayList<Edukasi> {
        val context = requireContext()
        val jenisArray = resources.getStringArray(R.array.jenis_sampah)
        val hargaArray = resources.getStringArray(R.array.harga)
        val deskripsiArray = resources.getStringArray(R.array.deskripsi_sampah)
        val gambarStringArray = resources.getStringArray(R.array.gambar_edukasi)

        val list = ArrayList<Edukasi>()
        for (i in jenisArray.indices) {
            val drawableName = gambarStringArray[i]
            val resId = resources.getIdentifier(drawableName, "drawable", context.packageName)
            list.add(Edukasi(resId, jenisArray[i], hargaArray[i], deskripsiArray[i]))
        }
        return list
    }

    private fun getListBeritaFromResources(): ArrayList<Berita> {
        val context = requireContext()
        val judulArray = resources.getStringArray(R.array.judul_berita)
        val isiArray = resources.getStringArray(R.array.isi_berita)
        val gambarArray = resources.getStringArray(R.array.foto_berita)

        val list = ArrayList<Berita>()
        for (i in judulArray.indices) {
            val drawableName = gambarArray[i]
            val resId = resources.getIdentifier(drawableName, "drawable", context.packageName)
            list.add(Berita(resId, judulArray[i], isiArray[i]))
        }
        return list
    }
}

