package com.ketal.qemoji

import android.app.Application
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import com.ketal.qemoji.data.hostInfo
import com.ketal.qemoji.data.init
import com.ketal.qemoji.hook.ModuleEntryHook
import com.ketal.qemoji.utils.*
import java.lang.reflect.Method

private const val QEMOJI_TAG = "QEmoji_TAG"
private var firstInit = false
var secondInitQQ = false
    private set

class HookLoader(lpparam: XC_LoadPackage.LoadPackageParam) {
    init {
        doInit(lpparam.classLoader)
    }

    private fun initItem(classLoader: ClassLoader) {
        Utils(classLoader)
        ModuleEntryHook()
        ResInject.initForStubActivity()
        ResInject.injectModuleResources(hostInfo.application.resources)
    }

    private fun doInit(rtLoader: ClassLoader) {
        if (firstInit) return
        try {
            val startup: XC_MethodHook = object : XC_MethodHook(51) {
                override fun afterHookedMethod(param: MethodHookParam) {
                    try {
                        if (secondInitQQ) return
                        val clazz = rtLoader.loadClass("com.tencent.common.app.BaseApplicationImpl")
                        val ctx =
                            clazz!!.let {
                                getField(
                                    it,
                                    "sApplication",
                                    clazz
                                )?.get(null)
                            } as Application
                        init(ctx)
                        appContext = hostInfo.application
                        if ("true" == System.getProperty(QEMOJI_TAG)) return
                        val classLoader = ctx.classLoader
                        System.setProperty(QEMOJI_TAG, "true")
                        initItem(classLoader)
                        secondInitQQ = true
                    } catch (e: Throwable) {
                        loge(e)
                        throw e
                    }
                }
            }
            val loadDex = rtLoader.loadClass("com.tencent.mobileqq.startup.step.LoadDex")
            val ms = loadDex.declaredMethods
            var m: Method? = null
            for (method in ms) {
                if (method.returnType == Boolean::class.javaPrimitiveType && method.parameterTypes.isEmpty()) {
                    m = method
                    break
                }
            }
            XposedBridge.hookMethod(m, startup)
            firstInit = true
        } catch (e: Throwable) {
            if (e.toString().contains("com.bug.zqq")) return
            if (e.toString().contains("com.google.android.webview")) return
            throw e
        }
    }
}