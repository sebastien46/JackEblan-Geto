package com.core.data.wrapper

import android.content.ClipData
import android.content.ClipboardManager
import com.core.domain.wrapper.ClipboardManagerWrapper
import javax.inject.Inject

class DefaultClipboardManagerWrapper @Inject constructor(
    private val clipboardManager: ClipboardManager
) : ClipboardManagerWrapper {
    override fun setPrimaryClip(label: String, text: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
    }
}