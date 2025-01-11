package com.example.pertemuan14

import android.app.Application
import com.example.pertemuan14.di.MahasiswaContainer
import com.google.firebase.FirebaseApp

class MahasiswaApp : Application() {
    lateinit var containerApp: MahasiswaContainer

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        containerApp = MahasiswaContainer(this)
    }
}