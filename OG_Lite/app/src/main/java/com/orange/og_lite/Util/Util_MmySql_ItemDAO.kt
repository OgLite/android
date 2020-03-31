package com.orango.electronic.orangetxusb.mmySql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
//import com.example.obd.MainPeace
//import com.orange.obd.R
import com.orange.og_lite.R
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import java.util.ArrayList

class Util_MmySql_ItemDAO(context: Context) {
    companion object {
        val TAG = "Util_MmySql_ItemDAO"
        // 表格名稱
        val TABLE_NAME = "mmy_table"

        // 編號表格欄位名稱，固定不變
        val KEY_ID = "_id"

        // 其它表格欄位名稱
        val MAKE_COLUMN = "Make"
        val MODEL_COLUMN = "Model"
        val YEAR_COLUMN = "Year"
        val ORANGE_PART_COLUMN = "Orangepart(ProgramName)"
        val RF_PROTOCOL_NUM_COLUMN = "RFProtocolNum"
        val LF_PROTOCOL_NUM_COLUMN = "LFProtocolNum"
        val MAKE_IMG_COLUMN = "Make_Img"

        // 使用上面宣告的變數建立表格的SQL敘述
        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MAKE_COLUMN + " TEXT, " +
                MODEL_COLUMN + " TEXT, " +
                YEAR_COLUMN + " TEXT, " +
                ORANGE_PART_COLUMN + " TEXT, " +
                RF_PROTOCOL_NUM_COLUMN + " TEXT, " +
                LF_PROTOCOL_NUM_COLUMN + " TEXT" +
                MAKE_IMG_COLUMN + " TEXT)"
    }

    //資料庫物件
    private var dbHelper = Util_MmySql_DatabaseHelper(context)
    private  var db: SQLiteDatabase

    init {
        dbHelper.createDataBase()
        if(dbHelper.checkDataBase())
            Log.d(TAG, "checkDataBase: true")
            dbHelper.openDataBase()
            db = dbHelper.db
    }

    // 取得資料數量
    val count: Int
        get() {
            var result = 0
            val cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null)

            if (cursor.moveToNext()) {
                result = cursor.getInt(0)
            }

            return result
        }

    // 關閉資料庫，一般的應用都不需要修改
    fun close() {
        dbHelper.close()
    }

    // 新增參數指定的物件
    fun insert(sql:ArrayList<String>): Boolean {
        for(a in sql){
            db.execSQL("insert into mmy_table(Make,Model,Year,OEPartNum,AMPart,DirectFit) values $a");
        }

        return true
    }



    // 刪除參數指定編號的資料
    fun delete(id: Long): Boolean {
        // 設定條件為編號，格式為「欄位名稱=資料」
        val where = KEY_ID + "=" + id
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where, null) > 0
    }


    fun IsFiveTire(name:String):Boolean{
        val result = db.rawQuery("select `Wheel_Count` from `Summary table` where `OBD1`='$name' and `Wheel_Count` not in('NA') limit 0,1", null)
        if(result.count > 0 ){
            result.moveToFirst()
            do{
                val count=result.getString(0)
                return if (count.contains("5")) true else false
            }while (result.moveToNext())
        }else{
            result.close()
            return false
        }
    }
    fun getMakeString(): ArrayList<String>?{
        val makes = arrayListOf<String>()

        val result = db.rawQuery(
            "select distinct $MAKE_COLUMN from $TABLE_NAME order by $MAKE_COLUMN asc",null)

        if(result.count > 0){
            result.moveToFirst()
            do{
                makes.add(result.getString(0))
            }while (result.moveToNext())
            // 關閉Cursor物件
            result.close()
            // 回傳結果
            return makes
        }else{
            result.close()
            return null
        }
    }

    fun getMake(): ArrayList<Util_MmySql_module>?{
        val makes = arrayListOf<Util_MmySql_module>()
        val result = db.rawQuery(
            "select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA') and `OBD1` not in('NA') order by `Make` asc",null)
        if(result.count > 0){
            result.moveToFirst()
            do{

                val module = Util_MmySql_module()
                module.make = result.getString(0)
                module.image=result.getString(1)
                Log.d("make",result.getString(0));
                Log.d("image",result.getString(1));
                makes.add(module)
            }while (result.moveToNext())
            // 關閉Cursor物件
            result.close()
            // 回傳結果
            return makes
        }else{
            result.close()
            return null
        }
    }

    fun getmodel(name:String): ArrayList<Util_MmySql_module>?{
        val makes = arrayListOf<Util_MmySql_module>()
        val result = db.rawQuery(
                "select distinct model from `Summary table` where make='$name' and `OBD1` not in('NA') order by model asc",null)
        if(result.count > 0){
            result.moveToFirst()
            do{
                val module = Util_MmySql_module()
                module.make = name
                module.modele=result.getString(0)
                Log.d("make",name);
                Log.d("image",result.getString(0));
                makes.add(module)
            }while (result.moveToNext())
            // 關閉Cursor物件
            result.close()
            // 回傳結果
            return makes
        }else{
            result.close()
            return null
        }
    }
    fun getYear(make:String,model:String):ArrayList<Util_MmySql_module>?{
        val models = arrayListOf<Util_MmySql_module>()
        val result = db. rawQuery(
                "select distinct Year from `Summary table` where model='$model' and make='$make' and `OBD1` not in('NA') order by Year asc",
                null)

        if(result.count > 0){
            result.moveToFirst()
            do{
                val module = Util_MmySql_module()
                module.make=make
                module.modele=model
                module.year=result.getString(0)
                models.add(module)
            }while (result.moveToNext())
            // 關閉Cursor物件
            result.close()
            // 回傳結果
            return models
        }else{
            result.close()
            return null
        }
    }
    fun getModel(make: String): ArrayList<String>?{
        val models = arrayListOf<String>()

        val result = db. rawQuery(
            "select distinct $MODEL_COLUMN from $TABLE_NAME where $MAKE_COLUMN = '$make' order by $MODEL_COLUMN asc",
            null)

        if(result.count > 0){
            result.moveToFirst()
            do{
                models.add(result.getString(0))
            }while (result.moveToNext())
            // 關閉Cursor物件
            result.close()
            // 回傳結果
            return models
        }else{
            result.close()
            return null
        }
    }
fun  getPart(make:String,model:String,year:String):Util_MmySql_module{
    var module = Util_MmySql_module()
    val result = db. rawQuery(
            "select `OBD1` from `Summary table` where Make='$make' and Model='$model' and year='$year' and `OBD1` not in('NA') limit 0,1",
            null)

    if(result.count > 0){
        result.moveToFirst()
        do{
            module.make=make
            module.modele=model
            module.year=year
            module.directfit=result.getString(0)
        }while (result.moveToNext())
        // 關閉Cursor物件
        result.close()
        // 回傳結果
        return module
    }else{
        result.close()
        return module
    }
}
    fun GetreLarm(make:String,model:String,year:String,act:Context):String{
        val profilePreferences = act.getSharedPreferences("Frag_SettingPager_Setting", Context.MODE_PRIVATE)
        val a= profilePreferences.getString("Language","English")
        var colname="English"
        when(a){
            "繁體中文"->{ colname="`Relearn Procedure (Traditional Chinese)`"}
            "简体中文"->{ colname="`Relearn Procedure (Jane)`"}
            "Deutsche"->{ colname="`Relearn Procedure (German)`"}
            "English"->{ colname="`Relearn Procedure (English)`"}
            "Italiano"->{ colname="`Relearn Procedure (Italian)`"}
        }
        val result = db.rawQuery(
                "select $colname from `Summary table` where make='$make' and model='$model' and year='$year' limit 0,1",
                null)

        if(result.count > 0 ){
            result.moveToFirst()
            if(result.getString(0).isEmpty()){  return act.resources.getString(R.string.norelarm)}else{  return result.getString(0)}

        }else{
            result.close()
            return act.resources.getString(R.string.norelarm)
        }
    }

}