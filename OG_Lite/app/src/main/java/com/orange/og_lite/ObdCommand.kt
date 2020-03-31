package com.orange.og_lite

import android.app.Dialog
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import com.example.jzandroidwidget.JzTool
import com.jianzhi.jzblehelper.FormatConvert.StringHexToByte
import com.jianzhi.jzblehelper.FormatConvert.bytesToHex
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.BootloaderState
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.loading_view.*
import java.util.*
import kotlin.experimental.xor

class ObdCommand {
    var clock=JzTool.newInstance.timer()
    val handler :Handler
        get(){
            return JzActivity.getControlInstance().getRootActivity().handler
        }
    private val rxUUID = "00008D81-0000-1000-8000-00805F9B34FB"
    private val TXUUID = "00008D82-0000-1000-8000-00805F9B34FB"
    fun send(a:String){
        Command.rx=""
        (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.writeHex(a,rxUUID,TXUUID)
    }

    //設定tireid
    fun setTire(myitem: ObdBeans):Boolean{
        try {
            val position = arrayOf("4", "1", "2", "3", "5")
            send("60A200FFFFFFFFC20A")
            Thread.sleep(50)
            for(i in 0 until myitem.NewSensor.size){
                var a=myitem.NewSensor[i]
                while(a.length<8){a= "0$a" }
send(addcheckbyte("60A20XidFF0A".replace("id",a ).replace("X", position[i])))
                Thread.sleep(50)
            }
            send("60A2FFFFFFFFFF3D0A")
            clock.Zeroing()
            while(true){
                if(clock.stop()>10){return false}
                if(Command.rx=="60B201FFFFFFFFD30A"){return true}
            }
        }catch (e:java.lang.Exception){e.printStackTrace()
            return false}
    }
    //讀取ID
    fun gerID(myitem: ObdBeans):Boolean{
try {
   val a = "60BF00010" + PublicBeans.wheelCount() + "FF0A";
    send(GetXOR(a))
    clock.Zeroing()
    var fal=0
    while(true){
if(clock.stop()>1){
fal+=1
    clock.Zeroing()
if(fal==10){return false}
    send(GetXOR(a))
}
        if(Command.rx.length == 52){
            for (i in 0 until myitem.OldSemsor.size){
                myitem.OldSemsor[i]=Command.rx.substring((i+1)*8,(i+2)*8)
            }
            return true
        }
        Thread.sleep(100)
    }
}catch (e:java.lang.Exception){e.printStackTrace()
return false}
    }
    //載入APP資料
    fun loadObdApp():Boolean{
        try {
            Command.goState(BootloaderState.Bootloader)
            askVersion()
            if (bytesToHex(PublicBeans.getOBDVersion()!!.toByteArray()) == PublicBeans.obdappversion) {
                return goPrObd()&&goApp()
            } else {
                if (goPrObd()&&writeVersion()&&goBootloader()) {
                    Thread.sleep(2000)
                    return writeFlush()&&Command.goState(BootloaderState.Obd_App)
                } else {
                    return false
                }
            }
        }catch (e:java.lang.Exception){
            return false
        }
    }
    //跳轉至燒錄OBD
    fun goPrObd():Boolean{
        try {
            val a =addcheckbyte("0A8D00030100F5")
            clock.Zeroing()
            send(a)
            while (true) {
                if (clock.stop() > 3) {
                    return false
                }
                if (Command.rx.length == 14) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            Log.d("CommandError", e.message)
            return false
        }
    }
    //握手指令
    fun handShake(): Boolean {
        try {
            val a =addcheckbyte("0A0000030000F5")
            clock.Zeroing()
            send(a)
            while (true) {
                if (clock.stop() > 3) {
                    return false
                }
                if (Command.rx.length == 14) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            Log.d("CommandError", e.message)
            return false
        }
    }
    //Reboot
    fun reboot(): Boolean {
        try {
            val a = addcheckbyte("0A0D00030000F5")
            send((a))
            clock.Zeroing()
            while (true) {
                if (clock.stop() > 3) {
                    return false
                }
                if (Command.rx==("F501000300F70A")) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            Log.d("CommandError", e.message)
            return false
        }
    }
    //取得版本號
    fun askVersion():Boolean{
        try {
            val a = GetXOR("0ACF000100FFF5")
            send((a))
            clock.Zeroing()
            while (true) {
                if (clock.stop() > 2) {
                    return false
                }
                if (Command.rx.length==54) {
                    PublicBeans.obdappversion=(Command.rx.substring(8, 50))
                    Log.d("BLECommand.rx", "版本號:" + PublicBeans.obdappversion)
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            Log.d("CommandError", e.message)
            return false
        }
    }
    //跳轉app
    fun goApp():Boolean{
        try {
            val a = GetXOR("0ACD000100FFF5")
            send((a))
            clock.Zeroing()
            var fal=0
            while (true) {
                if (clock.stop() > 2) {
                    fal++
                    if(fal==3){return false}
                    clock.Zeroing()
                    send((a))
                }
                if (Command.rx.length==14) {
                    Log.d("BLECommand.rx","進入app");
                    return true;
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            Log.d("CommandError", e.message)
            return false
        }
    }
    //寫入版本號
    fun writeVersion():Boolean{
        try {
            val a = GetXOR(
                "0ACA0015DDFFF5".replace(
                    "DD",
                    bytesToHex(PublicBeans.getOBDVersion()!!.toByteArray())))
            send(a)
            clock.Zeroing()
            var fal=0
            while (true) {
                if (clock.stop() > 1) {
                    fal++
                    if(fal==1){return false}
                    clock.Zeroing()
                    send((a))
                }
                if (Command.rx.length==14) {
                    Log.d("BLECommand.rx","寫入版本");
                    return true;
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            Log.d("CommandError", e.message)
            return false
        }
    }
    //跳轉Bootloader
    fun goBootloader():Boolean{
        try {
            val a = GetXOR("0ACD010100FFF5")
            send((a))
            clock.Zeroing()
            var fal=0
            while (true) {
                if (clock.stop() > 10) {
                    fal++
                    if(fal==1){return false}
                    clock.Zeroing()
                    send((a))
                }
                if (Command.rx.contains("F5CD010100CD0AF501000302F50A")) {
                    Log.d("BLECommand.rx","進入燒錄");
                    return true;
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            Log.d("CommandError", e.message)
            return false
        }
    }
    //寫入燒錄檔
    fun writeFlush():Boolean{
        try {
            val sb = PublicBeans.getOBD1()!!
            var long=0
            var Ind=298
            if (sb.length % Ind == 0) {
                long = sb.length / Ind
            } else {
                long = sb.length / Ind + 1
            }
            for(i in 0 until long){
                var b = i
                if (b >= 255) {
                    b -= 255
                }
                val result = StringBuffer(Integer.toHexString(b))
                while (result.length < 2) {
                    result.insert(0, "0")
                }
                val cont = result.toString().toUpperCase()
                if(i==long - 1){
                    Log.d("write", "以跑完$i")
                    val data = bytesToHex(sb.substring(i * Ind, sb.length).toByteArray())
                    val length = sb.substring(i * Ind, sb.length).toByteArray().size + 3
                    send(Convvvert(data, Integer.toHexString(length), cont))
                    handler.post {
                        JzActivity.getControlInstance().showDiaLog(R.layout.loading_view,false,false,object :SetupDialog(){
                            override fun dismess() {

                            }

                            override fun keyevent(event: KeyEvent): Boolean {
                                return false
                            }

                            override fun setup(rootview: Dialog) {
                                rootview.tit.text=rootview.context.resources.getString(R.string.Data_Loading)+"..."+100+"%"
                            }
                        },"loading_view")
                    }
                    return true
                }else{
                    val data = bytesToHex(sb.substring(i * Ind, i * Ind + Ind).toByteArray())
                    Log.d("行數", "" + i)
                    val length = sb.substring(i * Ind, i * Ind + Ind).toByteArray().size + 3
                    if (!check(Convvvert(data, Integer.toHexString(length), cont))) {
                        return false
                    }
                    handler.post {
                        JzActivity.getControlInstance().showDiaLog(R.layout.loading_view,false,false,object :SetupDialog(){
                            override fun dismess() {

                            }

                            override fun keyevent(event: KeyEvent): Boolean {
                                return false
                            }

                            override fun setup(rootview: Dialog) {
                                rootview.tit.text=rootview.context.resources.getString(R.string.Data_Loading)+"..."+(i * 100 / long )+"%"
                            }
                        },"loading_view")
                    }
                }
            }
            Log.d("總行數", "" + long)
            return true;
        } catch (e: Exception) {
            Log.d("CommandError", e.message)
            e.printStackTrace()
            return false
        }
    }
    //確認是否燒路成功
    fun check(data:String):Boolean{
        try {
            var a=addcheckbyte(data)
            clock.Zeroing()
            var fal=0
            send(a)
            while(true){
                if(clock.stop() > 2){
                    fal += 1
                    if(fal>20){return false}
                    clock.Zeroing()
                    send(a)
                }
                if(Command.rx.length >= 16){return true}
            }

        } catch (e:java.lang.Exception) {
            Log.d("CommandError", e.message);
            return false;
        }
    }
    fun GetXOR(a: String): String {
        val command = StringHexToByte(a)
        var xor = 0
        for (i in 0 until command.size - 2) {
            xor = xor xor command[i].toInt()
        }
        command[command.size - 2] = xor.toByte()
        return bytesToHex(command)
    }
    fun addcheckbyte(com: String): String {
        val a = StringHexToByte(com)
        var checkbyte = a[0]
        for (i in 1 until a.size - 2) {
            checkbyte = checkbyte xor a[i]
        }
        a[a.size - 2] = checkbyte
        return bytesToHex(a)
    }
    fun Convvvert(data: String, length: String, line: String): String {
        var length = length
        var line = line
        var command = "0A02LHX00F5"
        while (length.length < 4) {
            length = "0$length"
        }
        if (line == "F5") {
            line = "00"
        }
        if (line.length > 2) {
            line = "00"
        }
        command = addcheckbyte(command.replace("L", length).replace("X", data).replace("H", line))
        return command
    }
}