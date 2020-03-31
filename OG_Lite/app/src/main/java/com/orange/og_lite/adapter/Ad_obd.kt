package com.orange.og_lite.adapter

import android.text.InputFilter
import android.view.View
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.Command
import com.orange.og_lite.Frag.Frag_Function_OBD_ID_copy
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.textchange
import kotlinx.android.synthetic.main.adapter_ad_function_show_read.view.*

class Ad_obd(public val beans: ObdBeans,var frag: Frag_Function_OBD_ID_copy) : JzAdapter(R.layout.adapter_ad_function_show_read) {

    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    override fun sizeInit(): Int {
        return  beans.rowcount
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false);
        holder.mView.Vehice_ID.visibility=View.VISIBLE
        holder.mView.New_ID.visibility=View.VISIBLE
        when(PublicBeans.position){
            PublicBeans.OBD學碼->{
                holder.mView.Vehice_ID.isEnabled=false
                if(beans.readable[position]){
                    holder.mView.New_ID.setBackgroundResource(if(beans.NewSensor[position].isEmpty()) R.color.green else R.color.white)
                    holder.mView.Vehice_ID.setBackgroundResource(if(beans.OldSemsor[position].isEmpty()) R.color.color_grey else R.color.white)
                }else{
                    holder.mView.Vehice_ID.setBackgroundResource(if(beans.OldSemsor[position].isEmpty()) R.color.color_grey else R.color.white)
                    if(!beans.needPosition){
                        holder.mView.New_ID.setBackgroundResource(R.color.color_grey)
                    }
                }
            }
            PublicBeans.OBD複製->{
                holder.mView.Vehice_ID.isEnabled=false
                if(beans.readable[position]){holder.mView.New_ID.setBackgroundResource(if(beans.NewSensor[position].isEmpty()) R.color.green else R.color.white)}else{
                    if(!beans.needPosition){holder.mView.New_ID.setBackgroundResource(R.color.color_grey)}
                }
                holder.mView.Vehice_ID.setBackground(holder.mView.context.resources.getDrawable(R.color.white))
            }
            PublicBeans.複製ID->{
                holder.mView.Vehice_ID.isEnabled=false
                if(beans.readable[position]){holder.mView.New_ID.setBackgroundResource(if(beans.NewSensor[position].isEmpty()) R.color.green else R.color.white)
                    holder.mView.Vehice_ID.setBackgroundResource(if(beans.OldSemsor[position].isEmpty()) R.color.green else R.color.white)}else{
                    if(!beans.needPosition){holder.mView.New_ID.setBackgroundResource(R.color.color_grey)
                        holder.mView.Vehice_ID.setBackgroundResource(R.color.color_grey)
                    }
                } }
        }
        holder.mView.Tire_check.setVisibility(View.VISIBLE)
        holder.mView.New_ID.isEnabled=beans.CanEdit
        holder.mView.Tire_check.setVisibility(View.GONE)
        when (position) {
            0 -> {
                holder.mView.Tire_text.text = mMainActivity.getString(R.string.app_fl)
            }
            1 -> {
                holder.mView.Tire_text.text = mMainActivity.getString(R.string.app_fr)
            }
            2 -> {
                holder.mView.Tire_text.text = mMainActivity.getString(R.string.app_rr)
            }
            3 -> {
                holder.mView.Tire_text.text = mMainActivity.getString(R.string.app_rl)
            }
            4 -> {
                holder.mView.Tire_text.text = "SP"
            }
        }
        holder.mView.Vehice_ID.setText(beans.OldSemsor[position])
        holder.mView.New_ID.setText(beans.NewSensor[position])
        holder.mView.Vehice_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_black))
        holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_black))
        when (beans.state[position]) {
            ObdBeans.PROGRAM_FALSE -> {
                beans.Tire_img[position].setBackgroundResource(R.mipmap.img_wheel_fail)
                holder.mView.Tire_check.setVisibility(View.VISIBLE)
                holder.mView.Tire_check.setImageResource(R.mipmap.icon_check_sensor_fail)
                when(PublicBeans.position){
                    PublicBeans.OBD複製->{holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_orange))}
                    PublicBeans.OBD學碼->{holder.mView.Vehice_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_orange))}
                    PublicBeans.複製ID->{holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_orange))}
                }
            }
            ObdBeans.PROGRAM_WAIT -> {
                if(beans.readable[position]){
                    beans.Tire_img[position].setBackgroundResource(R.mipmap.img_wheel_n)
                }else{
                    beans.Tire_img[position].setBackgroundResource(R.mipmap.icon_tire_cancel)
                }
                holder.mView.Tire_check.setVisibility(View.VISIBLE)
                holder.mView.Tire_check.setImageResource(R.color.white)
                when(PublicBeans.position){
                    PublicBeans.OBD複製->{holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_orange))}
                    PublicBeans.OBD學碼->{holder.mView.Vehice_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_orange))}
                    PublicBeans.複製ID->{holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_orange))}
                }
            }
            ObdBeans.PROGRAM_SUCCESS -> {
                beans.Tire_img[position].setBackgroundResource(R.mipmap.img_tirel_ok)
                holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_black))
                holder.mView.Tire_check.visibility = View.VISIBLE
                holder.mView.Tire_check.setImageResource(R.mipmap.icon_check_sensor_ok)
                holder.mView.Vehice_ID.setTextColor(holder.itemView.context.resources.getColor(R.color.color_black));
                holder.mView.New_ID.setTextColor(holder.itemView.context.resources.getColor(R.color.color_black));
            }
        }
        holder.mView.New_ID.textchange= textchange {
            frag.checkComplete()
            beans.NewSensor[position]=holder.mView.New_ID.text.toString()
            holder.mView.New_ID.setBackgroundResource(if(beans.readable[position]&&beans.NewSensor[position].isEmpty()) R.color.green else R.color.white)
        }
        holder.mView.Vehice_ID.textchange= textchange {
            frag.checkComplete()
            beans.OldSemsor[position]=holder.mView.Vehice_ID.text.toString()
            holder.mView.Vehice_ID.setBackgroundResource(if(beans.readable[position]&&beans.OldSemsor[position].isEmpty()) R.color.green else R.color.white)
        }
        if(beans.needPosition){

            holder.mView.New_ID.isFocusable=false
            holder.mView.New_ID.isEnabled=true
            holder.mView.Vehice_ID.isFocusable=false
            holder.mView.Vehice_ID.isEnabled=true
            holder.mView.New_ID.setOnClickListener {
                beans.readable[position]=!beans.readable[position]
                notifyDataSetChanged()
            }
            if(PublicBeans.position==PublicBeans.複製ID){
                holder.mView.Vehice_ID.setOnClickListener {
                    beans.readable[position]=!beans.readable[position]
                    notifyDataSetChanged()
                }
            }
        }else{
            if(PublicBeans.selectWay==PublicBeans.KEY_IN){
                holder.mView.New_ID.isFocusable=beans.readable[position]
                holder.mView.New_ID.isEnabled=beans.readable[position]
                if(PublicBeans.position==PublicBeans.複製ID){
                    holder.mView.Vehice_ID.isFocusable=beans.readable[position]
                    holder.mView.Vehice_ID.isEnabled=beans.readable[position]
                    holder.mView.Vehice_ID.setTextIsSelectable(beans.readable[position])
                }
                holder.mView.New_ID.setTextIsSelectable(beans.readable[position])
            }else{
                holder.mView.New_ID.isFocusable=false
                holder.mView.New_ID.isEnabled=true
                holder.mView.Vehice_ID.isFocusable=false
                holder.mView.Vehice_ID.isEnabled=true
                if(PublicBeans.position==PublicBeans.複製ID){holder.mView.Vehice_ID.setTextIsSelectable(beans.readable[position])}
                holder.mView.New_ID.setTextIsSelectable(false)
                holder.mView.Vehice_ID.setTextIsSelectable(false)
            }
            holder.mView.New_ID.setOnClickListener {
                if(beans.readable[position]&&PublicBeans.selectWay==PublicBeans.SCAN){
                    PublicBeans.getSensor(object : PublicBeans.sensorBack {
                        override fun callback(content: String) {
                            holder.mView.New_ID.setText(content)
                        }
                    })
                }
            }
            holder.mView.Vehice_ID.setOnClickListener{
                if(beans.readable[position]&&PublicBeans.selectWay==PublicBeans.SCAN){
                    PublicBeans.getSensor(object : PublicBeans.sensorBack {
                        override fun callback(content: String) {
                            holder.mView.Vehice_ID.setText(content)
                        }
                    })
                }
            }
        }
        holder.mView.New_ID.filters= arrayOf<InputFilter>(InputFilter.LengthFilter(beans.idcount))
        holder.mView.Vehice_ID.filters= arrayOf<InputFilter>(InputFilter.LengthFilter(beans.idcount))
    }


}