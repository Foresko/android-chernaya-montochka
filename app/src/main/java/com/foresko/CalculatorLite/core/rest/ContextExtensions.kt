package com.foresko.CalculatorLite.core.rest

import android.content.Context

val Context.appStore: AppStore
    get() = when (packageManager.getInstallerPackageName(packageName)) {
        "com.xiaomi.mipicks" -> AppStore.GetApps
        "com.huawei.appmarket" -> AppStore.HuaweiAppGallery
        "com.sec.android.app.samsungapps" -> AppStore.GalaxyStore
        "ru.vk.store" -> AppStore.RuStore
        else -> AppStore.Other
    }