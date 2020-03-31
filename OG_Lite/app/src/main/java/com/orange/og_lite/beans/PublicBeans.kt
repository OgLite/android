package com.orange.og_lite.beans

import android.content.res.AssetManager
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.og_lite.Frag.Frag_Function_QrcodeScanner
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.Frag.scanback
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList
enum class BootloaderState{
    Bootloader,Og_App,Obd_App
}
class PublicBeans {
    interface sensorBack {
        fun callback(content: String)
    }

    companion object {

        //---------------------------------------------------------------------->OG用<---------------------------------------------------------------------------------------------------------
        var programNumber = 0
        var Update = false
        //---------------------------------------------------------------------->OBD用<---------------------------------------------------------------------------------------------------------

        var DongleState=BootloaderState.Bootloader
        var position = 0;
        val OBD複製 = 1;
        val OBD學碼 = 2;
        val 燒錄 = 3;
        val 複製ID = 4;
        val 讀取 = 5;
        val 線上訂購=6
        var selectWay = 0
        var SCAN = 1
        var READ = 2
        var KEY_IN = 3
        var make = ""
        var model = ""
        val admin: String
            get() {
                return JzActivity.getControlInstance().getPro("admin", "nodata")
            }
        val password: String
            get() {
                return JzActivity.getControlInstance().getPro("password", "nodata")
            }
        var year = ""
        var serial = ""
        var obdappversion = ""
        //---------------------------------------------------------------------->公用<---------------------------------------------------------------------------------------------------------
        //取得OBD燒錄檔
        fun getOBD1(): String? {
//            var data: String = ""
//            val act = JzActivity.getControlInstance().getRootActivity() as MainActivity
//            act.item.query(
//                "select `OBD1` from `Summary table` where Make='$make' and Model='$model' and year='$year' and `OBD1` not in('NA') limit 0,1",
//                Sql_Result {
//                    data = it.getString(0)
//                })
            val input = JzActivity.getControlInstance().getRootActivity().assets.open("test.srec")
            val reader = BufferedReader(InputStreamReader(input, "utf-8"))
            var line: String? = reader.readLine()
            val strBuf = StringBuffer()
            while (line  != null) {
                strBuf.append(line)
                line = reader.readLine()
            }
            return strBuf.toString()
//            return getFile(data)
        }

        //取得OBD名稱
        fun getOBDName(): String? {
            var data: String = ""
            val act = JzActivity.getControlInstance().getRootActivity() as MainActivity
            act.item.query(
                "select `OBD1` from `Summary table` where Make='$make' and Model='$model' and year='$year' and `OBD1` not in('NA') limit 0,1",
                Sql_Result {
                    data = it.getString(0)
                })
            return data
        }

        //取得現有OBD版本號
        fun getOBDVersion(): String? {
            var data: String = ""
            val act = JzActivity.getControlInstance().getRootActivity() as MainActivity
            act.item.query(
                "select `OBD1` from `Summary table` where Make='$make' and Model='$model' and year='$year' and `OBD1` not in('NA') limit 0,1",
                Sql_Result {
                    data = it.getString(0)
                })
            return JzActivity.getControlInstance().getPro("obd$data", "nodata").replace(".srec", "")
        }

        //取輪胎數量
        fun wheelCount(): Int {
            val act = JzActivity.getControlInstance().getRootActivity() as MainActivity
            var wheelcount = 4
            act.item.query(
                "select  `Wheel_Count`  from `Summary table` where Make='$make' and Model='$model' and year='$year' limit 0,1",
                Sql_Result {
                    //Callback回調，會迴圈跑到所有資料載入完
                    val result1 = it.getString(0)
                    wheelcount = result1.toInt()
                });
            return wheelcount
        }

        //取得sensorid
        fun getSensor(callback: sensorBack) {
            JzActivity.getControlInstance().changeFrag(
                Frag_Function_QrcodeScanner(
                    object :
                        scanback {
                        override fun callback(content: String) {
                            if (!content.contains(":") && !content.contains("*")) {
                                if (content != "nofound") {
                                    JzActivity.getControlInstance()
                                        .toast(R.string.app_invalid_sensor_qrcode)
                                } else {
                                    JzActivity.getControlInstance()
                                        .toast(R.string.app_scan_code_timeout)
                                }
                                return
                            }
                            var sensorid = ""
                            if (content.contains("**")) {
                                JzActivity.getControlInstance()
                                    .toast(R.string.app_invalid_sensor_qrcode)
                            } else {
                                if (content.split(":", "*").size >= 3) {
                                    sensorid = content.split(":", "*")[1]
                                }
                            }
                            if (sensorid != "") {
                                callback.callback(sensorid)
                            }
                        }
                    },
                    1
                ), R.id.frage, "Frag_Function_QrcodeScanner", true
            )
        }

        //取得file()
        fun getFile(a: String): String? {
            var data: String? = null
            val act = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item_File
            act.query("select data from file where ditectfit='$a'", Sql_Result {
                data = it.getString(0)
            })
            return data
        }

        //插入file()
        fun insertFile(a: String, b: String) {
            val act = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item_File
            act.exsql("insert or replace into file (ditectfit,data) values ('$a','$b')")

        }
        //取得S19
        fun getS19(): String {
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            var name = ""
            item.query("select  `Direct Fit` from `Summary table` " +
                    "where Make='$make' and Model='$model' and year='$year'  and  `Direct Fit` not in('NA') limit 0,1",
                Sql_Result {
                    name = it.getString(0)
                }
            )
            return name
        }
        //取得getOePart
        fun getOePart(): String {
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            var name = ""
            item.query(
                "select `OE Part Num` from `Summary table`   where `Direct Fit`='${getS19()}' limit 0,1",
                Sql_Result {
                    name = it.getString(0)
                })
            return name
        }
        //取得id數量
        fun getIdcount(): Int {
            var int = 8
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            val result = item.query(
                "select `ID_Count` from `Summary table` where `Direct Fit`='${getS19()}' limit 0,1",
                Sql_Result {
                    int = it.getString(0).toInt()
                })
            return int
        }
        //取得SencsorModel
        fun SencsorModel(): String {
            var a = "SP201"
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            val result = item.query(
                "select `Sensor` from `Summary table` where  `Direct Fit`='${getS19()}'",
                Sql_Result {
                    a = it.getString(0)
                })
            return a
        }
        //取得Lf
        fun getLf(): String? {
            var a = "0"
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            val result = item.query(
                "select `Lf` from `Summary table` where  `Direct Fit`='${getS19()}'",
                Sql_Result {
                    a = it.getString(0)
                })
            return a
        }

        //取得S19File
        fun getS19File(): String? {
            var data: String = ""
            val act = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item_File
            act.query("select data from file where ditectfit='${getS19()}'", Sql_Result {
                data = it.getString(0)
            })
            return data
        }
        //取得Lf_Power
        fun getLfPower():String{
            var data: String = "00"
            val act = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            act.query("select  `LF Power` from `Summary table` " +
                    "where Make='$make' and Model='$model' and year='$year'  and  `Direct Fit` not in('NA') limit 0,1", Sql_Result {
                data = it.getString(0)
            })
            return data
        }
        //取得HEX
        fun getHEX():String{
            var data: String = "00"
            val act = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            act.query("select  `OBD Product No. (hex)` from `Summary table`  " +
                    "where Make='$make' and Model='$model' and year='$year'  and  `Direct Fit` not in('NA') limit 0,1", Sql_Result {
                data = it.getString(0)

            })
            if(data.length==4){data= data.substring(2,4)}else{return "00"}
            return data
        }
    }
}