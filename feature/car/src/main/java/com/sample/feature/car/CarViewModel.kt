package com.sample.feature.car

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.data.repository.car.CarRepository
import com.sample.feature.car.BookUiState.Loading
import com.sample.feature.car.BookUiState.Success
import com.sample.feature.car.BookUiState.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CarViewModel @Inject constructor(
    private val mCarRepository: CarRepository
) : ViewModel() {

    val uiState: StateFlow<BookUiState> = mCarRepository
        .carNames.map<List<String>, BookUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addCar(name: String) {
        viewModelScope.launch {
            mCarRepository.add(name)
        }
    }
}

sealed interface BookUiState {
    object Loading : BookUiState
    data class Error(val throwable: Throwable) : BookUiState
    data class Success(val data: List<String>) : BookUiState
}
