package com.core.domain.usecase.userapplist

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.core.model.AppItem

class GetNonSystemApps {
    operator fun invoke(packageManager: PackageManager): List<AppItem> {
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }.map {
            val label = packageManager.getApplicationLabel(it).toString()

            val icon = try {
                packageManager.getApplicationIcon(it.packageName)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }

            AppItem(icon = icon, packageName = it.packageName, label = label)
        }.sortedBy { it.label }
    }
}