package com.example.esp32notif

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
        }
        val info = TextView(this).apply {
            text = "1. Autorise les notifications\n2. Autorise le Bluetooth\n3. Connecte-toi à l'ESP32"
            textSize = 16f
        }
        val btnNotif = Button(this).apply {
            text = "Activer accès notifications"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }
        val btnBt = Button(this).apply {
            text = "Autoriser Bluetooth"
            setOnClickListener { requestBtPermissions() }
        }
        val btnConnect = Button(this).apply {
            text = "Connecter à l'ESP32"
            setOnClickListener {
                BtManager.connect(applicationContext)
            }
        }
        layout.addView(info)
        layout.addView(btnNotif)
        layout.addView(btnBt)
        layout.addView(btnConnect)
        setContentView(layout)

        requestBtPermissions()
    }

    private fun requestBtPermissions() {
        val perms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
        } else {
            arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)
        }
        ActivityCompat.requestPermissions(this, perms, 1)
    }
}
