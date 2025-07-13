package com.example.banksampah.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banksampah.data.Result
import com.example.banksampah.databinding.FragmentHomeBinding
import com.example.banksampah.ui.adapter.ListBeritaAdapter
import com.example.banksampah.ui.adapter.ListKatalogAdapter
import com.example.banksampah.ui.model.ViewModelFactory
import com.example.banksampah.ui.notifikasi.NotifikasiActivity
import com.example.banksampah.ui.penarikan.PenarikanActivity
import com.example.banksampah.ui.riwayat.RiwayatPenarikanActivity
import java.text.NumberFormat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var katalogAdapter: ListKatalogAdapter
    private lateinit var beritaAdapter: ListBeritaAdapter

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Username
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvUsername.text = " ${user.name}"
            viewModel.getSaldo(user.token)
        }

        // Tampilkan saldo
        viewModel.saldo.observe(viewLifecycleOwner) { saldo ->
            val formatted = NumberFormat.getInstance().format(saldo)
            binding.textView10.text = "Rp. $formatted"
        }

        // Katalog
        katalogAdapter = ListKatalogAdapter()
        binding.rvKatalog.layoutManager = LinearLayoutManager(requireContext())
        binding.rvKatalog.adapter = katalogAdapter

        viewModel.getKatalog().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> katalogAdapter.submitList(result.data)
                is Result.Error -> Toast.makeText(requireContext(), "Gagal memuat katalog", Toast.LENGTH_SHORT).show()
            }
        }

        // Berita
        beritaAdapter = ListBeritaAdapter(arrayListOf())
        binding.rvBerita.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBerita.adapter = beritaAdapter

        viewModel.getBerita().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> beritaAdapter.setData(result.data)
                is Result.Error -> Toast.makeText(requireContext(), "Gagal memuat berita", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol
        binding.btnNotification.setOnClickListener {
            startActivity(Intent(requireContext(), NotifikasiActivity::class.java))
        }
        binding.btnTarik.setOnClickListener {
            startActivity(Intent(requireContext(), PenarikanActivity::class.java))
        }
        binding.btnRiwayat.setOnClickListener {
            startActivity(Intent(requireContext(), RiwayatPenarikanActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            viewModel.getSaldo(user.token)
        }
    }

}
