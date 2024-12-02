package com.example.kasirplp.template

abstract class TransaksiKasir {
    fun prosesTransaksi() {
        pilihBarang()
        hitungTotal()
        cetakStruk()
    }

    abstract fun pilihBarang()
    abstract fun hitungTotal()
    abstract fun cetakStruk()
}