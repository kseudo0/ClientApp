package com.example.clientapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private lateinit var socket: Socket
    private lateinit var tvMessage: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val send = findViewById<Button>(R.id.btnSend)
        tvMessage = findViewById<TextView>(R.id.tvMessage)
        send.setOnClickListener {
            val message = "You are what you do"
            GlobalScope.launch {
                sendMessage(message)
            }
        }
    }

    private suspend fun sendMessage(message: String) {
        try {
            val serverAddress = "192.168.0.213" // Replace with your server IP address
            val serverPort = 2345
            socket = Socket(serverAddress, serverPort)
            val outputStream: OutputStream = socket.getOutputStream()
            outputStream.write(message.toByteArray())
            outputStream.flush()
            socket.close()
            socket = Socket(serverAddress, serverPort)
            val inputReader = BufferedReader(InputStreamReader(socket.getInputStream()))

            while (true) {
                val clientMessage = inputReader.readLine() ?: break
                runOnUiThread {
                    tvMessage.text = clientMessage
                    socket.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}