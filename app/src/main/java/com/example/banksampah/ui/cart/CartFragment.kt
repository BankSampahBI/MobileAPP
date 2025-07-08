package com.example.banksampah.ui.cart

import android.content.Intent
import android.os.Bundle
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
                        is Result.Loading -> { /* tampilkan loading */ }
                        is Result.Success -> {
                            val list = result.data
                            if (list.isNotEmpty()) {
                                binding.tvEmpty.visibility = View.GONE

                                // FIX: set layout manager terlebih dulu!
                                binding.rvPenjualan.layoutManager = LinearLayoutManager(requireContext())
                                binding.rvPenjualan.adapter = ListPenjualanAdapter(list)
                            } else {
                                binding.tvEmpty.visibility = View.VISIBLE
                            }
                        }
                        is Result.Error -> {
                            binding.tvEmpty.text = "Gagal memuat data"
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
