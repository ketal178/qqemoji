package com.ketal.qemoji

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HookEntry : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            //"com.tencent.tim", TODO support tim
            "com.tencent.mobileqq" -> {
                HookLoader(lpparam)
            }
        }
    }
}