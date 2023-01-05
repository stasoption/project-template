package com.averin.android.developer.app.init

import android.os.StrictMode
import com.averin.android.developer.BuildConfig
import com.averin.android.developer.base.init.BaseAppInitializer

class StrictModeInitializer : BaseAppInitializer {
    override fun onAppCreateInit() {
//        if (BuildConfig.DEBUG) {
//            StrictMode.enableDefaults()
//            StrictMode.setVmPolicy(
//                StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
//                    .penaltyLog()
//                    .build()
//            )
//            StrictMode.setThreadPolicy(
//                StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectNetwork()
//                    .detectCustomSlowCalls()
//                    .penaltyLog()
//                    .build()
//            )
//        }
    }
}
