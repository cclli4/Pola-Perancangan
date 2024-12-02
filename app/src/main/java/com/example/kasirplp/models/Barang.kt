package com.example.kasirplp.models

data class Barang (
    val nama: String,
    val harga: Double,
    val kategori: String,
    var quantity: Int = 1
)