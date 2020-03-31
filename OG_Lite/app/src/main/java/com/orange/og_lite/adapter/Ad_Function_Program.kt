package com.orange.og_lite.adapter

import android.text.InputFilter
import android.view.View
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.R
import com.orange.og_lite.beans.Function_Program_Item
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.textchange
import kotlinx.android.synthetic.main.adapter_ad_function_program.view.*

class Ad_Function_Program(val myitem: Function_Program_Item) :
    JzAdapter(R.layout.adapter_ad_function_program) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        holder.mView.Tire_text.text = "${position + 1}"
        holder.mView.Tire_new_id.setText(myitem.programId[position])
        //holder.mView.Tire_check.setBackgroundResource(myitem.item3[position])

        holder.mView.Tire_new_id.textchange = textchange {
            myitem.programId[position] = holder.mView.Tire_new_id.text.toString()
        }

        holder.mView.Tire_new_id.isFocusable = PublicBeans.selectWay == PublicBeans.KEY_IN

        when (myitem.state[position]) {
            Function_Program_Item.PROGRAM_WAIT -> {
                holder.mView.Tire_check.setVisibility(View.VISIBLE)
                holder.mView.Tire_check.setImageResource(R.color.white)
            }
            Function_Program_Item.PROGRAM_SUCCESS -> {
                holder.mView.Tire_check.visibility = View.VISIBLE
                holder.mView.Tire_check.setImageResource(R.mipmap.icon_check_sensor_ok)
            }
            Function_Program_Item.PROGRAM_FALSE -> {
                holder.mView.Tire_check.setVisibility(View.VISIBLE)
                holder.mView.Tire_check.setImageResource(R.mipmap.icon_check_sensor_fail)
            }
        }

        holder.mView.Tire_new_id.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(myitem.idcount))
        //holder.mView.Vehice_ID.filters= arrayOf<InputFilter>(InputFilter.LengthFilter(beans.idcount))
    }

    override fun sizeInit(): Int {
        return myitem.rowcount
    }

}