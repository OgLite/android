package com.orange.og_lite.Frag

import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__add_my_favorite.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_AddMyFavorite_test : JzFragement(R.layout.fragment_frag__setting_pager__add_my_favorite) {

    val mMainActivity=JzActivity.getControlInstance().getRootActivity() as MainActivity
    val name = ArrayList<Util_MmySql_module>()

    val make = ArrayList<String>()
    val model = ArrayList<String>()
    val year = ArrayList<String>()

    lateinit var MakeAdapter:ArrayAdapter<String>
    lateinit var ModelAdapter:ArrayAdapter<String>
    lateinit var YearAdapter:ArrayAdapter<String>

    override fun viewInit() {

        MakeAdapter=ArrayAdapter<String>(activity!!, R.layout.mmy_spinner, make)
        rootview.make_spinner.adapter=MakeAdapter

        ModelAdapter = ArrayAdapter<String>(activity!!,
            R.layout.mmy_spinner, model)
        rootview.model_spinner.adapter=ModelAdapter

        YearAdapter = ArrayAdapter<String>(activity!!, R.layout.mmy_spinner, year)
        rootview.year_spinner.adapter=YearAdapter

        //db_download()
        query_make()

        rootview.make_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long)
            {
                query_model()
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {
            }
        }

        rootview.model_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long)
            {
                query_year()
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {
            }
        }

        rootview.year_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long)
            {
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {
            }

        }

        rootview.OK.setOnClickListener {
            val make_string = rootview.make_spinner.selectedItem.toString()
            val model_string = rootview.model_spinner.selectedItem.toString()
            val year_string = rootview.year_spinner.selectedItem.toString()

            if(!mMainActivity.MyFavorite_Memory.MakeArray.contains(make_string) || !mMainActivity.MyFavorite_Memory.ModelArray.contains(model_string) || !mMainActivity.MyFavorite_Memory.YearArray.contains(year_string)) {
                mMainActivity.MyFavorite_Memory.MakeArray.add(make_string)
                mMainActivity.MyFavorite_Memory.ModelArray.add(model_string)
                mMainActivity.MyFavorite_Memory.YearArray.add(year_string)

                Thread {

                    //val item= ItemDAO(context!!,"test.db").create()

                    //創建資料表
                    mMainActivity.item.exsql(
                        "CREATE TABLE   IF NOT EXISTS myFavoritetable (\n" +
                                "    id   INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                                "    make VARCHAR NOT NULL,\n" +
                                "    model VARCHAR NOT NULL,\n" +
                                "    year VARCHAR NOT NULL\n" +
                                ");\n"
                    )
                    //       mMainActivity.item.exsql("delete from myFavoritetable where make='$make_string' and model='$model_string' and year='$year_string' ")
                    //插入資料
                    mMainActivity.item.exsql(
                        "insert into myFavoritetable(make,model,year) " +
                                "values ( '" + make_string + "','" + model_string + "','" + year_string + "' )"
                    )

                    handler.post {
                        JzActivity.getControlInstance().goBack("Frag_SettingPager_MyFavorite")
                    }
                }.start()
                rootview.textView21.text = make_string + "/" + model_string + "/" + year_string
            }
        }

        rootview.Cancel.setOnClickListener {

            Thread {
                //資料查詢
                mMainActivity.item.query("select * from myFavoritetable", Sql_Result {
                    //Callback回調，會迴圈跑到所有資料載入完
                    val result1 = it.getString(1)
                    val result2 = it.getString(2)
                    val result3 = it.getString(3)
                    handler.post{
                        rootview.textView21.text = result1 + "/" + result2 + "/" + result3
                    }

                });
            }.start()


        }

    }

//    fun mmyname(): String {
//        try {
//            val url = URL("https://bento2.orange-electronic.com/Orange%20Cloud/Database/MMY/EU/")
//            val conn = url.openConnection() as HttpURLConnection
//            val reader = BufferedReader(InputStreamReader(conn.inputStream, "utf-8") as Reader?)
//            var line: String? = null
//            val strBuf = StringBuffer()
//            line=reader.readLine()
//            while (line != null) {
//                strBuf.append(line)
//                line=reader.readLine()
//            }
//            val arg = strBuf.toString().split("HREF=\"".toRegex()).dropLastWhile { it.isEmpty() }
//                .toTypedArray()
//            for (a in arg) {
//                if (a.contains(".db")) {
//                    return a.substring(a.indexOf(">") + 1, a.indexOf("<"))
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return "nodata"
//    }
//
//    fun db_download()
//    {
//        Thread{
//            val version=mmyname()
//            if(JzActivity.getControlInstance().getPro("version","nodata")==version){
//                //act.item.create().query("select count(1) from `Summary table`", Sql_Result { result ->
//
//            }else{
//                mMainActivity.item.init_ByUrl( "https://bento2.orange-electronic.com/Orange%20Cloud/Database/MMY/EU/" +version  , InitCaller {
//                    if (it) {
//                        JzActivity.getControlInstance().setPro("version",version)
//                        Log.e("database", "預載資料庫成功")
//                        //act.item.create().query("select count(1) from `Summary table`", Sql_Result { result ->
//
//                    } else {
//                        Log.e("database", "預載資料庫失敗")
//                    }
//                })
//            }
//
//        }.start()
//    }

    fun query_make()
    {
        make.clear()

        Thread {
            mMainActivity.item.create()
                .query("select distinct `Make` from `Summary table` ", Sql_Result { result ->
                    val module = Util_MmySql_module()
                    //val data = result.getString(0)
                    //module.make=result.getString(0)
                    //module.image=result.getString(1)
                    make.add(result.getString(0))
                    handler.post {
                        //rootview.Select_Make_Title.text = module.make
                        //(rootview.spinner3.adapter as Ad_Make).notifyDataSetChanged()
                        MakeAdapter.notifyDataSetChanged()
                        //make_string = rootview.make_spinner.selectedItem.toString()
                    }
                })
            handler.post{
                rootview.make_spinner.setSelection(0)
            }
            //Thread.sleep(500)
            //query_model()

        }.start()
    }

    fun query_model()
    {
        model.clear()
        if(rootview.make_spinner.selectedItem == null) {return}
        Thread {
            mMainActivity.item.query("select distinct `Model` from `Summary table` where `Make` = '" + rootview.make_spinner.selectedItem +"'", Sql_Result { result ->
                    //val module = Util_MmySql_module()
                    //val data = result.getString(0)
                    //module.make=result.getString(0)
                    //module.image=result.getString(1)
                    model.add(result.getString(0))
                    handler.post {
                        //rootview.Select_Make_Title.text = module.make
                        //(rootview.spinner3.adapter as Ad_Make).notifyDataSetChanged()
//                        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.mmy_spinner, model)
//                        rootview.model_spinner.adapter=arrayAdapter
                        ModelAdapter.notifyDataSetChanged()
                        //model_string = rootview.model_spinner.selectedItem.toString()
                    }

                })
          handler.post{
              query_year()
          }
            //Thread.sleep(500)
            //query_year()

        }.start()
    }

    fun query_year()
    {
        year.clear()

        if(rootview.make_spinner.selectedItem == null) {return}
        if(rootview.model_spinner.selectedItem == null) {return}

        Thread {
            mMainActivity.item.create()
                .query("select distinct `Year` from `Summary table` where `Make` = '" + rootview.make_spinner.selectedItem + "'" + "and `Model` = '" + rootview.model_spinner.selectedItem + "'", Sql_Result { result ->
                    //val module = Util_MmySql_module()
                    //val data = result.getString(0)
                    //module.make=result.getString(0)
                    //module.image=result.getString(1)
                    year.add(result.getString(0))
                    handler.post {
                        //rootview.Select_Make_Title.text = module.make
                        //(rootview.spinner3.adapter as Ad_Make).notifyDataSetChanged()
//                        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.mmy_spinner, year)
//                        rootview.year_spinner.adapter=arrayAdapter
                        YearAdapter.notifyDataSetChanged()
                        //year_string = rootview.year_spinner.selectedItem.toString()
                    }
                })
        }.start()
    }

}
