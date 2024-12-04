package com.example.kasirplp.command

import com.example.kasirplp.helper.CurrencyUtils
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
            strBuilder.append("${qty} x ${barang.nama} - Rp ${CurrencyUtils.formatRupiah(totalPerBarang)}\n")
        }

        strBuilder.append("===================\n")
        strBuilder.append("Total Harga: ${CurrencyUtils.formatRupiah(transaksiTotal)}\n")

        return strBuilder.toString()
    }
}