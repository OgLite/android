package com.orange.og_lite.adapter

import android.view.View
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.Frag.Frag_Function_Relearn_Procedure
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.MyFavoriteItem
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.adpater_ad_myfavorite.view.*

class Ad_MyFavorite(val myitem : MyFavoriteItem):JzAdapter(R.layout.adpater_ad_myfavorite)
{
    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mView.MyFavorite_MMY_button.text = myitem.item[position]

        //Frag_SettingPager_MyFavorite().MakeArray.set(0,"aaa")
        //IDtext.get(Triggerid.size - 1).text = a.toString()
        if(JzActivity.getControlInstance().getNowPageTag().equals("Frag_SelectMmyPage_MyFavorite"))
        {
            //this.notifyDataSetChanged()
            holder.mView.delete.visibility = View.GONE
            holder.mView.MyFavorite_button.setOnClickListener {
//                holder.mView.MyFavorite_MMY_button.text =
//                    mMainActivity.MyFavorite_Memory.MakeArray.get(position)

                PublicBeans.make = mMainActivity.MyFavorite_Memory.MakeArray.get(position)
                PublicBeans.model = mMainActivity.MyFavorite_Memory.ModelArray.get(position)
                PublicBeans.year = mMainActivity.MyFavorite_Memory.YearArray.get(position)
                //PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year

                JzActivity.getControlInstance()
                    .changeFrag(Frag_Function_Relearn_Procedure(0), R.id.frage, "Frag_Function_Relearn_Procedure", true)

            }
        }

        if(JzActivity.getControlInstance().getNowPageTag().equals("Frag_SettingPager_MyFavorite")) {
            holder.mView.delete.setOnClickListener {
                myitem.item.removeAt(position)
                mmy_delete(
                    mMainActivity.MyFavorite_Memory.MakeArray.get(position),
                    mMainActivity.MyFavorite_Memory.ModelArray.get(position),
                    mMainActivity.MyFavorite_Memory.YearArray[position]
                )

                mMainActivity.MyFavorite_Memory.MakeArray.removeAt(position)
                mMainActivity.MyFavorite_Memory.ModelArray.removeAt(position)
                mMainActivity.MyFavorite_Memory.YearArray.removeAt(position)

                this.notifyDataSetChanged()
            }
        }

    }

    override fun sizeInit(): Int {
        return myitem.item.size

    }


    fun mmy_delete( make_string:String,model_string:String,year_string:String) {
        Thread {
            //資料查詢
            mMainActivity.item_favorite.create().query("delete from myFavoritetable where `Make`='" +  make_string + "'" + "and `Model` = '" + model_string + "'" + "and `Year` = '" + year_string + "'", Sql_Result {
                //Callback回調，會迴圈跑到所有資料載入完
                val result1 = it.getString(1)
                val result2 = it.getString(2)
                val result3 = it.getString(3)

                //handler.post {
                    //(rootview.MyFavoriteView.adapter as Ad_MyFavorite).notifyDataSetChanged()
                //}

            });
        }.start()
    }
}