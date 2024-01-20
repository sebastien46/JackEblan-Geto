package com.feature.securesettingslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.repository.SecureSettingsRepository
import com.core.domain.usecase.CopySettingsUseCase
import com.core.model.SettingsType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecureSettingsListViewModel @Inject constructor(
    private val secureSettingsRepository: SecureSettingsRepository,
    private val copySettingsUseCase: CopySettingsUseCase
) : ViewModel() {

    private val _uIState =
        MutableStateFlow<SecureSettingsListUiState>(SecureSettingsListUiState.Empty)

    private var _showSnackBar = MutableStateFlow<String?>(null)

    val showSnackBar = _showSnackBar.asStateFlow()

    val uIState = _uIState.asStateFlow()

    fun onEvent(event: SecureSettingsListEvent) {
        when (event) {
            is SecureSettingsListEvent.GetSecureSettingsList -> {
                val settingsType = when (event.selectedRadioOptionIndex) {
                    0 -> SettingsType.SYSTEM

                    1 -> SettingsType.SECURE

                    2 -> SettingsType.GLOBAL

                    else -> SettingsType.SYSTEM
                }

                viewModelScope.launch {
                    _uIState.value = SecureSettingsListUiState.Loading

                    _uIState.value = SecureSettingsListUiState.Success(
                        secureSettingsRepository.getSecureSettings(settingsType)
                    )
                }
            }

            is SecureSettingsListEvent.OnCopySecureSettingsList -> {
                copySettingsUseCase(event.secureSettings).onSuccess {
                    _showSnackBar.value = it
                }.onFailure {
                    _showSnackBar.value = it.localizedMessage
                }
            }
        }
    }

    fun clearState() {
        _showSnackBar.value = null
    }
}