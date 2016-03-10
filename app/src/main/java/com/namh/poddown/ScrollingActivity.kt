package com.namh.poddown

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.android.volley.Response
import com.humpbackwhale.spike.utils.AndroidDialog
import com.humpbackwhale.spike.utils.App
import com.namh.poddown.network.volley.MyVolleyRequestQueue
import com.namh.poddown.network.volley.RequestSwallow
import org.json.JSONObject
import kotlin.properties.Delegates

class ScrollingActivity : AppCompatActivity() {

    val _LIST_REQUEST_URL = "http://enabler.kbs.co.kr/api/podcast_channel/feed.xml?channel_id=R2014-0346"
    var _podlist by Delegates.notNull<RecyclerView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        _initWithAppContext()

        // Floating button
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        // Recycler View
        _setRecyclerView()

        // init()
        _init()

    }

    private fun _initWithAppContext() {
        // init Queue
        MyVolleyRequestQueue.init(this.applicationContext)
        val queue = MyVolleyRequestQueue.getRequestQueue()

        // init Swallow
        RequestSwallow.init(this.applicationContext, queue)

    }

    private fun _setRecyclerView() {
        _podlist = findViewById(R.id.rv_podlist) as RecyclerView

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        _podlist.setHasFixedSize(true);

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(this);
        _podlist.setLayoutManager(layoutManager);


        val values = arrayOf("Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View",
                "Android Example List View2",
                "Android Example List View3")
        val adapter = MyAdapter(values)


        _podlist.adapter = adapter

    }

    private fun _init() {
        RequestSwallow.stringRequest(
                url = _LIST_REQUEST_URL,
                onSuccess = Response.Listener<String> { resp ->
                    // set listview
                    // resp!!.getJSONObject(Param.SUCCESS)!!.getString(Param.TOKEN)
                    val adapter = _podlist.adapter as MyAdapter
                    adapter.replaceDataset(
                            arrayOf("New List Item 1",
                                    "new List item 2"))
                    adapter.notifyDataSetChanged()
                },
                onError = Response.ErrorListener { err ->
                    AndroidDialog.show(this, message = "error on request _LIST_REQUEST_URL",
                            noText = "",
                            onYesClick = {d, h ->
                                App.exit()
                            })

                }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true

        }
        return super.onOptionsItemSelected(item)
    }
}
