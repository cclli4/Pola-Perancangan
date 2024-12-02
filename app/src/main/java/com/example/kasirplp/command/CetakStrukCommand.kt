package com.example.kasirplp.command

import com.example.kasirplp.models.Barang

class CetakStrukCommand(
    private val barangMap: Map<Barang, Int>,
    private val transaksiTotal: Double
) {
    fun execute(): String {
        val strBuilder = StringBuilder()
        strBuilder.append("===================\n")

        barangMap.forEach { (barang, qty) ->
            val totalPerBarang = qty * barang.harga
            strBuilder.append("${qty}x ${barang.nama} - Rp ${totalPerBarang}\n")
        }

        strBuilder.append("===================\n")
        strBuilder.append("Total Harga: Rp $transaksiTotal\n")

        return strBuilder.toString()
    }
}

