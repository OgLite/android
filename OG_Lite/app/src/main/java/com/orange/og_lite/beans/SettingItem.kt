package com.orange.og_lite.beans

import androidx.fragment.app.Fragment

class SettingItem{
    var item=ArrayList<Int>()
    var item2=ArrayList<String>()
    var item3=ArrayList<String>()
    var item3_view=ArrayList<Int>()
    var change=ArrayList<Fragment?>()
    var tag=ArrayList<String>()
    //var item3=ArrayList<Float>()
    fun add(a:Int,b:String,c:String,d:Int,change:Fragment?,tag:String){
        item.add(a);
        item2.add(b);
        item3.add(c)
        item3_view.add(d)
        this.change.add(change)
        this.tag.add(tag)
        //item3.add(c);
    }
    fun clear(){
        item.clear()
        item2.clear()
        item3.clear()
        item3_view.clear()
        tag.clear()
        change.clear()
    }
}