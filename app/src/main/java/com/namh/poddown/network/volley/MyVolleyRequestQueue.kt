package com.namh.poddown.network.volley

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*
import com.android.volley.toolbox.BasicNetwork

import kotlin.properties.Delegates

/**
 * Created by namh on 2015-10-29.
 */




object MyVolleyRequestQueue {

    var _ctx by Delegates.notNull<Context>()

    fun init(context: Context){
        _ctx = context
    }



    // to change to https
    // @ref http://i5on9i.blogspot.kr/2015/10/volley-https-ssl.html

    // Don't forget to start the volley request queue
    // Current approach is used just for brevity
    var _requestQueue: RequestQueue? = null

    fun getRequestQueue(): RequestQueue? {
        if (_requestQueue == null) {
            val cache = DiskBasedCache(_ctx.cacheDir, 10 * 1024 * 1024)
            val network = BasicNetwork(HurlStack())
            _requestQueue = RequestQueue(cache, network)
            _requestQueue!!.start()

        }
        return _requestQueue
    }


}
