package com.example.medicord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicord.Data.database.entity.PasienEntity
import com.example.medicord.Data.repository.PasienRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class PasienUiState(
    val pasienList: List<PasienEntity> = emptyList(),
    val filteredList: List<PasienEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedJenisKelamin: String? = null,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDeleteDialog: Boolean = false,
    val pasienToDelete: PasienEntity? = null
)

class PasienViewModel(private val pasienRepository: PasienRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PasienUiState())
    val uiState: StateFlow<PasienUiState> = _uiState.asStateFlow()

    init {
        loadAllPasien()
    }

    private fun loadAllPasien() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            pasienRepository.getAllPasien()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Error loading data: ${e.message}"
                    )
                }
                .collect { pasienList ->
                    _uiState.value = _uiState.value.copy(
                        pasienList = pasienList,
                        isLoading = false,
                        errorMessage = null
                    )
                    applyFilters()
                }
        }
    }

    fun searchPasien(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun filterByJenisKelamin(jenisKelamin: String?) {
        _uiState.value = _uiState.value.copy(selectedJenisKelamin = jenisKelamin)
        applyFilters()
    }

    fun filterByUsia(minAge: Int?, maxAge: Int?) {
        _uiState.value = _uiState.value.copy(minAge = minAge, maxAge = maxAge)
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val query = state.searchQuery.trim()
        val jenisKelamin = state.selectedJenisKelamin
        val minAge = state.minAge
        val maxAge = state.maxAge

        // Simplified filtering approach - filter the current list
        var filtered = state.pasienList
        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.namaLengkap.contains(query, ignoreCase = true) ||
                        it.nik.contains(query, ignoreCase = true)
            }
        }
        if (jenisKelamin != null) {
            filtered = filtered.filter { it.jenisKelamin == jenisKelamin }
        }
        if (minAge != null) {
            filtered = filtered.filter { it.usia >= minAge }
        }
        if (maxAge != null) {
            filtered = filtered.filter { it.usia <= maxAge }
        }

        _uiState.value = state.copy(filteredList = filtered)
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            selectedJenisKelamin = null,
            minAge = null,
            maxAge = null
        )
        applyFilters()
    }

    suspend fun insertPasien(pasien: PasienEntity): Result<Long> {
        return try {
            val id = pasienRepository.insertPasien(pasien)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePasien(pasien: PasienEntity): Result<Unit> {
        return try {
            pasienRepository.updatePasien(pasien)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun showDeleteDialog(pasien: PasienEntity) {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = true,
            pasienToDelete = pasien
        )
    }

    fun dismissDeleteDialog() {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = false,
            pasienToDelete = null
        )
    }

    suspend fun deletePasien(pasien: PasienEntity): Result<Unit> {
        return try {
            pasienRepository.deletePasien(pasien)
            dismissDeleteDialog()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
