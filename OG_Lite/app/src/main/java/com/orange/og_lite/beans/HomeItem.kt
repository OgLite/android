package com.orange.og_lite.beans

import androidx.fragment.app.Fragment

class HomeItem{

    var item=ArrayList<Int>()
    var item2=ArrayList<String>()
    var item3=ArrayList<Float>()
    var change=ArrayList<Fragment?>()
    var tag=ArrayList<String>()
    var changePlace= ArrayList<Int>()

    fun add(a:Int,b:String,c:Float,change:Fragment?,tag:String,changePlace:Int){
        //super.add
        item.add(a);
        item2.add(b);
        item3.add(c);
        this.change.add(change)
        this.tag.add(tag)
        this.changePlace.add(changePlace)
    }
}