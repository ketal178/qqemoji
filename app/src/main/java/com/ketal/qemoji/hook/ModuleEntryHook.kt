package com.ketal.qemoji.hook

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import com.ketal.qemoji.data.hostApp
import com.ketal.qemoji.data.hostInfo
import com.ketal.qemoji.secondInitQQ
import com.ketal.qemoji.utils.*
import java.lang.RuntimeException

//模块入口Hook
class ModuleEntryHook {
    init {
        hook()
    }

    private fun hook() {
        when (hostApp) {
            HostApp.QQ, HostApp.TIM -> hookQQ()
            else -> throw RuntimeException("不支持的软件")
        }
    }

    private fun hookQQ() {
        for (m in getMethods("com.tencent.mobileqq.activity.AboutActivity")) {
            if (m.name != "doOnCreate") continue
            XposedBridge.hookMethod(m, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    try {
                        val FormSimpleItem = loadClass("com.tencent.mobileqq.widget.FormSimpleItem")
                        var item = getObjectOrNull<View>(param.thisObject, "a", FormSimpleItem)
                        if (item == null) {
                            val ctx = param.thisObject as Activity
                            item =
                                (ctx.window.decorView as ViewGroup).findViewByType(FormSimpleItem)
                        }
                        val entry = newInstance(
                            FormSimpleItem,
                            param.thisObject,
                            Context::class.java
                        ) as View
                        invokeMethod(
                            entry,
                            "setLeftText",
                            "QEmoji",
                            CharSequence::class.java
                        )
                        invokeMethod(entry, "setRightText", "好耶", CharSequence::class.java)
                        val vg = item?.parent as ViewGroup
                        vg.addView(entry, 2)
                        entry.setOnClickListener {
                            if (secondInitQQ) {
                                appContext?.makeToast("开发中")
                                //val intent = Intent(appContext, SettingsActivity::class.java)
                                //appContext?.startActivity(intent)
                            } else {
                                appContext?.makeToast("坏耶 资源加载失败惹 重启${hostInfo.hostName}试试吧> <")
                            }
                        }
                    } catch (e: Exception) {
                        loge(e)
                    }
                }
            })
        }
    }
}