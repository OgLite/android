package com.orange.og_lite

import android.app.Activity
import android.util.Log
import com.jianzhi.jzblehelper.FormatConvert
import com.orange.jzchi.jzframework.JzActivity
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.JavaUtil.getBit
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.beans.SensorData
import com.orange.og_lite.callback.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.xor

class OgCommand {

    var NowTag = ""
    var SendTag = ""
    private val rxUUID = "00008D81-0000-1000-8000-00805F9B34FB"
    private val TXUUID = "00008D82-0000-1000-8000-00805F9B34FB"
    companion object{ var tx_memory = StringBuffer()}

    var cancel: Boolean? = false
    fun Send(a: String) {
        Command.rx = ""
        cancel = false
        SendTag = NowTag
        val data = GetCrc(a.toUpperCase())
        var dateStr = ""
        val sdf = SimpleDateFormat("HH:mm:ss:SSS")
        dateStr = sdf.format(Date())
        tx_memory.append("TX:\t").append(dateStr).append(FormatConvert.bytesToHex(data)).append("\n")
        if (tx_memory.toString().length > 40000) {
            tx_memory =
                StringBuffer().append(tx_memory.toString().substring(tx_memory.toString().length - 40000))
        }
        (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.writeHex(FormatConvert.bytesToHex(data),rxUUID,TXUUID)
    }

    fun GetCrcString(a: String): String {
        val command = StringHexToByte(a)
        var xor :Byte= 0
        for (i in 0 until command.size - 2) {
            xor = xor xor command[i]
        }
        command[command.size - 2] = xor.toByte()
        return bytesToHex(command)
    }

    fun GetCrc(a: String): ByteArray {
        val command = StringHexToByte(a)
        var xor :Byte= 0
        for (i in 0 until command.size - 2) {
            xor = xor xor command[i]
        }
        command[command.size - 2] = xor.toByte()
        return command
    }

    fun StringHexToByte(cs: CharSequence): ByteArray {
        val bytes = ByteArray(cs.length / 2)
        for (i in 0 until cs.length / 2)
            bytes[i] = Integer.parseInt(cs.toString().substring(2 * i, 2 * i + 2), 16).toByte()
        return bytes
    }

    fun bytesToHex(hashInBytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in hashInBytes) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }



    fun byte2ToINT(bytes: ByteArray): Int {
        val high = bytes[0].toInt()
        val low = bytes[1].toInt()
        return high shl 8 and 0xFF00 or (low and 0xFF)
    }

    //    public static Read
    fun GetPrId(): ArrayList<SensorData> {
        val array = ArrayList<SensorData>()
        try {
            val replace =
                "0A 10 000E 01 02 LF HEX 00 00 00 00 00 00 00 00 39 F5".replace("HEX", PublicBeans.getHEX())
                    .replace(" ", "").replace("LF", PublicBeans.getLfPower())
            Send(replace)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 1
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A")) {
                    if (fal == 3) {
                        MysqDatabase.InsertMemory(OgCommand.tx_memory.toString(), "5")
                        return array
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(replace)
                    fal += 1
                    JzActivity.getControlInstance().toast("第幾次$fal")
                }
                if (time > 20 || cancel!!) {
                    MysqDatabase.InsertMemory(OgCommand.tx_memory.toString(), "5")
                    if (time > 20) {
                        ReOpen()
                    }
                    return array
                }
                if (Command.rx.length >= 36) {
                    val data = SensorData()
                    val idcount = Integer.parseInt(Command.rx.substring(17, 18))
                    data.id=(Command.rx.substring(8, 16))
                    data.idcount=(idcount)
                    data.bat=(getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(3, 4))
                    data.kpa=(byte2ToINT(StringHexToByte(Command.rx.substring(22, 26)))).toFloat()
                    val bytes = StringHexToByte(Command.rx.substring(18, 22))
                    data.c=(bytes[1] - bytes[0])
                    data.vol=(22 + (StringHexToByte(Command.rx.substring(26, 28))[0] and 0x0F))
                    data.success=(true)
                    array.add(data)
                    if (array.size == ScanCount) {
                        return array
                    } else {
                        if (Command.rx.length > 36) {
                            Command.rx = Command.rx.substring(36)
                        } else {
                            Command.rx = ""
                        }
                    }
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return array
        }

    }

    //    public static Read
    fun GetPr(Lf: String, count: Int, hex: String): ArrayList<SensorData> {
        val response = ArrayList<SensorData>()
        try {
            var co = Integer.toHexString(count)
            while (co.length < 2) {
                co = "0$co"
            }
            Send(
                "0A 10 000E 01 00 LF hex 00 00 00 00 count 00 00 00 39 F5".replace(
                    " ",
                    ""
                ).replace("LF", Lf).replace("count", co).replace("hex", hex)
            )
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 20 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A") || cancel!!) {
                    MysqDatabase.InsertMemory(OgCommand.tx_memory.toString(), "1")
                    if (time > 20) {
                        ReOpen()
                    }
                    return response
                }
                if (Command.rx.length >= 36) {
                    val data = SensorData()
                    val idcount = Integer.parseInt(Command.rx.substring(17, 18))
                    data.idcount=(idcount)
                    data.id=(Command.rx.substring(8, 16))
                    data.bat=(getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(3, 4))
                    data.kpa=(byte2ToINT(StringHexToByte(Command.rx.substring(22, 26)))).toFloat()
                    val bytes = StringHexToByte(Command.rx.substring(18, 22))
                    data.c=(bytes[1] - bytes[0])
                    data.vol=(22 + (StringHexToByte(Command.rx.substring(26, 28))[0] and 0x0F))
                    data.success=(true)
                    response.add(data)
                    Command.rx = Command.rx.substring(36)
                    if (response.size == count) {
                        return response
                    }
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return response
        }

    }

//    0a 10 00 0e 01 00 09 2b 00 00 00 00 00 00 00 00 37 f5
//    0A 10 00 0E 01 00 50 2B 00 00 00 00 00 00 00 00 6E F5 oglite
    //    public static Read
    fun GetId(): SensorData {
        val data = SensorData()
        try {
            Send(
                "0A 10 000E 01 00 LF HEX 00 00 00 00 00 00 00 00 39 F5".replace(
                    "HEX",
                    PublicBeans.getHEX()
                ).replace(" ", "").replace("LF", PublicBeans.getLfPower())
            )
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 15 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A") || cancel!!||Command.rx=="F51C000301EB0A") {
                    data.success=(false)
                    if (time > 15) {
                        ReOpen()
                    }
                    return data
                }
                if (Command.rx.length >= 36) {
                    val idcount = Integer.parseInt(Command.rx.substring(17, 18))
                    data.idcount=(idcount)
                    data.id=(Command.rx.substring(16 - idcount, 16))
                    //                    data.id=Command.rx.substring(8, 16);
                    data.bat=(getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(3, 4))
                    data.kpa=(byte2ToINT(StringHexToByte(Command.rx.substring(22, 26)))).toFloat()
                    val bytes = StringHexToByte(Command.rx.substring(18, 22))
                    data.c=(bytes[1] - bytes[0])
                    data.vol=(22 + (StringHexToByte(Command.rx.substring(26, 28))[0] and 0x0F))
                    data.有無胎溫=(
                        getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                            0,
                            1
                        ) == "1"
                    )
                    data.有無電壓=(
                        getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                            1,
                            2
                        ) == "1"
                    )
                    data.有無電池=(
                        getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                            2,
                            3
                        ) == "1"
                    )
                    data.success=(true)
                    return data
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            data.success=(false)
            return data
        }
    }

    lateinit var P_Callback: Program_C
    fun Program(
        count: String,
        caller: Program_C,
        sensor: ArrayList<String>
    ) {
        try {
            P_Callback = caller
            if (SendTrigerInfo(sensor) && ProgramFirst(PublicBeans.getLfPower(), PublicBeans.getHEX(), count, PublicBeans.getS19File().toString())) {
                caller.Program_Finish(ProgramCheck(spilt))
            } else {
                caller.Program_Finish(false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Program_Finish(false)
        }

    }

    fun SendTrigerInfo(sensor: ArrayList<String>): Boolean {
        try {
            for (i in sensor.indices) {
                var position = Integer.toHexString(i + 1)
                while (position.length < 2) {
                    position = "0$position"
                }
                var id = sensor[i]
                while (id.length < 8) {
                    id = "0$id"
                }
                Send(
                    "0A 15 00 0E position ID 00 00 00 00 00 00 00 18 F5".replace(
                        "position",
                        position
                    ).replace("ID", id).replace(" ", "")
                )
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                val past = sdf.parse(sdf.format(Date()))
                do {
                    val now = sdf.parse(sdf.format(Date()))
                    val time = getDatePoor(now, past)
                    if (time > 20 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A") || cancel!!) {
                        if (time > 20) {
                            ReOpen()
                        }
                        return false
                    }
                } while (Command.rx.length != 36)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    var spilt: String=""
    internal var ScanCount = 0
    fun ProgramFirst(Lf: String, Hex: String, count: String, data: String): Boolean {
        var Hex = Hex
        var count = count
        try {

            while (count.length < 2) {
                count = "0$count"
            }
            while (Hex.length < 2) {
                Hex = "0$Hex"
            }
            val B8 = data.substring(14, 16)
            val B9 = data.substring(16, 18)
            val B12 = data.substring(22, 24)
            val B13 = data.substring(24, 26)
            val Data =
                "0A 10 00 0E  02 CT  Lf Hex 8b 9b 12b 13b 00 00 00 00 ff f5".replace("CT", count)
                    .replace("Lf", Lf).replace("Hex", Hex)
                    .replace("8b", B8).replace("9b", B9).replace("12b", B12).replace("13b", B13)
                    .replace(" ", "")
            Send(Data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 1
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A")) {
                    if (fal == 3) {
                        MysqDatabase.InsertMemory(OgCommand.tx_memory.toString(), "2")
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(Data)
                    fal += 1
                    JzActivity.getControlInstance().toast("第幾次$fal")
                }
                if (time > 20 || cancel!!) {
                    MysqDatabase.InsertMemory(OgCommand.tx_memory.toString(), "2")
                    if (time > 20) {
                        ReOpen()
                    }
                    return false
                }
                if (Command.rx.length >= 36) {
                    ScanCount = Integer.parseInt(Command.rx.substring(9, 10))
                    spilt = if (Command.rx.substring(10, 12) == "04") data.substring(
                        0,
                        2048 * 2
                    ) else data.substring(0, 6144 * 2)
                    return WriteFlash(spilt)
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun ReOpen() {
        try {
            Log.e("DATA:", "逾時")

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun WriteFlash(data: String): Boolean {
        try {
            val count = if (data.length % 400 == 0) data.length / 400 else data.length / 400 + 1
            for (i in 0 until count) {
                if (i == count - 1) {
                    P_Callback.Program_Progress(100)
                    if (!CheckData(data.substring(400 * i), Integer.toHexString(i + 1))) {
                        return false
                    }
                } else {
                    P_Callback.Program_Progress(i * 100 / count)
                    if (!CheckData(
                            data.substring(400 * i, 400 * i + 400),
                            Integer.toHexString(i + 1)
                        )
                    ) {
                        return false
                    }
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun CheckData(data: String, place: String): Boolean {
        var place = place
        try {
            while (place.length < 2) {
                place = "0$place"
            }
            val Long =
                if (data.length == 400) "00CB" else "00" + Integer.toHexString(data.length / 2 + 3)
            val command = "0A 13 LONG DATA PLACE FF F5".replace(" ", "").replace("LONG", Long)
                .replace("DATA", data).replace("PLACE", place)
            Send(command)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            val fal = 0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 6 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A") || cancel!!) {
                    if (time > 6) {
                        MysqDatabase.InsertMemory(OgCommand.tx_memory.toString(), "3")
                        ReOpen()
                    }
                    return false
                }
                if (Command.rx.length >= 36) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun ProgramCheck(data: String): Boolean {
        try {
            Send("0A 14 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 ff f5".replace(" ", ""))
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var fal = 0
            var past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 15 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A") || fal == 10 || cancel!!) {
                    MysqDatabase.InsertMemory(OgCommand.tx_memory.toString(), "4")
                    if (time > 15) {
                        ReOpen()
                    }
                    return false
                }
                if (Command.rx.length >= 36 && Command.rx.contains("F513000E00")) {
                    val check = Command.rx.substring(12, 20)
                    if (check == "7FFFFFFF" || check == "000007FF") {
                        Log.e("燒錄","燒錄成功")
                        return true
                    } else {
                        if (!RePr(getBit(check).substring(1), data)) {
                            return false
                        }
                        past = sdf.parse(sdf.format(Date()))
                        fal+=1
                    }
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun RePr(b: String?, data: String): Boolean {
        var b = b
        b = reverseBySort(b)
        Log.d("DATA::", "失敗" + b!!)
        val count = if (data.length % 400 == 0) data.length / 400 else data.length / 400 + 1
        for (i in 0 until count) {
            P_Callback.Program_Progress(i * 100 / count)
            if (b[i].toString() != "1") {
                if (i == count - 1) {
                    if (!CheckData(data.substring(400 * i), Integer.toHexString(i + 1))) {
                        return false
                    }
                } else {
                    if (!CheckData(
                            data.substring(400 * i, 400 * i + 400),
                            Integer.toHexString(i + 1)
                        )
                    ) {
                        return false
                    }
                }
            }
        }
        Send("0A 14 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 ff f5".replace(" ", ""))
        return true
    }

    fun reboot(): Boolean {
        try {
            val data = "0A0D00030000F5"
            Send(data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 20 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A")) {
                    if (time > 20) {
                        ReOpen()
                    }
                    return false
                }
                if (Command.rx.length == 14) {
                    return true
                }
                Thread.sleep(1000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun GetVerion(caller: Version_C) {
        try {
            val data = "0A0A000EFFFFFFFFFFFFFFFFFFFFFFFF00F5"
            Send(data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 15 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A")) {
                    if (time > 15) {
                        ReOpen()
                    }
                    caller.version("", false)
                    return
                }
                if (Command.rx.length >= 36) {
                    caller.version(Command.rx.substring(8, 16), true)
                    return
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun WriteBootloader(act: JzActivity, Ind: Int, filename: String, caller: Update_C) {
        try {
            //            FileInputStream fo=new FileInputStream(context.getApplicationContext().getFilesDir().getPath()+"/"+filename+".s2");
            val fr = InputStreamReader(
                if (filename == "no") act.assets.open("update.x2") else FileInputStream(act.applicationContext.filesDir.path + "/update.x2")
            )
            val br = BufferedReader(fr)
            val sb = StringBuilder()
            while (br.ready()) {
                var s = br.readLine()
                s = s.replace("null", "")
                sb.append(s)
            }
            var Long = 0
            if (sb.length % Ind == 0) {
                Long = sb.length / Ind
            } else {
                Long = sb.length / Ind + 1
            }
            Log.d("總行數", "" + Long)
            for (i in 0 until Long) {
                if (i == Long - 1) {
                    Log.d("行數", "" + i)
                    val data = bytesToHex(sb.substring(i * Ind, sb.length).toByteArray())
                    val length = Ind + 2
                    check(Convvvert(data, Integer.toHexString(length)))
                    caller.Updateing(100)
                    caller.Finish(true)
                } else {
                    val data = bytesToHex(sb.substring(i * Ind, i * Ind + Ind).toByteArray())
                    Log.d("行數", "" + i)
                    val length = Ind + 2
                    caller.Updateing(i * 100 / Long)
                    if (!check(Convvvert(data, Integer.toHexString(length)))) {
                        caller.Finish(false)
                    }
                }
            }
            fr.close()
            caller.Finish(true)
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Finish(false)
        }

    }

    fun Convvvert(data: String, length: String): String {
        var length = length
        var command = "0A02LX00F5"
        while (length.length < 4) {
            length = "0$length"
        }
        command = command.replace("L", length).replace("X", data)
        return command
    }

    fun check(data: String): Boolean {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            Send(data)
            while (fal < 5) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 2) {
                    past = sdf.parse(sdf.format(Date()))
                    Send(data)
                    fal+=1
                }
                if (Command.rx.length >= 14 && Command.rx == GetCrcString("F502000300F40A") || Command.rx == GetCrcString("F50B000301F70A")) {
                    return true
                }
                Thread.sleep(100)
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun GetHard() {
        try {
            val data = "0A0C000EFFFFFFFFFFFFFFFFFFFFFFFF00F5"
            Send(data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 15 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A")) {
                    if (time > 15) {
                        ReOpen()
                    }
                    return
                }
                if (Command.rx.length >= 14) {
                    //                    if(Command.rx.contains(GetCrcString("F500000302F40A"))){caller.result(2);}
                    //                    if(Command.rx.contains(GetCrcString("F500000301F40A"))){caller.result(1);}
                    return
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            //        caller.result(-1);
        }

    }

    fun HandShake(caller: Hanshake_C) {
        try {
            val data = "0A0000030000F5"
            Send(data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 15 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A")) {
                    if (time > 15) {
                        ReOpen()
                    }
                    caller.result(-1)
                    return
                }
                if (Command.rx.length >= 14) {
                    if (Command.rx.contains(GetCrcString("F500000302F40A"))) {
                        caller.result(2)
                        return
                    }
                    if (Command.rx.contains(GetCrcString("F500000301F40A"))) {
                        caller.result(1)
                        return
                    }
                    if (Command.rx.contains(GetCrcString("F501000300F70A"))) {
                        caller.result(1)
                        return
                    }
                    caller.result(-1)
                    return
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            caller.result(-1)
        }

    }

    //01-03 11:12:43.743 10851-10890/com.orange.homescreem E/DATA:: RX： f5 20 00 0e c4 e9 12 61 00 00 00 00 00 00 00 00 85 0a
    //01-03 11:12:43.860 10851-10890/com.orange.homescreem E/DATA:: RX： f5 20 00 0e c4 dd c0 8b 00 00 00 00 00 00 00 00 89 0a
    //01-03 11:12:45.576 10851-10890/com.orange.homescreem E/DATA:: RX： f5 20 00 0e 60 dd c1 8e 00 00 00 00 00 00 00 00 29 0a
    //01-03 11:12:45.923 10851-10890/com.orange.homescreem E/DATA:: RX： f5 20 00 0e 60 dd db 46 00 00 00 00 00 00 00 00 fb 0a
    fun IdCopy(caller: Copy_C, _long: Int,obd: ObdBeans) {
        var hex = PublicBeans.getHEX()
        try {
            while (hex.length < 2) {
                hex = "0$hex"
            }
            for (i in 0 until obd.rowcount) {
                if(!obd.readable[i]){continue}
                var Original_ID = obd.OldSemsor[i]
                while (Original_ID.length < 8) {
                    Original_ID = "0$Original_ID"
                }
                var New_ID = obd.NewSensor[i]
                while (New_ID.length < 8) {
                    New_ID = "0$New_ID"
                }
                val data =
                    "0A 11 00 0E Original_ID Original_Long New_ID New_Long hex 00 ff f5".replace(
                        " ",
                        ""
                    ).replace(
                        "Original_Long",
                        "0$_long"
                    )
                        .replace("New_Long", "0$_long").replace("Original_ID", Original_ID)
                        .replace("New_ID", New_ID).replace("hex", hex)
                Log.e("DATA:", "Prepare:$data")
                Send(data)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                val fal = 0
                val past = sdf.parse(sdf.format(Date()))
                while (true) {
                    val now = sdf.parse(sdf.format(Date()))
                    val time = getDatePoor(now, past)
                    if (time > 15 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A") || cancel!!) {
                        if (time > 15) {
                            ReOpen()
                            caller.Copy_Finish(false)
                            return
                        }
                        if (SendTag == NowTag) {
                            caller.Copy_Next(false, i)
                        }
                        break
                    }
                    if (Command.rx.length >= 36) {
                        val idcount = Integer.parseInt(Command.rx.substring(17, 18))
                        if (Command.rx.contains(obd.OldSemsor[i].substring(8 - idcount))) {
                                obd.state[i]=ObdBeans.PROGRAM_SUCCESS
                                caller.Copy_Next(true, i)
                        } else {
                                obd.state[i]=ObdBeans.PROGRAM_FALSE
                                caller.Copy_Next(false, i)

                        }
                        break
                    }
                    Thread.sleep(100)
                }
                Thread.sleep(1000)
            }
            if (SendTag == NowTag) {
                caller.Copy_Finish(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Copy_Finish(false)
        }

    }

    fun reverseBySort(str: String?): String? {
        if (str == null || str.length == 1) {
            return null
        }
        val sb = StringBuffer()
        for (i in str.length - 1 downTo 0) {
            sb.append(str[i])//使用StringBuffer從右往左拼接字元
        }
        return sb.toString()
    }

    fun getDatePoor(endDate: Date, nowDate: Date): Double {
        val diff = endDate.time - nowDate.time
        val sec = diff / 1000
        return (if (SendTag == NowTag) sec else 16).toDouble()
    }
}