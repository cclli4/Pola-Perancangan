package com.example.kasirplp.factory

import com.example.kasirplp.models.Barang

class BarangFactory {

    // Fungsi untuk membuat barang berdasarkan nama dan harga
    fun createBarang(nama: String, harga: Double, kategori: String): Barang {
        return Barang(nama, harga, kategori)
    }
}