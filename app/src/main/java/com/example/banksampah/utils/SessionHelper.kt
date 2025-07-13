package com.example.banksampah.utils

import android.content.Context
import com.example.banksampah.data.di.Injection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

fun getUserToken(context: Context): String? {
    val pref = Injection.provideUserPreference(context)
    val user = runBlocking { pref.getSession().first() }
    return if (user.isLogin) user.token else null
}
