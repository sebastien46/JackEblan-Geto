package com.feature.user_app_settings.domain.use_case.user_app_settings

import com.feature.user_app_settings.domain.use_case.ValidationResult

class ValidateAppName {

    operator fun invoke(appName: String): ValidationResult {
        if (appName.isBlank()) {
            return ValidationResult(successful = false, errorMessage = "App name is invalid")
        }

        return ValidationResult(successful = true)
    }
}