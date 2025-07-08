package com.example.banksampah.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.banksampah.data.Repository
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.BeritaItem
import com.example.banksampah.data.remote.response.DataItem
import com.example.banksampah.ui.model.UserModel

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun getKatalog(): LiveData<Result<List<DataItem>>> {
        return repository.getKatalog()
    }
    fun getBerita(): LiveData<Result<List<BeritaItem>>> {
        return repository.getBerita()
    }

}