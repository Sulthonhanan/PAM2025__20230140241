package com.example.medicord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicord.Data.database.entity.HistoriVisitEntity
import com.example.medicord.Data.database.entity.ObatEntity
import com.example.medicord.Data.database.entity.PasienEntity
import com.example.medicord.Data.repository.HistoriVisitRepository
import com.example.medicord.Data.repository.ObatRepository
import com.example.medicord.Data.repository.PasienRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class PasienDetailUiState(
    val pasien: PasienEntity? = null,
    val historiList: List<HistoriVisitEntity> = emptyList(),
    val obatList: List<ObatEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class PasienDetailViewModel(
    private val pasienRepository: PasienRepository,
    private val historiRepository: HistoriVisitRepository,
    private val obatRepository: ObatRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PasienDetailUiState())
    val uiState: StateFlow<PasienDetailUiState> = _uiState.asStateFlow()

    fun loadPasienDetail(pasienId: Long) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val pasien = pasienRepository.getPasienById(pasienId)
                _uiState.value = _uiState.value.copy(
                    pasien = pasien,
                    isLoading = false
                )
                pasien?.let {
                    loadHistori(it.id)
                    loadObat(it.id)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error loading pasien: ${e.message}"
                )
            }
        }
    }

    private fun loadHistori(pasienId: Long) {
        viewModelScope.launch {
            historiRepository.getHistoriByPasienId(pasienId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error loading histori: ${e.message}"
                    )
                }
                .collect { historiList ->
                    _uiState.value = _uiState.value.copy(historiList = historiList)
                }
        }
    }

    private fun loadObat(pasienId: Long) {
        viewModelScope.launch {
            obatRepository.getObatByPasienId(pasienId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error loading obat: ${e.message}"
                    )
                }
                .collect { obatList ->
                    _uiState.value = _uiState.value.copy(obatList = obatList)
                }
        }
    }

    suspend fun addHistori(histori: HistoriVisitEntity): Result<Long> {
        return try {
            val id = historiRepository.insertHistori(histori)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addObat(obat: ObatEntity): Result<Long> {
        return try {
            val id = obatRepository.insertObat(obat)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
