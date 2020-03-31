package com.orange.og_lite.adapter

import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.R
import com.orange.og_lite.beans.Function_Check_Sensor_Item
import kotlinx.android.synthetic.main.adapter_ad_function_check_sensor_read.view.*

class Ad_Function_Check_Sensor(val myitem: Function_Check_Sensor_Item):JzAdapter(R.layout.adapter_ad_function_check_sensor_read)
{
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.ID_text.text = myitem.item[position]
        holder.mView.ID.text = myitem.item2[position]
    }

    override fun sizeInit(): Int {
        return myitem.item.size
    }

}