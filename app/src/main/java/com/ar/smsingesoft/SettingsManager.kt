package com.ar.smsingesoft

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.ar.smsingesoft.R

class SettingsManager {

    var isSendEnabled: Boolean = false
    var interval: Int = 1
    var sendURL: String = ""
    var deviceId: String = ""
    var deviceSecret: String = ""
    var sharedPref: SharedPreferences
    var context: Context

    constructor(_context: Context) {
        context = _context
        sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        this.updateSettings()
    }

    fun updateSettings(){

        val defaultSendEnabled = context.resources.getBoolean(R.bool.preference_default_send_enabled)
        val defaultInterval = context.resources.getInteger(R.integer.preference_default_interval)
        val defaultSendURL = context.resources.getString(R.string.preference_default_send_url)
        val defaultDeviceId = context.resources.getString(R.string.preference_default_device_id)
        val defaultdeviceSecret = context.resources.getString(R.string.preference_default_device_secret)

        this.isSendEnabled = sharedPref!!.getBoolean(context.getString(R.string.preference_send_enabled), defaultSendEnabled)
        this.interval = sharedPref!!.getInt(context.getString(R.string.preference_interval), defaultInterval)
        this.sendURL = sharedPref!!.getString(context.getString(R.string.preference_send_url), defaultSendURL)
        this.deviceId = sharedPref!!.getString(context.getString(R.string.preference_device_id), defaultDeviceId)
        this.deviceSecret = sharedPref!!.getString(context.getString(R.string.preference_device_secret), defaultdeviceSecret)
    }

    fun setSettings(_isSendEnabled: Boolean, _interval: Int, _sendURL: String, _deviceId: String, _deviceSecret: String){
        this.interval = _interval
        this.sendURL = _sendURL
        this.deviceId = _deviceId
        this.deviceSecret = _deviceSecret
        this.isSendEnabled = _isSendEnabled
        with(sharedPref!!.edit()) {
            putBoolean(context.getString(R.string.preference_send_enabled), _isSendEnabled)
            putString(context.getString(R.string.preference_send_url), _sendURL)
            putString(context.getString(R.string.preference_device_id), _deviceId)
            putString(context.getString(R.string.preference_device_secret), _deviceSecret)

            putInt(context.getString(R.string.preference_interval), _interval)
            commit()
            Log.d("--->_isSendEnabled", _isSendEnabled.toString())
            Toast.makeText(context, "La configuración ha sido cambiada", Toast.LENGTH_SHORT).show()
        }
        this.updateSettings()
    }

}