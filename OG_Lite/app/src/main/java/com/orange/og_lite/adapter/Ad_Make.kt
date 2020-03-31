package com.orange.og_lite.adapter

import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.Frag.Frag_SelectMmyPage_Model
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.PublicBeans
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import kotlinx.android.synthetic.main.adapter_ad_make.view.*
import java.util.ArrayList

class Ad_Make(val name: ArrayList<Util_MmySql_module>) : JzAdapter(R.layout.adapter_ad_make)
{
    //lateinit var mMainActivity: MainActivity
    val mMainActivity=JzActivity.getControlInstance().getRootActivity() as MainActivity

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.image.setImageResource(holder.itemView.resources.getIdentifier(name[position].image,"mipmap",holder.itemView.context.packageName))
        holder.mView.image.setOnClickListener {
            //mMainActivity=activity!! as MainActivity
            PublicBeans.make=name[position].make
            JzActivity.getControlInstance().changeFrag(Frag_SelectMmyPage_Model(),R.id.frage,"Frag_SelectMmyPage_Model",true)
        }
    }

    override fun sizeInit(): Int {
        return name.size
    }

}