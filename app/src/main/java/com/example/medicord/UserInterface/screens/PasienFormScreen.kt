package com.example.medicord.UserInterface.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.medicord.Data.database.AppDatabase
import com.example.medicord.Data.repository.PasienRepository
import com.example.medicord.navigation.Screen
import com.example.medicord.utils.ImagePicker
import com.example.medicord.utils.rememberImagePickerLauncher
import com.example.medicord.viewmodel.PasienFormViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasienFormScreen(
    navController: NavController,
    pasienId: Long,
    viewModel: PasienFormViewModel = viewModel(
        factory = PasienFormViewModelFactory(
            PasienRepository(AppDatabase.getDatabase(LocalContext.current).pasienDao())
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    val imagePicker = rememberImagePickerLauncher { imagePath ->
        viewModel.updateFotoPath(imagePath)
    }

    LaunchedEffect(pasienId) {
        if (pasienId > 0) {
            viewModel.loadPasien(pasienId)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.popBackStack()
        }
    }

    fun openCamera() {
        if (imagePicker.hasCameraPermission()) {
            val uri = imagePicker.createCameraFile()
            uri?.let { imagePicker.cameraLauncher.launch(it) }
        } else {
            imagePicker.cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun openGallery() {
        if (imagePicker.hasStoragePermission()) {
            imagePicker.galleryLauncher.launch("image/*")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                imagePicker.storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                imagePicker.storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (pasienId > 0) Icons.Default.Edit else Icons.Default.PersonAdd,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 12.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            if (pasienId > 0) "Edit Pasien" else "Tambah Pasien",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.namaLengkap,
                onValueChange = viewModel::updateNamaLengkap,
                label = { Text("Nama Lengkap *") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = uiState.nik,
                onValueChange = viewModel::updateNik,
                label = { Text("NIK *") },
                leadingIcon = { Icon(Icons.Default.Badge, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.usia,
                    onValueChange = viewModel::updateUsia,
                    label = { Text("Usia *") },
                    leadingIcon = { Icon(Icons.Default.Cake, null) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Jenis Kelamin *",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = uiState.jenisKelamin == "Laki-laki",
                            onClick = { viewModel.updateJenisKelamin("Laki-laki") },
                            label = { Text("Laki-laki") },
                            modifier = Modifier.weight(1f),
                            leadingIcon = if (uiState.jenisKelamin == "Laki-laki") {
                                { Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp)) }
                            } else null
                        )
                        FilterChip(
                            selected = uiState.jenisKelamin == "Perempuan",
                            onClick = { viewModel.updateJenisKelamin("Perempuan") },
                            label = { Text("Perempuan") },
                            modifier = Modifier.weight(1f),
                            leadingIcon = if (uiState.jenisKelamin == "Perempuan") {
                                { Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp)) }
                            } else null
                        )
                    }
                }
            }

            OutlinedTextField(
                value = uiState.alamat,
                onValueChange = viewModel::updateAlamat,
                label = { Text("Alamat *") },
                leadingIcon = { Icon(Icons.Default.Home, null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = uiState.nomorTelepon,
                onValueChange = viewModel::updateNomorTelepon,
                label = { Text("Nomor Telepon *") },
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = uiState.catatanRiwayat,
                onValueChange = viewModel::updateCatatanRiwayat,
                label = { Text("Catatan Riwayat") },
                leadingIcon = { Icon(Icons.Default.Note, null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            // Photo Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Photo Preview
                if (uiState.fotoPath != null && File(uiState.fotoPath!!).exists()) {
                    Card(
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context)
                                    .data(File(uiState.fotoPath!!))
                                    .build()
                            ),
                            contentDescription = "Foto Pasien",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                // Photo Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { openCamera() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kamera")
                    }
                    OutlinedButton(
                        onClick = { openGallery() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Galeri")
                    }
                }
                
                if (uiState.fotoPath != null) {
                    TextButton(
                        onClick = { viewModel.updateFotoPath(null) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Hapus Foto")
                    }
                }
            }

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = { viewModel.savePasien() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Simpan",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

class PasienFormViewModelFactory(private val pasienRepository: PasienRepository) :
    androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasienFormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasienFormViewModel(pasienRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
