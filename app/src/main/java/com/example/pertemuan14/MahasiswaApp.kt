package com.example.pertemuan14

import android.app.Application
import com.example.pertemuan14.di.AppContainer
import com.example.pertemuan14.di.MahasiswaContainer

class MahasiswaApplications : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = MahasiswaContainer()
    }
}