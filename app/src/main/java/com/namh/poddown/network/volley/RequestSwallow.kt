package com.namh.poddown.network.volley

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.humpbackwhale.spike.volley.MyJSONObjectRequest
import com.humpbackwhale.spike.volley.MyStringRequest
import org.json.JSONObject

/**
 * Created by namh on 2015-10-14.
 */


/**
 * Singleton RequestSwallow
 *
 * Swallow is a metaphor for request-worker class
 */
object RequestSwallow {


//    const val HOST = "http://124.49.91.155:8080/"
//    const val HOST = "http://daum.net/"
//    const val HOST = "https://124.49.91.155:443/"
//    const val HOST = "https://www.google.com/"
//    const val HOST = "https://spikeis.net:443/"
    const val HOST = "https://www.hbwhale.com:443/"


    var _queue: RequestQueue? = null
    var _token: String = ""

    fun init(appContext: Context, queue: RequestQueue?){
        _queue = queue

    }
    fun deliver() {

    }


    /**
     *  Generate the Request instance and add it the the queue
     *
     * @param jparam
     *  JsonObject()
     *  Json.genJSON(mapOf("key" to Enc.getEncryptedSymKey(context) ))
     *
     * @param onSuccess
     *      Response.Listener<JSONObject>{resp ->
     *          listener(resp)
     *      },
     *
     * @param onError
     *      Response.ErrorListener{ err ->
     *          errorListener(err)
     *      }) {
     *
     */

    fun request(url: String, jparam: JSONObject,
                onSuccess: Response.Listener<JSONObject>,
                onError: Response.ErrorListener,
                tag: String = "no tag", longer: Boolean = false){


        // request
        val jsonRequest = MyJSONObjectRequest(
                Request.Method.GET,
                url, jparam,
                onSuccess,
                onError
        )

        // Set Tag
        val REQUEST_TAG = tag
        jsonRequest.setTag(REQUEST_TAG)

        if(longer){
            jsonRequest.setRetryPolicy(DefaultRetryPolicy(30 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        }


        // add to queue
        _queue!!.add(jsonRequest)


    }

    fun stringRequest(url: String,
                      onSuccess: Response.Listener<String>,
                      onError: Response.ErrorListener,
                      tag: String = "no tag", longer: Boolean = false){


        // request
        val jsonRequest = MyStringRequest(
                Request.Method.GET,
                url,
                onSuccess,
                onError
        )

        // Set Tag
        val REQUEST_TAG = tag
        jsonRequest.setTag(REQUEST_TAG)

        if(longer){
            jsonRequest.setRetryPolicy(DefaultRetryPolicy(30 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        }


        // add to queue
        _queue!!.add(jsonRequest)


    }


}





