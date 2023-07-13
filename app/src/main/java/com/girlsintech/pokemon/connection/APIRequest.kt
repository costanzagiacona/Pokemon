package com.girlsintech.pokemon.connection

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.girlsintech.pokemon.util.Constants.BASE_URL
import org.json.JSONObject

class APIRequest (context: Context){
    private var request: RequestQueue

    companion object {
        private var apiRequest: APIRequest? = null

        fun getAPI(context: Context): APIRequest {
            if (apiRequest == null) {
                apiRequest = APIRequest(context)

            }
            return apiRequest!!
        }
    }

    init {
        request = Volley.newRequestQueue(context)
    }

    fun getPokemonList(onSuccess: (JSONObject?) -> Unit,
                    onFail: (VolleyError?) -> Unit, limit: Int, offset:Int ){
        val url = "$BASE_URL?offset=$offset&limit=$limit"

        val jsonLoader = JsonObjectRequest(url,
            onSuccess,
            onFail)
        jsonLoader.retryPolicy = DefaultRetryPolicy(5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        request.add(jsonLoader)
    }

    fun getPokemonInfo(onSuccess: (JSONObject?) -> Unit,
                       onFail: (VolleyError?) -> Unit, pokemonName: String){
        val url = "$BASE_URL/$pokemonName"

        val jsonLoader = JsonObjectRequest(url,
            onSuccess,
            onFail)
        jsonLoader.retryPolicy = DefaultRetryPolicy(5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        request.add(jsonLoader)
    }

}