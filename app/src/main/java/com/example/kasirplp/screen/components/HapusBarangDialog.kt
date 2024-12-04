package com.example.kasirplp.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.kasirplp.models.Barang

@Composable
fun HapusBarangDialog(
    barangList: List<Barang>,
    onDeleteConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Hapus Barang") },
        text = {
            Column {
                Text("Apakah Anda yakin ingin menghapus barang berikut?")
                barangList.forEach { barang ->
                    Text("- ${barang.nama}")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDeleteConfirm() // Menjalankan logika hapus barang
                }
            ) {
                Text("Ya")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Tidak")
            }
        }
    )
}
