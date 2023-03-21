package com.example.mrrobotchatgptapi

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var queryEdt: TextInputEditText
    lateinit var messageRV: RecyclerView
    lateinit var messageRVAdapter: MessageRVAdapter
    lateinit var messageList: ArrayList<MessageRVModel>
    var url = "https://api.openai.com/v1/completions"
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queryEdt = findViewById(R.id.idEdtQuery)
        messageRV = findViewById(R.id.idRVMessages)
        messageList = ArrayList()
        messageRVAdapter = MessageRVAdapter(messageList)
        val layoutManager = LinearLayoutManager(applicationContext)
        messageRV.layoutManager = layoutManager
        messageRV.adapter = messageRVAdapter

        queryEdt.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEND) {
                if (queryEdt.text.toString().length > 0) {
                    messageList.add(MessageRVModel(queryEdt.text.toString(), "user"))
                    messageRVAdapter.notifyDataSetChanged()
                    getResponse(queryEdt.text.toString())
                } else {
                    Toast.makeText(this, "Please enter your query", Toast.LENGTH_SHORT).show()
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getResponse(query: String) {
        queryEdt.setText("")
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObj: JSONObject? = JSONObject()

        jsonObj?.put("model","text-davinci-003")
        jsonObj?.put("prompt",query)
        jsonObj?.put("temperature",0)
        jsonObj?.put("max_tokens",100)
        jsonObj?.put("top_p",1)
        jsonObj?.put("frequency_penalty",0.0)
        jsonObj?.put("presence_penalty",0.0)

        val postRequest: JsonObjectRequest = object: JsonObjectRequest(Method.POST, url, jsonObj, Response.Listener { response ->
            val responseMsg: String = response.getJSONArray("choices").getJSONObject(0).getString("text")
            messageList.add(MessageRVModel(responseMsg, "bot"))
            messageRVAdapter.notifyDataSetChanged()
        }, Response.ErrorListener {
            Toast.makeText(applicationContext,"Fail to get response.....", Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer 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"
                return params
            }
        }
        postRequest.setRetryPolicy(object: RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            override fun retry(error: VolleyError) {

            }
        })
        queue.add(postRequest)

    }
}