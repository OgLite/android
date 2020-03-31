package com.orange.og_lite.Frag

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Modle
import com.orange.og_lite.beans.PublicBeans
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import kotlinx.android.synthetic.main.fragment_frag__select_mmy_page__model.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SelectMmyPage_Model : JzFragement(R.layout.fragment_frag__select_mmy_page__model) {

    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    var name = ArrayList<Util_MmySql_module>()
    lateinit var adapter: Ad_Modle

    override fun viewInit() {
        rootview.Select_Model_Title.text = PublicBeans.make
        query_model()
        name = ArrayList<Util_MmySql_module>()
        adapter= Ad_Modle(name)
        rootview.Select_Model_View.layoutManager= GridLayoutManager(activity, 1)
        rootview.Select_Model_View.adapter= adapter
    }

    fun query_model()
    {
        name.clear()
        Thread {
            var sql=""
            if(PublicBeans.position==PublicBeans.OBD複製||PublicBeans.position==PublicBeans.OBD學碼){
                sql="select distinct `Model` from `Summary table` where `Make` = '" + PublicBeans.make +"' and `OBD1` not in('NA')"
            }else{
                sql="select distinct `Model` from `Summary table` where `Make` = '" + PublicBeans.make +"' "
            }
            mMainActivity.item
                .query(sql, Sql_Result { result ->
                    val module = Util_MmySql_module()
                    module.modele=result.getString(0)
                    name.add(module)

                })
            handler.post {
                (rootview.Select_Model_View.adapter as Ad_Modle).notifyDataSetChanged()
            }
        }.start()
    }

}
