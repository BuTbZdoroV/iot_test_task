package com.example.network

import message
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit

object SpringHttpClient {
    private const val SPRING_URL = "http://localhost:8080"
    private val PROTOBUF_MEDIA_TYPE = "application/x-protobuf".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    fun sendMessageToSpring(playerUuid: UUID, message: String) {
        try {
            val protobufMessage = message {
                this.uuid = playerUuid.toString()
                this.message = message
            }

            // Преобразуем в ByteArray и создаем RequestBody с помощью extension function
            val requestBody = protobufMessage.toByteArray().toRequestBody(PROTOBUF_MEDIA_TYPE)

            val request = Request.Builder()
                .url("$SPRING_URL/api/v1/messages/sendMessage")
                .post(requestBody) // используем созданный requestBody
                .addHeader("Content-Type", "application/x-protobuf")
                .addHeader("Accept", "application/x-protobuf")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("❌ Ошибка отправки в Spring: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        println(response.body!!.string())
                        println("✅ Сообщение отправлено в Spring: ${response.code}")
                    } else {
                        println("❌ Ошибка Spring сервера: ${response.code} - ${response.body?.string()}")
                    }
                    response.close()
                }
            })

        } catch (e: Exception) {
            println("❌ Ошибка создания Protobuf сообщения: ${e.message}")
            e.printStackTrace()
        }
    }
}