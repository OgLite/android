package com.orange.og_lite.beans

import android.widget.TextView
import java.util.ArrayList

class ObdBeans {
    var rowcount = 6
    var idcount = 8
    var Tire_img = ArrayList<TextView>()
    var OldSemsor = ArrayList<String>()
    var NewSensor = ArrayList<String>()
    var state = ArrayList<Int>()
    var readable= arrayOf(false,false,false,false,false)
    var needPosition=true
    var CanEdit = false
    fun add(OldSemsor: String, NewSensor: String, state: Int) {
        this.OldSemsor.add(OldSemsor)
        this.NewSensor.add(NewSensor)
        this.state.add(state)
    }

    fun replaceOldSensor(a:String,b:String){
        for(i in 0 until NewSensor.size){
            if(NewSensor[i]==a){
                NewSensor[i]=b
            }
        }
    }

    fun clear() {
        OldSemsor.clear()
        NewSensor.clear()
        state.clear()
    }

    fun getReadable():Int{
        var i=0
        for(a in readable){
            if(a){i++}
        }
        return i
    }
    fun getOldSensor():ArrayList<String>{
        val i=ArrayList<String>()
        for(a in readable.indices){
            if(readable[a]){i.add(OldSemsor[a])}
        }
        return i
    }

    fun getNewSensorReader():ArrayList<String>{
        val i=ArrayList<String>()
        for(a in readable.indices){
            if(readable[a]){i.add(NewSensor[a])}
        }
        return i
    }

    companion object {
        var PROGRAM_WAIT = 2
        var PROGRAM_SUCCESS = 0
        var PROGRAM_FALSE = 1
    }
}
