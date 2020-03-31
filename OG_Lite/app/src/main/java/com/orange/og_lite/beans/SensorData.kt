package com.orange.og_lite.beans

import com.orange.jzchi.jzframework.JzActivity

class SensorData {
    var id = ""
    var kpa = 0F
        get(){
            when(JzActivity.getControlInstance().getPro("Pre",2)){
                0->{
                    return ((field*0.145037738F).toInt()).toFloat()
                }
                2->{
                    return field
                }
                1->{
                    return ((field*0.01).toInt()).toFloat()
                }
            }
            return field
        }
    var c =0
        get(){
            when(JzActivity.getControlInstance().getPro("Tem",0)){
                0->{
return field
                }
                1->{
                    return (field*9/5)+32
                }
            }
            return field}
    var idcount = 0
    var bat = ""
    var vol = 0
    var success = false
    var 有無胎溫 = false
    var 有無電池 = false
    var 有無電壓 = false


companion object{
    fun getTem():String{
        when(JzActivity.getControlInstance().getPro("Tem",0)){
            0->{return "C:"}
            1->{return "F:"}
        }
        return "C:"
    }
    fun getPre():String{
        when(JzActivity.getControlInstance().getPro("Pre",2)){
            0->{return "Psi:"}
            1->{return "Bar:"}
            2->{return "Kpa:"}
        }
        return "Kpa:"
    }
}
}
