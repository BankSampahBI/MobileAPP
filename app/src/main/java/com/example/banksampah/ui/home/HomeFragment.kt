// HomeFragment.kt
package com.example.banksampah.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.data.Result
import com.example.banksampah.ui.adapter.ListBeritaAdapter
import com.example.banksampah.ui.adapter.ListKatalogAdapter
import com.example.banksampah.ui.model.Berita
import com.example.banksampah.ui.model.ViewModelFactory
import com.example.banksampah.ui.notifikasi.NotifikasiActivity
import com.example.banksampah.ui.penarikan.PenarikanActivity
import com.example.banksampah.ui.riwayat.RiwayatPenarikanActivity


class HomeFragment : Fragment() {

    private lateinit var rvKatalog: RecyclerView
    private lateinit var rvBerita: RecyclerView
    private lateinit var katalogAdapter: ListKatalogAdapter

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val tvUsername: TextView = view.findViewById(R.id.tv_username)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            tvUsername.text = " ${user.name}"
        }

        rvKatalog = view.findViewById(R.id.rv_katalog)
        rvKatalog.layoutManager = LinearLayoutManager(requireContext())
        katalogAdapter = ListKatalogAdapter()
        rvKatalog.adapter = katalogAdapter

        viewModel.getKatalog().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> katalogAdapter.submitList(result.data)
                is Result.Error -> Toast.makeText(requireContext(), "Gagal memuat katalog", Toast.LENGTH_SHORT).show()
            }
        }

        rvBerita = view.findViewById(R.id.rv_berita)
        rvBerita.layoutManager = LinearLayoutManager(requireContext())
        rvBerita.adapter = ListBeritaAdapter(getListBeritaFromResources())

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

    private fun getListBeritaFromResources(): ArrayList<Berita> {
        val context = requireContext()
        val judulArray = resources.getStringArray(R.array.judul_berita)
        val isiArray = resources.getStringArray(R.array.isi_berita)
        val gambarArray = resources.getStringArray(R.array.foto_berita)
        return ArrayList(judulArray.indices.map { i ->
            val resId = resources.getIdentifier(gambarArray[i], "drawable", context.packageName)
            Berita(resId, judulArray[i], isiArray[i])
        })
    }
}
