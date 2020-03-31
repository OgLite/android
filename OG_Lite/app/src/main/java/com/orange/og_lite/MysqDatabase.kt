package com.orange.og_lite

import android.location.Location
import com.jzsql.lib.mmySql.ItemDAO
import com.orange.jzchi.jzframework.JzActivity
import com.orange.og_lite.beans.PublicBeans
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

object MysqDatabase {
    fun InsertMemory(memory:String,errortype: String) {
try {
    OgCommand.tx_memory= StringBuffer()
    var sql=(JzActivity.getControlInstance().getRootActivity() as MainActivity).sqlmemnory
    sql.exsql("CREATE TABLE if not exists `transfermemory2` (\n" +
            "  `id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "  `tx` varchar NOT NULL,\n" +
            "  `errortype` varchar NOT NULL,\n" +
            "  `serialnumber` varchar NOT NULL,\n" +
            "  `make` varchar NOT NULL,\n" +
            "  `model` varchar NOT NULL,\n" +
            "  `year` varchar NOT NULL,\n" +
            "  `account` varchar NOT NULL,\n"+
            "`number` INTEGER"+")")
    Thread {
        var conn: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("com.mysql.jdbc.Driver")
            conn = DriverManager.getConnection(
                "jdbc:mysql://35.240.51.141:3306/ogmemory?useSSL=false&serverTimezone=UTC",
                "root",
                "orangetpms"
            )
            stmt = conn!!.createStatement()
            stmt.executeUpdate("insert into transfermemory (tx,errortype,serialnumber,make,model,year,account,number) values " +
                    "('${memory}','$errortype','${"SP:" + PublicBeans.serial}','${PublicBeans.make}','${PublicBeans.model}','${PublicBeans.year}','${PublicBeans.admin}',${PublicBeans.password})")
        } catch (e: Exception) {
            e.printStackTrace()
            sql.exsql("delete transfermemory2  where rownum <= (select (count(*)-20) from transfermemory2")
            sql.exsql("insert into transfermemory2 (tx,errortype,serialnumber,make,model,year,account,number) values " +
                    "('${memory}','$errortype','${"SP:" + PublicBeans.serial}','${PublicBeans.make}','${PublicBeans.model}','${PublicBeans.year}','${PublicBeans.admin}',"+PublicBeans.programNumber+")")
        }
    }.start()
}catch (e:java.lang.Exception){e.printStackTrace()}

    }

}
