package com.orange.og_lite.adapter

import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.*
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.beans.SettingItem
import kotlinx.android.synthetic.main.adapter_ad_setting.view.*

class Ad_Setting(val myitem: SettingItem) : JzAdapter(R.layout.adapter_ad_setting)
{
    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mView.imageView6.setBackgroundResource(myitem.item[position])

        holder.mView.textView17.text = myitem.item2[position]

        holder.mView.textView37.text = myitem.item3[position]
        holder.mView.textView37.visibility = myitem.item3_view[position]

        if(myitem.change[position] != null) {
            holder.mView.Setting_R.setOnClickListener {
                //Frag_SettingPager_Set_Languages.changePlace = 1
                if(myitem.tag[position]==("Frag_Function_QrcodeScanner")||myitem.tag[position]==("Frag_SelectMmyPage_Make")||
                    myitem.tag[position]==("Frag_SelectMmyPage_MyFavorite")||myitem.tag[position]==("Frag_Function_Detecting")){
                    if (!mMainActivity.manager.Ble_Helper.isConnect() || !mMainActivity.manager.Ble_Helper.bleadapter.isEnabled){
                        JzActivity.getControlInstance().showDiaLog(R.layout.activity_scan_ble, false, false,
                            Da_Scan_ble(object :connectBack{
                                override fun connec() {
                                    JzActivity.getControlInstance().changeFrag(myitem.change[position]!!,R.id.frage,myitem.tag[position],true)
                                }
                            }), "Da_Scan_ble")
                        mMainActivity.manager.Ble_Helper.openBle()
                        mMainActivity.manager.Ble_Helper.startScan()
                    }else{
                        JzActivity.getControlInstance()
                            .changeFrag(myitem.change[position]!!, R.id.frage, myitem.tag[position], true)
                    }
                }else{
                    JzActivity.getControlInstance()
                        .changeFrag(myitem.change[position]!!, R.id.frage, myitem.tag[position], true)
                }
            }
        }

    }

    override fun sizeInit(): Int {
        return myitem.item.size
    }

}