package com.example.banksampah.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banksampah.data.Result
import com.example.banksampah.data.di.Injection
import com.example.banksampah.databinding.FragmentCartBinding
import com.example.banksampah.ui.adapter.ListPenjualanAdapter
import com.example.banksampah.ui.add.AddPenjualanActivity
import com.example.banksampah.ui.model.ViewModelFactory
import com.example.banksampah.data.remote.response.PenjualanResponseItem

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabAddPenjualan.setOnClickListener {
            val intent = Intent(requireContext(), AddPenjualanActivity::class.java)
            startActivity(intent)
        }

        // Observe session
        Injection.provideUserPreference(requireContext()).getSession().asLiveData()
            .observe(viewLifecycleOwner) { user ->
                val repository = Injection.provideRepository(requireContext(), user.token)
                cartViewModel = ViewModelProvider(
                    this,
                    ViewModelFactory(repository)
                )[CartViewModel::class.java]

                // tampilkan nama user
                binding.tvUsername.text = user.name

                // load penjualan
                cartViewModel.loadPenjualan(user.token)

                // observe penjualan
                cartViewModel.penjualan.observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            // tampilkan loading jika perlu
                        }
                        is Result.Success -> {
                            val list = result.data

                            // Debug status
                            list.forEach {
                                Log.d("StatusCheck", "Item: ${it.namaBarang}, status: ${it.status}")
                            }

                            // Urutkan: diterima (0), menunggu (1), ditolak (2), lain (3)
                            val sortedList = list.sortedWith(compareBy<PenjualanResponseItem> {
                                when (it.status.lowercase()) {
                                    "diterima", "ditampilkan" -> 0
                                    "menunggu", "menunggu_validasi" -> 1
                                    "ditolak" -> 2
                                    else -> 3
                                }
                            }.thenByDescending { it.createdAt })

                            if (sortedList.isNotEmpty()) {
                                binding.tvEmpty.visibility = View.GONE
                                binding.rvPenjualan.layoutManager = LinearLayoutManager(requireContext())
                                binding.rvPenjualan.adapter = ListPenjualanAdapter(sortedList)
                            } else {
                                binding.tvEmpty.visibility = View.VISIBLE
                            }
                        }
                        is Result.Error -> {
                            binding.tvEmpty.text = "Gagal Memuat Data"
                            binding.tvEmpty.visibility = View.VISIBLE
                        }
                    }
                }
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
