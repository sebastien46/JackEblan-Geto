package com.feature.user_app_settings.domain.use_case.add_settings

import com.feature.user_app_settings.domain.use_case.ValidationResult

class ValidateLabel {

    operator fun invoke(label: String): ValidationResult {
        if (label.isBlank()) {
            return ValidationResult(successful = false, errorMessage = "Settings label is blank")
        }

        return ValidationResult(successful = true)
    }
}