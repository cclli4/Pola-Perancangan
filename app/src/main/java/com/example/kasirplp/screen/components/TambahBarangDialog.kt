package com.example.kasirplp.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kasirplp.models.Barang

@Composable
fun TambahBarangDialog(
    newBarangNama: String,
    newBarangHarga: String,
    newBarangKategori: String,
    onNamaChange: (String) -> Unit,
    onHargaChange: (String) -> Unit,
    onKategoriChange: (String) -> Unit,
    onBarangAdded: (Barang) -> Unit,
    onDismiss: () -> Unit,
    onError: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Tambah Barang Baru") },
        text = {
            Column {
                TextField(
                    value = newBarangNama,
                    onValueChange = onNamaChange,
                    label = { Text("Nama Barang") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newBarangHarga,
                    onValueChange = onHargaChange,
                    label = { Text("Harga Barang") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newBarangKategori,
                    onValueChange = onKategoriChange,
                    label = { Text("Kategori Barang") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (newBarangNama.isNotBlank() && newBarangHarga.isNotBlank() && newBarangKategori.isNotBlank()) {
                    try {
                        val harga = newBarangHarga.toDouble()
                        val barangBaru = Barang(newBarangNama, harga, newBarangKategori) // Sesuaikan dengan struktur data Barang
                        onBarangAdded(barangBaru)
                    } catch (e: NumberFormatException) {
                        onError("Harga harus berupa angka.")
                    }
                } else {
                    onError("Semua field harus diisi.")
                }
            }) {
                Text("Tambah")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
