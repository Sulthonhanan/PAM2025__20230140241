package com.example.medicord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicord.Data.database.entity.PasienEntity
import com.example.medicord.Data.repository.PasienRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PasienFormUiState(
    val id: Long = 0,
    val namaLengkap: String = "",
    val nik: String = "",
    val usia: String = "",
    val jenisKelamin: String = "",
    val alamat: String = "",
    val nomorTelepon: String = "",
    val catatanRiwayat: String = "",
    val fotoPath: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class PasienFormViewModel(private val pasienRepository: PasienRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PasienFormUiState())
    val uiState: StateFlow<PasienFormUiState> = _uiState.asStateFlow()

    fun loadPasien(pasienId: Long) {
        viewModelScope.launch {
            try {
                val pasien = pasienRepository.getPasienById(pasienId)
                pasien?.let {
                    _uiState.value = PasienFormUiState(
                        id = it.id,
                        namaLengkap = it.namaLengkap,
                        nik = it.nik,
                        usia = it.usia.toString(),
                        jenisKelamin = it.jenisKelamin,
                        alamat = it.alamat,
                        nomorTelepon = it.nomorTelepon,
                        catatanRiwayat = it.catatanRiwayat ?: "",
                        fotoPath = it.fotoPath
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error loading pasien: ${e.message}"
                )
            }
        }
    }

    fun updateNamaLengkap(nama: String) {
        _uiState.value = _uiState.value.copy(namaLengkap = nama, errorMessage = null)
    }

    fun updateNik(nik: String) {
        _uiState.value = _uiState.value.copy(nik = nik, errorMessage = null)
    }

    fun updateUsia(usia: String) {
        _uiState.value = _uiState.value.copy(usia = usia, errorMessage = null)
    }

    fun updateJenisKelamin(jenisKelamin: String) {
        _uiState.value = _uiState.value.copy(jenisKelamin = jenisKelamin, errorMessage = null)
    }

    fun updateAlamat(alamat: String) {
        _uiState.value = _uiState.value.copy(alamat = alamat, errorMessage = null)
    }

    fun updateNomorTelepon(telepon: String) {
        _uiState.value = _uiState.value.copy(nomorTelepon = telepon, errorMessage = null)
    }

    fun updateCatatanRiwayat(catatan: String) {
        _uiState.value = _uiState.value.copy(catatanRiwayat = catatan, errorMessage = null)
    }

    fun updateFotoPath(path: String?) {
        _uiState.value = _uiState.value.copy(fotoPath = path)
    }

    private fun validateInput(): String? {
        val state = _uiState.value
        if (state.namaLengkap.isBlank()) return "Nama lengkap harus diisi"
        if (state.nik.isBlank()) return "NIK harus diisi"
        if (state.nik.length < 16) return "NIK harus minimal 16 digit"
        if (state.usia.isBlank()) return "Usia harus diisi"
        val usiaInt = state.usia.toIntOrNull()
        if (usiaInt == null || usiaInt < 0 || usiaInt > 150) return "Usia tidak valid"
        if (state.jenisKelamin.isBlank()) return "Jenis kelamin harus dipilih"
        if (state.alamat.isBlank()) return "Alamat harus diisi"
        if (state.nomorTelepon.isBlank()) return "Nomor telepon harus diisi"
        return null
    }

    fun savePasien() {
        val error = validateInput()
        if (error != null) {
            _uiState.value = _uiState.value.copy(errorMessage = error)
            return
        }

        val state = _uiState.value
        _uiState.value = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val pasien = PasienEntity(
                    id = state.id,
                    namaLengkap = state.namaLengkap.trim(),
                    nik = state.nik.trim(),
                    usia = state.usia.toInt(),
                    jenisKelamin = state.jenisKelamin,
                    alamat = state.alamat.trim(),
                    nomorTelepon = state.nomorTelepon.trim(),
                    catatanRiwayat = state.catatanRiwayat.trim().takeIf { it.isNotEmpty() },
                    fotoPath = state.fotoPath
                )

                if (state.id == 0L) {
                    pasienRepository.insertPasien(pasien)
                } else {
                    pasienRepository.updatePasien(pasien)
                }

                _uiState.value = state.copy(
                    isLoading = false,
                    isSuccess = true,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    errorMessage = "Error saving pasien: ${e.message}"
                )
            }
        }
    }

    fun resetSuccessState() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}
