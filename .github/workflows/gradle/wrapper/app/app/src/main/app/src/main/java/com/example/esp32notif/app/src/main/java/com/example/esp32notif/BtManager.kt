package com.example.esp32notif

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import java.io.OutputStream
import java.util.UUID

object BtManager {
    private const val TAG = "BtManager"
    private const val TARGET_NAME = "ESP32_NOTIF"
    private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private var socket: BluetoothSocket? = null
    private var output: OutputStream? = null

    @SuppressLint("MissingPermission")
    fun connect(context: Context) {
        Thread {
            try {
                val adapter = BluetoothAdapter.getDefaultAdapter() ?: return@Thread
                val device = adapter.bondedDevices?.firstOrNull { it.name == TARGET_NAME }
                if (device == null) {
                    Log.e(TAG, "Appareil '$TARGET_NAME' non appairé")
                    return@Thread
                }
                socket?.close()
                socket = device.createRfcommSocketToServiceRecord(SPP_UUID).also { it.connect() }
                output = socket?.outputStream
                Log.i(TAG, "Connecté à $TARGET_NAME")
                send("App connectée\n")
            } catch (e: Exception) {
                Log.e(TAG, "Erreur connexion: ${e.message}")
            }
        }.start()
    }

    fun send(text: String) {
        try {
            output?.let {
                it.write(text.toByteArray())
                it.flush()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erreur envoi: ${e.message}")
            socket = null
            output = null
        }
    }

    fun isConnected(): Boolean = socket?.isConnected == true
}
