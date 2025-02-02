package com.core.data.wrapper

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.core.domain.wrapper.BuildVersionWrapper
import javax.inject.Inject

class DefaultBuildVersionWrapper @Inject constructor() : BuildVersionWrapper {
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    override fun isApi32Higher(): Boolean {
        return Build.VERSION.SDK_INT >= 32
    }
}