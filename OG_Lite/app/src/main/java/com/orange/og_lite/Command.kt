package com.orange.og_lite

import android.app.Dialog
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.example.jzandroidwidget.JzTool
import com.jianzhi.jzblehelper.FormatConvert
import com.jianzhi.jzblehelper.FormatConvert.bytesToHex
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.*
import com.orange.og_lite.beans.PublicBeans.Companion.DongleState
import com.orange.og_lite.callback.Copy_C
import com.orange.og_lite.callback.Program_C
import kotlinx.android.synthetic.main.data_loading.*
import kotlinx.android.synthetic.main.loading_view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.xor

class Command {
    //abc
    companion object {
        var rx = ""
        var obdCommand = ObdCommand()
        var ogCommand = OgCommand()
        val handler :Handler
        get(){
           return JzActivity.getControlInstance().getRootActivity().handler
        }
        var demo = false
        var clock= JzTool.newInstance.timer()
        private val rxUUID = "00008D81-0000-1000-8000-00805F9B34FB"
        private val TXUUID = "00008D82-0000-1000-8000-00805F9B34FB"
        fun Send(a: String) {
            rx = ""
            if( !(JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.isConnect()){
                handler.post {
                    (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
                    JzActivity.getControlInstance()
                        .showDiaLog(R.layout.activity_scan_ble, false, false,
                            Da_Scan_ble(object : connectBack {
                                override fun connec() {
                                    (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.stopScan()
                                }
                            }), "Da_Scan_ble"
                        )
                }
                return
            }
            (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.writeHex(
                addcheckbyte(a),
                rxUUID,
                TXUUID
            )
        }

        fun addcheckbyte(com: String): String {
            val a = FormatConvert.StringHexToByte(com)
            var checkbyte = a[0]
            for (i in 1 until a.size - 2) {
                checkbyte = checkbyte xor a[i]
            }
            a[a.size - 2] = checkbyte
            return bytesToHex(a)
        }

        interface callback {
            fun result(a: Boolean)
        }

        interface idback {
            fun result(a: String)
        }

        interface notify {
            fun result()
        }

        fun send(data: String) {

        }

        fun getObd(myitem: ObdBeans, result: callback) {
            JzActivity.getControlInstance()
                .showDiaLog(R.layout.loading_view, false, false, object : SetupDialog() {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text = rootview.context.resources.getString(R.string.wait)
                    }
                }, "loading_view")
            if (demo) {
                Thread {
                    Thread.sleep(3000)
                    handler.post {
                        JzActivity.getControlInstance().closeDiaLog("loading_view")
                        val geta = true
                        if (geta) {
                            for (i in 0 until myitem.OldSemsor.count()) {
                                myitem.OldSemsor[i] = "0000000$i"
                            }
                        }
                        result.result(geta)
                    }
                }.start()
            } else {
                Thread {
                    if(goState(BootloaderState.Bootloader)){
                        val a = obdCommand.loadObdApp()
                        if (a) {
                            if (obdCommand.gerID(myitem)) {
                                handler.post {
                                    JzActivity.getControlInstance().closeDiaLog("loading_view")
                                    result.result(true)
                                }
                            } else {
                                handler.post {
                                    JzActivity.getControlInstance().closeDiaLog("loading_view")
                                    result.result(false)
                                }
                            }
                        } else {
                            handler.post {
                                JzActivity.getControlInstance().closeDiaLog("loading_view")
                                result.result(false)
                            }
                        }
                    }else{
                        handler.post {
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                            result.result(false)
                        }
                    }

                }.start()
            }
        }

        fun getSensorID(myitem: ArrayList<TextView>, result: callback) {
            JzActivity.getControlInstance()
                .showDiaLog(R.layout.loading_view, false, false, object : SetupDialog() {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text = rootview.context.resources.getString(R.string.wait)
                    }
                }, "loading_view")
            Thread {
                Thread.sleep(3000)
                handler.post {
                    JzActivity.getControlInstance().closeDiaLog("loading_view")
                    val geta = true
                    if (geta) {
                        for (i in 0 until myitem.count()) {
                            myitem[i].text = "0000000$i"
                        }
                    }
                    result.result(geta)
                }
            }.start()

        }

        fun getSensorData(myitem: Function_Check_Sensor_Item, resultt: callback) {
            JzActivity.getControlInstance()
                .showDiaLog(R.layout.data_loading, false, true, object : SetupDialog() {
                    override fun dismess() {
                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        if (event.keyCode == 4) {
                            ogCommand.cancel = true
                        }
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                    }
                }, "data_loading")
            if (demo) {
                Thread {
                    Thread.sleep(3000)
                    handler.post {
                        JzActivity.getControlInstance().closeDiaLog("data_loading")
                        val geta = true
                        if (geta) {
                            for (i in 0 until myitem.item.count()) {
                                myitem.item2[i] = "0000000$i"
                            }
                        }
                        resultt.result(geta)
                    }
                }.start()
            } else {
                Thread {
                    if(goState(BootloaderState.Og_App)){
                        var result = ogCommand.GetId()
                        handler.post {

                            JzActivity.getControlInstance().closeDiaLog()
                            if (result.success) {
                                myitem.item2[0] = result.id
                                myitem.item2[1] = "" + result.kpa
                                myitem.item2[2] = if (result.有無胎溫) "" + result.c else "NA"
                                myitem.item2[3] = if (result.有無電池) "ok"  else "NA"
                                myitem.item2[4] = if (result.有無電壓) "" + result.vol else "NA"
                                resultt.result(true)
                            } else {
                                resultt.result(false)
                                JzActivity.getControlInstance().showDiaLog(
                                    R.layout.data_loading_false,
                                    true,
                                    false,
                                    "data_loading_false"
                                )
                            }
                        }
                    }else{
                        handler.post {
                            resultt.result(false)
                            JzActivity.getControlInstance().showDiaLog(
                                R.layout.data_loading_false,
                                true,
                                false,
                                "data_loading_false"
                            )
                        }
                        JzActivity.getControlInstance().closeDiaLog()
                    }
                }.start()
            }
        }

        fun readId(result: idback) {
            JzActivity.getControlInstance()
                .showDiaLog(R.layout.loading_view, false, false, object : SetupDialog() {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text =
                            rootview.context.resources.getString(R.string.app_sensor_info_read) + "..."
                    }
                }, "loading_view")
            if (demo) {
                JzActivity.getControlInstance().closeDiaLog("loading_view")
                result.result(arrayOf("aaaaaaaa", "bbbbbbbb", "cccccccc", "dddddddd").random())
            } else {
                Thread {
                    if(goState(BootloaderState.Og_App)){
                        val a = ogCommand.GetId()
                        handler.post {
                            if (a.success) {
                                result.result(a.id)
                            } else {
                                JzActivity.getControlInstance().showDiaLog(
                                    R.layout.data_loading_false,
                                    true,
                                    false,
                                    "data_loading_false"
                                )
                            }
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                    }else{
                        handler.post{
                            JzActivity.getControlInstance().showDiaLog(
                                R.layout.data_loading_false,
                                true,
                                false,
                                "data_loading_false"
                            )
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                    }
                }.start()
            }
        }
        fun getPrid(result: idback) {
            JzActivity.getControlInstance()
                .showDiaLog(R.layout.loading_view, false, false, object : SetupDialog() {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text =
                            rootview.context.resources.getString(R.string.app_sensor_info_read) + "..."
                    }
                }, "loading_view")
            if (demo) {
                JzActivity.getControlInstance().closeDiaLog("loading_view")
                result.result(arrayOf("aaaaaaaa", "bbbbbbbb", "cccccccc", "dddddddd").random())
            } else {
                Thread {
                    if(goState(BootloaderState.Og_App)){
                        val a = ogCommand.GetPr("00",PublicBeans.programNumber,PublicBeans.getHEX())
//                        叫一下()
                        handler.post {
                            if (a.size >= 0) {
                                for (i in a) {
                                   result.result(i.id)
                                }
                            } else {
                                JzActivity.getControlInstance().showDiaLog(
                                    R.layout.data_loading_false,
                                    true,
                                    false,
                                    "data_loading_false"
                                )
                            }
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                    }else{
                        handler.post{
                            JzActivity.getControlInstance().showDiaLog(
                                R.layout.data_loading_false,
                                true,
                                false,
                                "data_loading_false"
                            )
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                    }
                }.start()
            }
        }
fun 叫一下(){
        if(goState(BootloaderState.Bootloader)){Send("0AE200030000F5")}
}
        fun writeID(myitem: ObdBeans, resultn: notify) {
            JzActivity.getControlInstance()
                .showDiaLog(R.layout.data_loading, false, true, object : SetupDialog() {
                    override fun dismess() {
                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        if (event.keyCode == 4) {
                            ogCommand.cancel = true
                        }
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                    }
                }, "data_loading")
            if (demo) {
                for (i in 0 until myitem.rowcount) {
                    if (myitem.readable[i]) {
                        myitem.state[i] =
                            arrayListOf(ObdBeans.PROGRAM_SUCCESS, ObdBeans.PROGRAM_FALSE).random()
                    }
                }
                JzActivity.getControlInstance().closeDiaLog()
                resultn.result()
            } else {
                Thread {
                    if(goState(BootloaderState.Og_App)){
                        ogCommand.Program(
                            Integer.toHexString(myitem.getReadable()),
                            object : Program_C {
                                override fun Program_Progress(i: Int) {
                                    handler.post {
                                        JzActivity.getControlInstance()
                                            .showDiaLog(
                                                R.layout.data_loading,
                                                false,
                                                true,
                                                object : SetupDialog() {
                                                    override fun dismess() {
                                                    }

                                                    override fun keyevent(event: KeyEvent): Boolean {
                                                        if (event.keyCode == 4) {
                                                            ogCommand.cancel = true
                                                        }
                                                        return false
                                                    }

                                                    override fun setup(rootview: Dialog) {
                                                        rootview.pass.visibility = View.VISIBLE
                                                        rootview.pass.text = "$i%"
                                                    }
                                                },
                                                "data_loading"
                                            )
                                    }
                                }

                                override fun Program_Finish(a: Boolean) {
                                    if (a) {
                                        Log.e("DATA:", "燒錄成功")
                                        Thread.sleep(3000)
                                        val result = ogCommand.GetPrId()
                                        for (i in result) {
                                            for (check in 0 until myitem.getNewSensorReader().size) {
                                                val d = myitem.getNewSensorReader()[check]
                                                if (d.substring(d.length - 4, d.length) == i.id.substring(
                                                        4,
                                                        8
                                                    )
                                                ) {
                                                    myitem.replaceOldSensor(d, i.id)
                                                }
                                            }
                                            Log.e("DATA:", "成功id:" + i.id)
                                        }
                                        Thread {
                                            Thread.sleep(2000)
                                            ogCommand.IdCopy(object : Copy_C {
                                                override fun Copy_Finish(a: Boolean) {
                                                    handler.post {
                                                        if(!a){
                                                            for (i in 0 until myitem.rowcount) {
                                                                if (myitem.readable[i]) {
                                                                    myitem.state[i] = ObdBeans.PROGRAM_FALSE
                                                                }
                                                            }
                                                        }
                                                        JzActivity.getControlInstance().closeDiaLog()
                                                        resultn.result()
                                                    }
                                                }

                                                override fun Copy_Next(
                                                    success: Boolean,
                                                    position: Int
                                                ) {
                                                    handler.post {
                                                        JzActivity.getControlInstance().showDiaLog(
                                                            R.layout.data_loading,
                                                            false,
                                                            true,
                                                            object : SetupDialog() {
                                                                override fun dismess() {

                                                                }

                                                                override fun keyevent(event: KeyEvent): Boolean {
                                                                    if (event.keyCode == 4) {
                                                                        ogCommand.cancel = true
                                                                    }
                                                                    return false
                                                                }

                                                                override fun setup(rootview: Dialog) {
                                                                    rootview.pass.visibility =
                                                                        View.VISIBLE
                                                                    rootview.pass.text =
                                                                        "${position * 100 / myitem.getReadable()}%"
                                                                }

                                                            },
                                                            "data_loading"
                                                        )
                                                    }
                                                }
                                            }, myitem.idcount, myitem)
                                        }.start()
                                    } else {
                                        handler.post {
                                            JzActivity.getControlInstance().closeDiaLog()
                                            for (i in 0 until myitem.rowcount) {
                                                if (myitem.readable[i]) {
                                                    myitem.state[i] = ObdBeans.PROGRAM_FALSE
                                                }
                                            }
                                            resultn.result()
                                        }

                                    }
                                }
                            }
                            , myitem.getNewSensorReader()
                        )
                    }else{
                        handler.post {
                            JzActivity.getControlInstance().closeDiaLog("data_loading")
                            resultn.result()
                        }
                    }
                }.start()

            }

        }

        fun checkID(myitem: ArrayList<TextView>, image: ArrayList<TextView>, result: notify) {

            CheckBeans.CHECK =
                (arrayListOf(CheckBeans.CHECK_SUCCESS, CheckBeans.CHECK_FALSE).random())

            result.result()
        }

        fun writeOBD(myitem: ObdBeans, result: notify) {
            JzActivity.getControlInstance()
                .showDiaLog(R.layout.loading_view, false, false, object : SetupDialog() {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text = rootview.context.resources.getString(R.string.wait)
                    }
                }, "loading_view")
            if (demo) {
                for (i in 0 until myitem.rowcount) {
                    myitem.state[i] =
                        arrayListOf(ObdBeans.PROGRAM_SUCCESS, ObdBeans.PROGRAM_FALSE).random()
                }
                result.result()
                JzActivity.getControlInstance().closeDiaLog("loading_view")
            } else {
                Thread {
                    if(goState(BootloaderState.Obd_App)){
                        val a = obdCommand.setTire(myitem)
                        for (i in 0 until myitem.rowcount) {
                            myitem.state[i] =
                                if (a) ObdBeans.PROGRAM_SUCCESS else ObdBeans.PROGRAM_FALSE
                            myitem.OldSemsor[i] = myitem.NewSensor[i]
                        }
                        handler.post {
                            result.result()
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                    }else{
                        handler.post {
                            for (i in 0 until myitem.rowcount) {
                                myitem.state[i] =ObdBeans.PROGRAM_FALSE
                                myitem.OldSemsor[i] = myitem.NewSensor[i]
                            }
                            result.result()
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                    }
                }.start()
            }

        }

        fun Program(myitem: Function_Program_Item, resultt: notify) {
            JzActivity.getControlInstance()
                .showDiaLog(R.layout.data_loading, false, true, object : SetupDialog() {
                    override fun dismess() {
                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        if (event.keyCode == 4) {
                            ogCommand.cancel = true
                        }
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                    }
                }, "data_loading")
            if (demo) {
                JzActivity.getControlInstance().closeDiaLog()
                for (i in 0 until myitem.rowcount) {
                    myitem.state[i] = arrayListOf(
                        Function_Program_Item.PROGRAM_SUCCESS,
                        Function_Program_Item.PROGRAM_FALSE
                    ).random()
                }
                resultt.result()
            } else {
                Thread {

                    if(goState(BootloaderState.Og_App)){
                        ogCommand.Program(
                            Integer.toHexString(myitem.rowcount),
                            object : Program_C {
                                override fun Program_Progress(i: Int) {
                                    handler.post {
                                        JzActivity.getControlInstance().showDiaLog(
                                            R.layout.data_loading,
                                            false,
                                            true,
                                            object : SetupDialog() {
                                                override fun dismess() {

                                                }

                                                override fun keyevent(event: KeyEvent): Boolean {
                                                    if (event.keyCode == 4) {
                                                        ogCommand.cancel = true
                                                    }
                                                    return false
                                                }

                                                override fun setup(rootview: Dialog) {
                                                    rootview.pass.visibility =
                                                        View.VISIBLE
                                                    rootview.pass.text = "$i%"
                                                }

                                            },
                                            "data_loading"
                                        )
                                    }
                                }

                                override fun Program_Finish(a: Boolean) {
                                    if (!a) {
                                        handler.post {
                                            for (i in 0 until myitem.rowcount) {
                                                myitem.state[i] = Function_Program_Item.PROGRAM_FALSE
                                            }
                                            JzActivity.getControlInstance().closeDiaLog()
                                            resultt.result()
                                        }
                                        return
                                    }
                                    val result = ogCommand.GetPrId()
                                    handler.post {
                                        for (i in 0 until myitem.rowcount) {
                                            myitem.state[i] = Function_Program_Item.PROGRAM_FALSE
                                            for (b in result) {
                                                var compareid = myitem.programId[i]
                                                while (compareid.length < 8) {
                                                    compareid = "0${compareid}"
                                                }
                                                if (b.id.substring(8 - PublicBeans.getIdcount() + 2) == compareid.substring(
                                                        8 - PublicBeans.getIdcount() + 2
                                                    )
                                                ) {
                                                    myitem.programId[i] = b.id.substring(8 - b.idcount)
                                                    myitem.state[i] =
                                                        Function_Program_Item.PROGRAM_SUCCESS
                                                }
                                            }
                                        }
                                        JzActivity.getControlInstance().closeDiaLog()
                                        resultt.result()
                                    }
                                }
                            }, ArrayList(myitem.programId)
                        )
                    }else{
                        handler.post{
                            for (i in 0 until myitem.rowcount) {
                                myitem.state[i]=Function_Program_Item.PROGRAM_FALSE
                            }
                            JzActivity.getControlInstance().closeDiaLog()
                            resultt.result()
                        }
                    }

                }.start()
            }
        }

        fun getState(): Boolean {
            try {
                Send(addcheckbyte("0A0000030000F5"))
                clock.Zeroing()
                while (true) {
                    if (clock.stop() > 3) {
                        return false
                    }
                    if (rx.length >= 14) {
                        when (rx.substring(8, 10)) {
                            "01" -> {
                                DongleState = BootloaderState.Bootloader
                            }
                            "02" -> {
                                DongleState = BootloaderState.Og_App
                            }
                            "03" -> {
                                DongleState = BootloaderState.Obd_App
                            }
                        }
                        return true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

        fun goState(state:BootloaderState): Boolean {
            try {
//                Send(addcheckbyte("0A0D00030200F5"))
                Thread.sleep(500)
                when (state) {
                    BootloaderState.Bootloader -> {
                        Send(addcheckbyte("0A0D00030000F5"))
                    }
                    BootloaderState.Og_App -> {
                        Send(addcheckbyte("0A0D00030100F5"))
                    }
                    BootloaderState.Obd_App -> {
                        Send(addcheckbyte("0A0D00030200F5"))
                    }
                }
                Thread.sleep(1000)
                clock.Zeroing()
                if (getState() && DongleState == state) {
                    return true
                } else {
                    handler.post {
                        (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
                        JzActivity.getControlInstance()
                            .showDiaLog(R.layout.activity_scan_ble, false, false,
                                Da_Scan_ble(object : connectBack {
                                    override fun connec() {
                                        (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.stopScan()
                                    }
                                }), "Da_Scan_ble"
                            )
                    }
                    return false
                }
            } catch (e: Exception) {
                Log.e("錯誤", "d")
                e.printStackTrace()
                return false;
            }
        }


    }
}