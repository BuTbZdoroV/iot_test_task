package com.example

import com.example.network.payload.MessageS2CPayload
import com.example.render.screen.CustomScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.toast.SystemToast
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW


object IOT_TestTaskClient : ClientModInitializer {

    private lateinit var openGuiKey: KeyBinding

	override fun onInitializeClient() {

        ClientPlayNetworking.registerGlobalReceiver(MessageS2CPayload.ID) { payload, context ->
            val messageText = payload.messageText
            val client = context.client()

            println("📩 IOT Client: Получен S2C пакет: '$messageText'")

            client?.player?.sendMessage(
                Text.literal("📨 Сервер: $messageText"),
                false
            )

            client?.toastManager?.add(
                SystemToast.create(
                    client,
                    SystemToast.Type.PERIODIC_NOTIFICATION,
                    Text.literal("📨 Сообщение от сервера"),
                    Text.literal(messageText)
                )
            )
        }

        openGuiKey = KeyBindingHelper.registerKeyBinding(KeyBinding(
            "key.iot_test_task.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H, // Код клавиши G
            "category.iot_test_task.general"
        ))

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (openGuiKey.wasPressed()) {
                client.setScreen(CustomScreen(null))
            }
        }
	}
}