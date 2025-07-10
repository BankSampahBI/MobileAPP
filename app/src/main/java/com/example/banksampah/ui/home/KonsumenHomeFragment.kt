package com.example.banksampah.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.PenjualanKonsumenItem
import com.example.banksampah.ui.adapter.DataBarangAdapter
import com.example.banksampah.ui.detail.DetailBarangActivity
import com.example.banksampah.ui.model.ViewModelFactory

class KonsumenHomeFragment : Fragment() {

    private lateinit var rvDataBarang: RecyclerView
    private lateinit var adapter: DataBarangAdapter

    private val viewModel: KonsumenHomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_konsumen_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvUsername: TextView = view.findViewById(R.id.tv_username)

        rvDataBarang = view.findViewById(R.id.rv_databarang)
        rvDataBarang.layoutManager = LinearLayoutManager(requireContext())

        adapter = DataBarangAdapter(arrayListOf()) { selectedBarang: PenjualanKonsumenItem ->
            val intent = Intent(requireContext(), DetailBarangActivity::class.java)
            intent.putExtra("extra_barang", selectedBarang)
            startActivity(intent)
        }
        rvDataBarang.adapter = adapter

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            tvUsername.text = " ${user.name}"
            viewModel.getDataBarang("Bearer ${user.token}")
        }

        viewModel.barangList.observe(viewLifecycleOwner) { list ->
            adapter.setData(list)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            // Optional: tampilkan progress bar / loading indicator jika perlu
        }
    }
}
