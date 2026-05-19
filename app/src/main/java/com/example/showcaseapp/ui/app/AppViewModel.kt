package com.example.showcaseapp.ui.app

import androidx.lifecycle.ViewModel
import com.example.showcaseapp.data.local.OnboardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingPreferences
) : ViewModel() {
    private val _isOnboardingCompleted = MutableStateFlow(
        onboardingPreferences.isOnboardingCompleted()
    )
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    fun completeOnboarding() {
        onboardingPreferences.setOnboardingCompleted()
        _isOnboardingCompleted.value = true
    }
}
