package com.example.kasirplp.command

import com.example.kasirplp.models.Barang
import com.example.kasirplp.session.KasirSession

class TambahBarangCommand(private val barang: Barang) {
    fun execute() {
        if (KasirSession.barangMap.containsKey(barang)) {
            KasirSession.barangMap[barang] = KasirSession.barangMap[barang]!! + 1
        } else {
            KasirSession.barangMap[barang] = 1
        }
        KasirSession.totalHarga += barang.harga
    }
}