package com.orange.og_lite.Util


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import com.jzsql.lib.mmySql.InitCaller

import com.orange.jzchi.jzframework.JzActivity
import com.orange.og_lite.MainActivity
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.Donload_C
import com.orange.og_lite.callback.Update_C

import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object Util_FileDowload {
    var Internet = true
    var Beta = false
    fun haveData(activity: Activity, caller: Update_C) {
        Thread {
            try {
                if (JzActivity.getControlInstance().getPro("init", false)) {
                    caller.Finish(true)
                    return@Thread
                }
                var success = true
                if (JzActivity.getControlInstance().getPro("mmyinit", "no") == "no") {
                    Log.d("下載", "下載mmy ok")
                    if (!doloadmmy()) {
                        success = false
                    }
                }
                if (JzActivity.getControlInstance().getPro("s19init", "no") == "no") {
                    Log.d("下載", "下載s19 ok")
                    if (!DownAllS19(activity, caller)) {
                        success = false
                    }
                }
                if (JzActivity.getControlInstance().getPro("obdinit", "no") == "no") {
                    if (!DownAllObd(activity, caller)) {
                        success = false
                        Log.e("下載", "下載Obd 失敗")
                    }
                }
                Log.e("下載情況", "$success")
                if (success) {
                    JzActivity.getControlInstance().setPro("init", true)
                } else {
                    JzActivity.getControlInstance().setPro("init", false)
                }
                caller.Finish(success)
            } catch (e: Exception) {
                e.printStackTrace()
                caller.Finish(false)
            }

        }.start()
    }

    fun ChechUpdate(activity: Activity, caller: Update_C) {
        try {
            if (!doloadmmy()) {
                caller.Finish(false)
                return
            }
            if (!DownAllS19(activity, caller)) {
                caller.Finish(false)
                return
            }
            if (!DownAllObd(activity, caller)) {
                caller.Finish(false)
                return
            }
            caller.Finish(true)
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Finish(false)
        }

    }


    fun DownAllObd(activity: Activity, caller: Update_C): Boolean {
        try {
            val response = GetText(
                if (Beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Drive/OBD%20DONGLE/" else "https://bento2.orange-electronic.com/Orange%20Cloud/Drive/OBD%20DONGLE/",
                10
            )
            if (response == "nodata") {
                return false
            }
            var success = true
            val arg =
                response.split("HREF=\"".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in arg.indices) {
                if (i != 1 && arg[i].contains("OBD%20DONGLE")) {
                    Log.e("obd", arg[i].substring(arg[i].indexOf(">") + 1, arg[i].indexOf("<")))
                    if (!DonloadObd(
                            arg[i].substring(arg[i].indexOf(">") + 1, arg[i].indexOf("<")),
                            activity
                        )
                    ) {
                        success = false
                    }
                }
                caller.Updateing(i * 100 / arg.size / 3 + 100 / 3 * 2)
//                i * 100 / arg.size / 3
            }

            JzActivity.getControlInstance().setPro("obdinit", if (success) "yes" else "no")
            return success
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun DonloadObd(name: String, activity: Activity): Boolean {
        try {

            val donloadobd = GetObdName(name)
            if (donloadobd == "nodata") {
                Log.e("obd失敗", name)
                return false
            }
            if (donloadobd == JzActivity.getControlInstance().getPro("obd$name", "nodata")) {
                return true
            }
            val data = GetText(
                if (Beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Drive/OBD%20DONGLE/$name/$donloadobd" else "https://bento2.orange-electronic.com/Orange%20Cloud/Drive/OBD%20DONGLE/$name/$donloadobd",
                10
            )
            if (data != "nodata") {
                PublicBeans.insertFile(name, data)
                JzActivity.getControlInstance().setPro("obd$name", donloadobd)
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun GetObdName(name: String): String {
        try {
            val response = GetText(
                if (Beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Drive/OBD%20DONGLE/$name" else "https://bento2.orange-electronic.com/Orange%20Cloud/Drive/OBD%20DONGLE/$name",
                10
            )
            if (response == "nodata") {
                return response
            }
            val arg =
                response.split(" HREF=\"".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (a in arg) {
                if (a.contains(".srec")) {
                    return a.substring(a.indexOf(">") + 1, a.indexOf("<"))
                }
            }
            return "nodata"
        } catch (e: Exception) {
            e.printStackTrace()
            return "nodata"
        }

    }

    fun DownAllS19(activity: Activity, caller: Update_C): Boolean {
        try {
            val response = GetText(
                if (Beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Database/SensorCode/SIII/" else "https://bento2.orange-electronic.com/Orange%20Cloud/Database/SensorCode/SIII/",
                10
            )
            if (response == "nodata") {
                return false
            }
            var success = true
            val arg =
                response.split("HREF=\"".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in arg.indices) {
                if (i != 0 && arg[i].contains("SIII")) {
                    if (!donloads19(
                            arg[i].substring(arg[i].indexOf(">") + 1, arg[i].indexOf("<"))
                        )
                    ) {
                        success = false
                    }
                }
                caller.Updateing(i * 100 / arg.size / 3)
            }

            JzActivity.getControlInstance().setPro("s19init", if (success) "yes" else "no")
            return success
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun GetS19Name(name: String): String {
        try {
            val response = GetText(
                if (Beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Database/SensorCode/SIII/$name/" else "https://bento2.orange-electronic.com/Orange%20Cloud/Database/SensorCode/SIII/$name/",
                10
            )
            if (response == "nodata") {
                return response
            }
            val arg =
                response.split(" HREF=\"".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (a in arg) {
                if (a.contains(".s19")) {
                    return a.substring(a.indexOf(">") + 1, a.indexOf("<"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "nodata"
    }


    fun donloads19(name: String): Boolean {
        try {
            val s19name = GetS19Name(name)

            if (JzActivity.getControlInstance().getPro(name, "no") == s19name) {
                return true
            }
            val data = GetText(
                if (Beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Database/SensorCode/SIII/$name/$s19name" else "https://bento2.orange-electronic.com/Orange%20Cloud/Database/SensorCode/SIII/$name/$s19name",
                10
            )
            if (data != "nodata") {
                JzActivity.getControlInstance().setPro(name, s19name)
                PublicBeans.insertFile(name, data)
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun doloadmmy(): Boolean {
        try {
            val activity = JzActivity.getControlInstance().getRootActivity() as MainActivity
            val mmyname = mmyname()
            if (JzActivity.getControlInstance().getPro("mmy", "") == mmyname) {
                return true
            } else {
                val a =
                    activity.item.init_ByUrl(if (Beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Database/MMY/EU/$mmyname" else "https://bento2.orange-electronic.com/Orange%20Cloud/Database/MMY/EU/$mmyname")
                if (a) {
                    JzActivity.getControlInstance().setPro("mmy", mmyname)
                }
                return a
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun mmyname(): String {
        try {
            val response = GetText(
                if (Beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Database/MMY/EU/" else "https://bento2.orange-electronic.com/Orange%20Cloud/Database/MMY/EU/",
                10
            )
            if (response == "nodata") {
                return response
            }
            val arg =
                response.split("HREF=\"".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (a in arg) {
                if (a.contains(".db")) {
                    return a.substring(a.indexOf(">") + 1, a.indexOf("<"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "nodata"
    }

    fun FileDonload(path: String, url: String, timeout: Int, caller: Donload_C): Boolean {
        try {
            Log.d("path", path)
            val conn = URL(url).openConnection()
            conn.connectTimeout = 1000 * timeout
            val `is` = conn.inputStream
            val fos = FileOutputStream(path)
            val bufferSize = 8192
            var prread = 0.0
            val buf = ByteArray(bufferSize)
            while (true) {
                val read = `is`.read(buf)
                prread += read.toDouble()
                if (read == -1) {
                    break
                }
                fos.write(buf, 0, read)
                caller.Updateing((prread * 100 / JzActivity.getControlInstance().getAppInformation().getAppSize()).toInt())
            }
            `is`.close()
            fos.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("錯誤", e.message)
            return false
        }

    }


    fun GetText(url: String, timeout: Int): String {
        try {
            val conn = URL(url).openConnection()
            conn.connectTimeout = timeout * 1000
            val reader = BufferedReader(InputStreamReader(conn.inputStream, "utf-8"))
            var line: String? = null
            val strBuf = StringBuffer()
            line = reader.readLine()
            while (line != null) {
                strBuf.append(line)
                line = reader.readLine()
            }
            return strBuf.toString()
        } catch (e: Exception) {
            Log.e("錯誤", e.message)
            e.printStackTrace()
            return "nodata"
        }

    }
}