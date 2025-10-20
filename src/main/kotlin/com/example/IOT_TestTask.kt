package com.example

import com.example.network.SpringHttpClient.sendMessageToSpring
import com.example.network.payload.MessageC2SPayload
import com.example.network.payload.MessageS2CPayload
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

object IOT_TestTask : ModInitializer {

    private val logger = LoggerFactory.getLogger("iot_testtask")

    override fun onInitialize() {
        // Регистрируем пакет
        PayloadTypeRegistry.playC2S().register(
            MessageC2SPayload.ID,
            MessageC2SPayload.CODEC
        )

        PayloadTypeRegistry.playS2C().register(
            MessageS2CPayload.ID,
            MessageS2CPayload.CODEC
        )

        ServerPlayNetworking.registerGlobalReceiver(MessageC2SPayload.ID) { payload, context ->
            val messageText = payload.messageText
            val server = context.server()
            val player = context.player()

            server.execute {

                val responsePayload = MessageS2CPayload("Сервер получил: '$messageText'")
                ServerPlayNetworking.send(player, responsePayload)

                sendMessageToSpring(player.uuid, messageText)

                player.sendMessage(
                    Text.literal("✅ Сообщение получено сервером: \"$messageText\""),
                    false
                )
            }
        }

        println("✅ IOT Mod initialized")
    }
}