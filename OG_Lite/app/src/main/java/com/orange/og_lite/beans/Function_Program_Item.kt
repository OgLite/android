package com.orange.og_lite.beans

import android.widget.ImageView

class Function_Program_Item
{
    var rowcount = 6
    var idcount = 8
    var readable= arrayOf(false,false,false,false,false)

    var programId = ArrayList<String>()
    var state = ArrayList<Int>()

    fun add(b:String, state: Int)
    {
        programId.add(b)
        this.state.add(state)
    }

    companion object {
        var PROGRAM_WAIT = 2
        var PROGRAM_SUCCESS = 0
        var PROGRAM_FALSE = 1
    }
}