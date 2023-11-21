package com.feature.user_app_settings.domain.use_case.add_settings

import com.feature.user_app_settings.domain.use_case.ValidationResult

class ValidateValueOnLaunch {

    operator fun invoke(valueOnLaunch: String): ValidationResult {
        if (valueOnLaunch.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Settings value on launch is blank"
            )
        }

        return ValidationResult(successful = true)
    }
}