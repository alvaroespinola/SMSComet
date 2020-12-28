package com.ar.smsingesoft

import android.os.AsyncTask

class PostReceivedMessage : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String): String {
        var sendURL = params[0]
        var deviceId = params[1]
        var deviceSecret = params[2]
        var smsBody = params[3]
        var smsSender = params[4]
        khttp.post(
            url = sendURL,
            data = mapOf("deviceId" to deviceId, "deviceSecret" to deviceSecret, "message" to smsBody, "number" to smsSender, "action" to "RECEIVED")
        )
        return "great!"
    }

    override fun onPostExecute(result: String) {

    }

    override fun onPreExecute() {}

    override fun onProgressUpdate(vararg values: Void) {}
}