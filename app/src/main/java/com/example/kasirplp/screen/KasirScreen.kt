package com.example.kasirplp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kasirplp.R
import com.example.kasirplp.command.CetakStrukCommand
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.text.TextStyle
import com.example.kasirplp.command.TambahBarangCommand
import com.example.kasirplp.factory.BarangFactory
import com.example.kasirplp.helper.CurrencyUtils
import com.example.kasirplp.models.Barang
import com.example.kasirplp.observer.KasirObservable
import com.example.kasirplp.screen.components.CetakStrukDialog
import com.example.kasirplp.screen.components.ErrorDialog
import com.example.kasirplp.screen.components.HapusBarangDialog
import com.example.kasirplp.screen.components.TambahBarangDialog
import com.example.kasirplp.session.KasirSession

@Composable
fun KasirScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var strukContent by remember { mutableStateOf("") }
    val observable = remember { KasirObservable() }
    var transaksiTotal by remember { mutableStateOf(0.0) }
    var barangMap by remember { mutableStateOf(KasirSession.barangMap) }

    var isEditing by remember { mutableStateOf(false) }
    val selectedBarangToDelete = remember { mutableStateListOf<Barang>() }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var barangToDelete by remember { mutableStateOf<Barang?>(null) }

    val barangFactory = remember { BarangFactory() }
    var barangData by remember {
        mutableStateOf(
            listOf(
                barangFactory.createBarang("Buku", 5000.0, "Alat Tulis"),
                barangFactory.createBarang("Penggaris", 3000.0, "Alat Tulis"),
                barangFactory.createBarang("Skirt", 250000.0, "Pakaian"),
                barangFactory.createBarang("T-Shirt", 150000.0, "Pakaian")
            )
        )
    }

    var newBarangNama by remember { mutableStateOf("") }
    var newBarangHarga by remember { mutableStateOf("") }
    var newBarangKategori by remember { mutableStateOf("") }
    var showAddBarangDialog by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val observer = object : com.example.kasirplp.observer.Observer {
            override fun update() {
                barangMap = KasirSession.barangMap
                transaksiTotal = KasirSession.totalHarga
            }
        }
        observable.addObserver(observer)
        onDispose { observable.removeObserver(observer) }
    }

    val cetakStrukCommand = CetakStrukCommand(
        barangMap = barangMap,
        transaksiTotal = transaksiTotal
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Text(
                text = "Aplikasi Kasir",
                color = Color(0xFF9DB8F1),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFF6200EE))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Edit
            EditButton(isEditing = isEditing) {
                isEditing = !isEditing
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Barang List
        LazyColumn(modifier = Modifier.fillMaxHeight(0.4f)) {
            items(barangData) { barang ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isEditing) {
                            IconButton(
                                onClick = {
                                    if (selectedBarangToDelete.contains(barang)) {
                                        selectedBarangToDelete.remove(barang)
                                    } else {
                                        selectedBarangToDelete.add(barang)
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (selectedBarangToDelete.contains(barang)) R.drawable.ic_check_circle else R.drawable.ic_circle
                                    ),
                                    contentDescription = "Select Barang"
                                )
                            }
                        }
                        Column {
                            Text(
                                text = barang.nama,
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = CurrencyUtils.formatRupiah(barang.harga),
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                val currentQty = KasirSession.barangMap[barang] ?: 0
                                if (currentQty > 1) {
                                    // Kurangi jumlah barang
                                    KasirSession.barangMap[barang] = currentQty - 1
                                    KasirSession.totalHarga -= barang.harga
                                    observable.notifyObservers()
                                } else if (currentQty == 1) {
                                    // Tampilkan konfirmasi penghapusan
                                    barangToDelete = barang
                                    showDeleteConfirmationDialog = true
                                    // Kurangi jumlah barang menjadi 0 sebelum menampilkan konfirmasi
                                    KasirSession.barangMap[barang] = 0
                                    KasirSession.totalHarga -= barang.harga
                                    observable.notifyObservers()
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_remove),
                                contentDescription = "Kurangi Barang"
                            )
                        }

                        Text(
                            text = "${KasirSession.barangMap[barang] ?: 0}",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = {
                                // Panggil Tambah barang command
                                val tambahBarangCommand = TambahBarangCommand(barang)
                                tambahBarangCommand.execute()

                                observable.notifyObservers()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "Tambah Barang"
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for Adding and Deleting Barang
        if (isEditing && selectedBarangToDelete.isNotEmpty()) {
            Button(
                onClick = {
                    if (selectedBarangToDelete.isNotEmpty()){
                        barangToDelete = selectedBarangToDelete.first()
                        showDeleteConfirmationDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Hapus Barang yang Dipilih")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showAddBarangDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Barang Baru")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total Harga
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total Harga:",
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            Text(text = CurrencyUtils.formatRupiah(transaksiTotal))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Barang dalam Transaksi
        Text(
            text = "Barang dalam Transaksi",
            style = TextStyle(fontWeight = FontWeight.Medium)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.fillMaxWidth()) {

            barangMap.filter { it.value > 0 }.forEach { (barang, quantity) ->
                Text(text = "$quantity x ${barang.nama} - ${CurrencyUtils.formatRupiah(barang.harga * quantity)}")
            }
        }


        // Bottom Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    KasirSession.resetTransaksi()
                    observable.notifyObservers()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Reset Transaksi")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (barangMap.isEmpty()) {
                        errorMessage = "Tidak ada barang yang ingin dicetak."
                        showErrorDialog = true
                    } else {
                        strukContent = cetakStrukCommand.execute()
                        showDialog = true
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cetak Struk")
            }
        }

        // Cetak struk dialog
        if (showDialog) {
            CetakStrukDialog(
                strukContent = strukContent,
                onDismiss = { showDialog = false }
            )
        }

        //Tambah barang dialog
        if (showAddBarangDialog) {
            TambahBarangDialog(
                newBarangNama = newBarangNama,
                newBarangHarga = newBarangHarga,
                newBarangKategori = newBarangKategori,
                onNamaChange = { newBarangNama = it },
                onHargaChange = { newBarangHarga = it },
                onKategoriChange = { newBarangKategori = it },
                onBarangAdded = { barangBaru ->
                    // Tambahkan barang ke barangData
                    barangData = barangData.toMutableList().apply { add(barangBaru) }

                    // Reset input setelah menambahkan barang
                    newBarangNama = ""
                    newBarangHarga = ""
                    newBarangKategori = ""

                    showAddBarangDialog = false
                },
                onDismiss = { showAddBarangDialog = false },
                onError = { message ->
                    errorMessage = message
                    showErrorDialog = true
                }
            )
        }

        if (showDeleteConfirmationDialog && selectedBarangToDelete.isNotEmpty()) {
            HapusBarangDialog(
                barangList = selectedBarangToDelete,
                onDeleteConfirm = {
                    selectedBarangToDelete.forEach{barang ->
                        KasirSession.totalHarga -= barang.harga * (KasirSession.barangMap[barang] ?: 0)
                        KasirSession.barangMap.remove(barang)
                    }

                    barangData = barangData.filterNot { it in selectedBarangToDelete }

                    selectedBarangToDelete.clear()

                    showDeleteConfirmationDialog = false
                    observable.notifyObservers()
                },

                onDismiss = {
                    showDeleteConfirmationDialog = false
                }
            )
        }

        //Menampilkan pesan kesalahan
        if (showErrorDialog) {
            ErrorDialog(
                errorMessage = errorMessage,
                onDismiss = {showErrorDialog = false}
            )
        }
    }
}

@Composable
fun EditButton(isEditing: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(onClick = onClick) {
            Text(if (isEditing) "Batal Edit" else "Edit")
        }
    }
}

