package com.example.statisticsview

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.statistics.library.line_chart.data.DataEntity
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        line_chart_view.verticalParts = 4
        line_chart_view.setData(test1())
        line_chart_view.startAnim()

        line_chart_view2.verticalStartValue = 50//纵坐标的起始值，默认-5
        line_chart_view2.verticalParts = 6//纵坐标的段数，默认5
        line_chart_view2.setData(test2())
        line_chart_view2.startAnim()

        line_chart_view3.verticalStartValue = 0
        line_chart_view3.setData(test3())
        line_chart_view3.startAnim()
    }

    fun test1(): ArrayList<DataEntity> {
        var data1 = DataEntity(0)
        data1.value = 10
        data1.des = "10s"
        var data2 = DataEntity(1)
        data2.value = 25
        data2.des = "25s"
        var data3 = DataEntity(2)
        data3.value = 20
        data3.des = "20s"
        var data4 = DataEntity(3)
        data4.value = 30
        data4.des = "30s"
        var data5 = DataEntity(4)
        data5.value = 15
        data5.des = "15s"
        var data6 = DataEntity(5)
        data6.value = 68
        data6.des = "68s"
        var data7 = DataEntity(6)
        data7.value = 40
        data7.des = "40s"
        var data8 = DataEntity(7)
        data8.value = 45
        data8.des = "45s"
        var mDatas = ArrayList<DataEntity>()
        mDatas.add(data1)
        mDatas.add(data2)
        mDatas.add(data3)
        mDatas.add(data4)
        mDatas.add(data5)
        mDatas.add(data6)
        mDatas.add(data7)
        mDatas.add(data8)
        var currMillis = System.currentTimeMillis()
        val daysMillis = TimeUnit.DAYS.toMillis(1)
        val temp = currMillis % daysMillis
        currMillis -= temp
        for (i in mDatas.indices) {
            val position = (mDatas.size - 1 - i).toLong()
            val offsetMillis = TimeUnit.DAYS.toMillis(position)
            val millis = currMillis - offsetMillis
            mDatas.get(i).millis = (millis)
        }
        return mDatas
    }

    fun test2(): ArrayList<DataEntity> {
        var data1 = DataEntity(0)
        data1.value = 100
        data1.des = "100"
        var data2 = DataEntity(1)
        data2.value = 2000
        data2.des = "2000"
        var data3 = DataEntity(2)
        data3.value = 600
        data3.des = "600"
        var data4 = DataEntity(3)
        data4.value = 80
        data4.des = "80"
        var data5 = DataEntity(4)
        data5.value = 999
        data5.des = "999"
        var data6 = DataEntity(5)
        data6.value = 888
        data6.des = "888"
        var data7 = DataEntity(6)
        data7.value = 1790
        data7.des = "1790"
        var mDatas = ArrayList<DataEntity>()
        mDatas.add(data1)
        mDatas.add(data2)
        mDatas.add(data3)
        mDatas.add(data4)
        mDatas.add(data5)
        mDatas.add(data6)
        mDatas.add(data7)
        var currMillis = System.currentTimeMillis()
        val daysMillis = TimeUnit.DAYS.toMillis(1)
        val temp = currMillis % daysMillis
        currMillis -= temp
        for (i in mDatas.indices) {
            val position = (mDatas.size - 1 - i).toLong()
            val offsetMillis = TimeUnit.DAYS.toMillis(position)
            val millis = currMillis - offsetMillis
            mDatas.get(i).millis = (millis)
        }
        return mDatas
    }

    fun test3(): ArrayList<DataEntity> {
        var data1 = DataEntity(0)
        data1.value = 44
        data1.des = "44"
        var data2 = DataEntity(1)
        data2.value = 90
        data2.des = "90"
        var data3 = DataEntity(2)
        data3.value = 222
        data3.des = "222"
        var data4 = DataEntity(3)
        data4.value = 49
        data4.des = "49"
        var data5 = DataEntity(4)
        data5.value = 60
        data5.des = "60"
        var mDatas = ArrayList<DataEntity>()
        mDatas.add(data1)
        mDatas.add(data2)
        mDatas.add(data3)
        mDatas.add(data4)
        mDatas.add(data5)
        var currMillis = System.currentTimeMillis()
        val daysMillis = TimeUnit.DAYS.toMillis(1)
        val temp = currMillis % daysMillis
        currMillis -= temp
        for (i in mDatas.indices) {
            val position = (mDatas.size - 1 - i).toLong()
            val offsetMillis = TimeUnit.DAYS.toMillis(position)
            val millis = currMillis - offsetMillis
            mDatas.get(i).millis = (millis)
        }
        return mDatas
    }
    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
