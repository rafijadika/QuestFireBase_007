package com.example.pertemuan14.repository

import com.example.pertemuan14.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NetworkRepositoryMhs(
    private val firestore: FirebaseFirestore
) : RepositoryMhs {
    override suspend fun insertMhs(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa").add(mahasiswa)
                .await() //Membuat Method untuk  Insert Data kedalam Collection Mahasiswa
        } catch (e: Exception) {
            throw Exception("Gagal menambahkan data mahasiswa : ${e.message}") //Tambahkan Debugging untuk excpetion error
        }
    }

    override fun getAllMhs(): Flow<List<Mahasiswa>> = callbackFlow {
        // Membuka collection dari Firestore
        val mhsCollection = firestore.collection("Mahasiswa")
            .orderBy("nim", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val mhsList = value.documents.mapNotNull {
                        // Convert dari Document Firestore kedalam data class
                        it.toObject(Mahasiswa::class.java)!!
                    }
                    // fungsi untuk mengirimkan collection ke database
                    trySend(mhsList)
                }
            }
        awaitClose {
            // Menutup Collection
            mhsCollection.remove()
        }
    }

    override fun getMhs(nim: String): Flow<Mahasiswa> = callbackFlow{
        val mhsDocument = firestore.collection("Mahasiswa") // Method untuk get Mahasiswa by nim
            .document(nim)
            .addSnapshotListener{ value, error ->
                if (value != null) {
                    val mhs = value.toObject(Mahasiswa::class.java)!!
                    trySend(mhs)
                }
            }
        awaitClose {
            mhsDocument.remove()
        }
    }

    override suspend fun deleteMhs(mahasiswa: Mahasiswa) {
        try {
            mahasiswa.nim?.let {
                firestore.collection("Mahasiswa") // Method untuk hapus mahasiswa
                    .document(it)
                    .delete()
                    .await()
            }
        } catch (e: Exception) {
            throw Exception("Gagal menghapus data mahasiswa: ${e.message}")
        }
    }

    override suspend fun updateMhs(mahasiswa: Mahasiswa) {
        try {
