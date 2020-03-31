package com.orange.og_lite.adapter

import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.Frag.Frag_Function_Relearn_Procedure
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.PublicBeans
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import kotlinx.android.synthetic.main.adapter_ad_modle.view.*
import java.util.ArrayList


class Ad_Year(val name: ArrayList<Util_MmySql_module>):JzAdapter(R.layout.adapter_ad_modle)
{
    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.ad_modle_name.text=name[position].year
        holder.mView.ad_modle_button.setOnClickListener {
            PublicBeans.year=name[position].year
            //PublicBeans.model= name[position].modele
            JzActivity.getControlInstance().changeFrag(
                Frag_Function_Relearn_Procedure(
                    0
                ),R.id.frage,"Frag_Function_Relearn_Procedure",true)
        }
    }

    override fun sizeInit(): Int {
        return name.size
    }

}