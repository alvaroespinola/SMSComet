package com.ar.smsingesoft

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.Activity
import android.util.Log
import khttp.responses.Response
import org.jetbrains.anko.doAsync


class SMSSendIntent : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var status: String

        var delivered = intent!!.getIntExtra("delivered", 0)
        if (delivered == 1) {
            status = "DELIVERED"
        } else {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    status = "SENT"
                }
                else -> {
                    status = "FAILED"
                }
            }

        }

        var sendURL = intent!!.getStringExtra("sendURL")
        var deviceId = intent!!.getStringExtra("deviceId")
        var deviceSecret = intent!!.getStringExtra("deviceSecret")
        var messageId = intent!!.getStringExtra("messageId")
        Log.d("----->", "async->" + messageId + "-" + status + "-sucker" + deviceId)

        doAsync {
            lateinit var res: Response
            try {
                Log.d("-->", "Post status to " + sendURL)
                res = khttp.post(
                    url = sendURL,
                    data = mapOf(
                        "deviceId" to deviceId,
                        "deviceSecret" to deviceSecret,
                        "messageId" to messageId,
                        "status" to status,
                        "action" to "STATUS_UPDATE"
                    )
                )
                Log.d("----->", res.text)
            } catch (e: java.net.ConnectException) {
                Log.d("-->", "Cannot connect to URL")
            }


        }
    }
}