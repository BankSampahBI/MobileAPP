package com.example.banksampah.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banksampah.data.remote.response.CartItem
import com.example.banksampah.databinding.FragmentKonsumenCartBinding
import com.example.banksampah.ui.adapter.CartAdapter
import com.example.banksampah.ui.checkout.CheckoutActivity
import com.example.banksampah.ui.detail.DetailCartActivity // Ubah ke DetailCartActivity
import com.example.banksampah.ui.model.ViewModelFactory
import com.example.banksampah.ui.riwayat.RiwayatPembelianActivity

class KonsumenCartFragment : Fragment() {

    private var _binding: FragmentKonsumenCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: KonsumenCartViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var cartAdapter: CartAdapter
    private val selectedItems = mutableListOf<CartItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKonsumenCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRiwayat.setOnClickListener {
            val intent = Intent(requireContext(), RiwayatPembelianActivity::class.java)
            startActivity(intent)
        }


        cartAdapter = CartAdapter(
            listCart = listOf(),
            onCheckedChange = { item, isChecked ->
                if (isChecked) selectedItems.add(item) else selectedItems.remove(item)
                updateTotal()
            },
            onItemClick = { item ->
                val intent = Intent(requireContext(), DetailCartActivity::class.java) // ⬅️ ganti activity tujuan
                intent.putExtra(DetailCartActivity.EXTRA_BARANG_CART, item) // ⬅️ gunakan key EXTRA_BARANG_CART
                startActivity(intent)
            }
        )

        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = cartAdapter

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            viewModel.getCart(user.token)
            binding.tvUsername.text = user.name

            binding.btnCheckout.setOnClickListener {
                if (selectedItems.isEmpty()) {
                    Toast.makeText(requireContext(), "Pilih barang terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(requireContext(), CheckoutActivity::class.java)
                    intent.putParcelableArrayListExtra("selected_items", ArrayList(selectedItems))
                    startActivity(intent)
                }
            }
        }

        viewModel.cartItems.observe(viewLifecycleOwner) { list ->
            selectedItems.clear()
            cartAdapter = CartAdapter(list, { item, isChecked ->
                if (isChecked) selectedItems.add(item) else selectedItems.remove(item)
                updateTotal()
            }, { item ->
                val intent = Intent(requireContext(), DetailCartActivity::class.java) // ⬅️ detail cart
                intent.putExtra(DetailCartActivity.EXTRA_BARANG_CART, item)
                startActivity(intent)
            })
            binding.rvCart.adapter = cartAdapter
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTotal() {
        val total = selectedItems.sumOf { it.penjualan.harga * it.jumlah }
        binding.tvTotal.text = "Total: Rp $total"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
