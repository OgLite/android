package com.orange.og_lite.Frag


import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Make
import com.orange.og_lite.beans.PublicBeans
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import kotlinx.android.synthetic.main.fragment_frag__select_mmy_page__make.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SelectMmyPage_Make : JzFragement(R.layout.fragment_frag__select_mmy_page__make) {


    val mMainActivity=JzActivity.getControlInstance().getRootActivity() as MainActivity
    var name = ArrayList<Util_MmySql_module>()
    lateinit var adapter: Ad_Make

    override fun viewInit() {

        val a = JzActivity.getControlInstance().getPro("Languages","")
        Log.e("Languages",a)

        query_make()

        name = ArrayList<Util_MmySql_module>()
        adapter= Ad_Make(name)
        rootview.Select_Make_View.layoutManager= GridLayoutManager(activity, 3)
        rootview.Select_Make_View.adapter=adapter
    }





    fun query_make()
    {
        name.clear()
        Thread {
            //mMainActivity.item.query("select distinct `Make`,`Make_img` from `Summary table` ", Sql_Result { result ->
            var sql=""
            if(PublicBeans.position==PublicBeans.OBD複製||PublicBeans.position==PublicBeans.OBD學碼){
                sql="select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA') and `OBD1` not in('NA') order by `Make` asc"
            }else{
                sql="select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA')  order by `Make` asc"
            }
                mMainActivity.item.query(sql, Sql_Result { result ->
                    val module = Util_MmySql_module()
                    //val data = result.getString(0)
                    module.make=result.getString(0)
                    module.image=result.getString(1)
                    name.add(module)
                    handler.post {
                        //rootview.Select_Make_Title.text = module.make
                        (rootview.Select_Make_View.adapter as Ad_Make).notifyDataSetChanged()
                    }
                })
        }.start()
    }

}
