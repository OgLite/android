package com.orange.og_lite.beans

class Function_Enter_Sensor_ID_Item
{
    companion object{
        var 選擇讀ID感測器=0
        var Scan_Code=1
        var Sensor_Read=2
        var Key_In=3
    }

    var item = ArrayList<Int>()
    var item2 = ArrayList<String>()
    var item3 = ArrayList<Int>()
    var item4 = ArrayList<String>()
    var condition = ArrayList<Int>()

    fun add(a:Int,b:String,c:Int,d:String,e:Int)
    {
        item.add(a)
        item2.add(b)
        item3.add(c)
        item4.add(d)
        condition.add(e)
    }
}