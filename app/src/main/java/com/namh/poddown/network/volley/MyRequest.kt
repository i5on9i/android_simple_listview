package com.humpbackwhale.spike.volley

import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.util.*
import kotlin.properties.Delegates

class MyJSONObjectRequest : JsonObjectRequest {

    /**
     * @param listener
     *      Response.Listener<JSONObject>{resp ->
     *          listener(resp)
     *      },
     *
     * @param errorListener
     *      Response.ErrorListener{ err ->
     *          errorListener(err)
     *      }) {
     *
     */
    constructor(method: Int, url: String,
                jsonRequest: JSONObject,
                listener: Response.Listener<JSONObject>,
                errorListener: Response.ErrorListener)
    : super(method, url, jsonRequest, listener, errorListener) {


    }


    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers.put("Content-Type", "application/json; charset=utf-8")

        return headers
    }

    override fun getRetryPolicy(): RetryPolicy {
        // here you can write a custom retry policy
        val MY_SOCKET_TIMEOUT_MS = 10000

        return DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    }
}



/**
 *
 */
class MyStringRequest : StringRequest {



    /**
     *
     * @param listener
     *      Response.Listener<String> { resp ->
     *           listener(resp)
     *       },
     * @param errorListener
     *      Response.ErrorListener { err ->
     *           errorListener(err)
     *       }
     *
     */
    constructor(method: Int,
                url: String,
                listener: Response.Listener<String>,
                errorListener: Response.ErrorListener)
    : super(method, url, listener, errorListener) {


    }


//    @Throws(AuthFailureError::class)
//    override fun getHeaders(): Map<String, String> {
//        val headers = HashMap<String, String>()
//        headers.put("Content-Type", "application/x-www-form-urlencoded")
//
//        return headers
//    }


    /**
     * To make parameters for POST request
     */
    override fun getParams(): MutableMap<String, String>? {
        var params: Map<String, String>? = null

        params = mapOf(
                "username" to "test_user",
                "password" to "test_pw"
        )


        return params as MutableMap<String, String>
    }

    override fun getRetryPolicy(): RetryPolicy {
        // here you can write a custom retry policy
        val MY_SOCKET_TIMEOUT_MS = 10000

        return DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    }
}

