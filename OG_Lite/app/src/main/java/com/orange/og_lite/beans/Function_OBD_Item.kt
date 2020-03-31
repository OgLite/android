package com.orange.og_lite.beans

class Function_OBD_Item {
    companion object{
        var 燒錄成功=0
        var 燒錄失敗=1
        var 未燒錄=2
    }
    var item=ArrayList<String>()
    var Original= arrayOf("","","","","","")
    var readID= arrayOf("","","","","","")
    var readable= arrayOf(false,false,false,false,false)
    var condition=ArrayList<Int>()
    fun add(a:String,e:Int){
        //super.add
        item.add(a);
        condition.add(e)
    }
}