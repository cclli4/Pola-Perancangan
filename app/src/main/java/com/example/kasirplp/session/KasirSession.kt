package com.example.kasirplp.session

import com.example.kasirplp.models.Barang

object KasirSession {
    var barangMap: MutableMap<Barang, Int> = mutableMapOf()
    var totalHarga: Double = 0.0

    fun resetTransaksi() {
        barangMap.clear()
        totalHarga = 0.0
    }
}