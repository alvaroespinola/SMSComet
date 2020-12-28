package com.ar.smsingesoft

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.beust.klaxon.Klaxon
import java.util.*
import khttp.responses.Response
import java.io.StringReader

class SMS(val message: String, val number: String, val messageId: String )

class SendTask constructor(_settings: SettingsManager, _context: Context) : TimerTask() {
    var settings = _settings
    var mainActivity: MainActivity = _context as MainActivity

    override fun run() {

        lateinit var apiResponse : Response
        try {
            apiResponse = khttp.post(
                url = settings.sendURL,
                data = mapOf(
                    "deviceId" to settings.deviceId,
                    "deviceSecret" to settings.deviceSecret,
                    "action" to "SEND"
                )
            )
            mainActivity.runOnUiThread(Runnable {
                mainActivity.msgShow("Estoy buscando mensajes en la web")
            })
        } catch (e: Exception) {
            Log.d("-->", "Cannot connect to URL")
            return
        }
        //var sms: SMS? = SMS("", "", "")
        var smsArray: List<SMS>? = emptyList()
        var canSend: Boolean = false
        try {
            val klaxon = Klaxon()
            //val parsed = klaxon.parseJsonObject(StringReader(apiResponse.text))
            //val dataArray = parsed.array<Any>("data")
            //smsArray = klaxon.parseArray<SMS>(apiResponse.text)
            smsArray = klaxon.parseArray<SMS>(apiResponse.text)
            canSend = true
        } catch (e: com.beust.klaxon.KlaxonException) {
            if (apiResponse.text == "") {
                mainActivity.runOnUiThread(Runnable {
                    //mainActivity.logMain(".", false)
                    mainActivity.logMain("No hay mensajes para enviar en la identificación del equipo#" + settings.deviceId)
                })
                Log.d("-->", "Nothing")
            } else {
                mainActivity.runOnUiThread(Runnable {
                    mainActivity.logMain("Error al analizar la respuesta del servidor:\n" + apiResponse.text + "\n" + e.message)
                })
                Log.d("error", "Error al analizar SMS" + apiResponse.text)
            }
        } finally {
            // optional finally block
        }
        if (canSend) {
            smsArray?.forEach {
                val sentIn = Intent(mainActivity.SENT_SMS_FLAG)
                settings.updateSettings()
                sentIn.putExtra("messageId", it!!.messageId)
                sentIn.putExtra("sendURL", settings.sendURL)
                sentIn.putExtra("deviceId", settings.deviceId)
                sentIn.putExtra("deviceSecret", settings.deviceSecret)
                sentIn.putExtra("delivered", 0)


                val sentPIn = PendingIntent.getBroadcast(mainActivity, mainActivity.nextRequestCode(), sentIn,0)

                val deliverIn = Intent(mainActivity.DELIVER_SMS_FLAG)
                deliverIn.putExtra("messageId", it!!.messageId)
                deliverIn.putExtra("sendURL", settings.sendURL)
                deliverIn.putExtra("deviceId", settings.deviceId)
                deliverIn.putExtra("deviceSecret", settings.deviceSecret)
                deliverIn.putExtra("delivered", 1)


                val deliverPIn = PendingIntent.getBroadcast(mainActivity, mainActivity.nextRequestCode(), deliverIn, 0)

                val smsManager = SmsManager.getDefault() as SmsManager
                smsManager.sendTextMessage(it!!.number, null, it!!.message, sentPIn, deliverPIn)
                mainActivity.runOnUiThread(Runnable {
                    mainActivity.logMain("Enviado a: " + it!!.number + " - id: " + it!!.messageId + " - Mensaje: " + it!!.message)
                })
                Log.d("-->", "Enviado!")

                Thread.sleep(500)
            }
        }


    }

}