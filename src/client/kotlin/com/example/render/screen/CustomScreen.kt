package com.example.render.screen

import com.example.network.payload.MessageC2SPayload
import com.example.render.utils.BLUE
import com.example.render.utils.createColor
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.toast.SystemToast
import net.minecraft.text.Text

class CustomScreen(
    var parent: Screen?,
) : Screen(
    Text.literal("Custom Screen")
) {

    private lateinit var textField: TextFieldWidget
    private lateinit var sendButton: ButtonWidget

    override fun init() {
        super.init()


        textField = TextFieldWidget(
            this.textRenderer,
            width / 2 - 150,
            height / 2 - 30,
            300,
            20,
            Text.literal("Введите сообщение...")
        )
        textField.setPlaceholder(Text.literal("Введите ваше сообщение здесь..."))
        textField.setMaxLength(256)

        sendButton = ButtonWidget.builder(Text.literal("📨 Отправить")) { button ->
            onSendButtonPressed()
        }.dimensions(
            width / 2 - 75,
            height / 2 + 10,
            150,
            20
        ).build()

        this.addDrawableChild(textField)
        this.addDrawableChild(sendButton)

        setInitialFocus(textField)

    }

    private fun onSendButtonPressed() {
        val messageText = textField.text.trim()

        if (messageText.isNotEmpty()) {

            val payload = MessageC2SPayload(messageText)

            ClientPlayNetworking.send(payload)

            this.client?.toastManager?.add(
                SystemToast.create(
                    this.client,
                    SystemToast.Type.PERIODIC_NOTIFICATION,
                    Text.literal("Сообщение отправлено"),
                    Text.literal(messageText)
                )
            )

            textField.text = ""
        } else {
            this.client?.toastManager?.add(
                SystemToast.create(
                    this.client,
                    SystemToast.Type.UNSECURE_SERVER_WARNING,
                    Text.literal("❌ Ошибка"),
                    Text.literal("Сообщение не может быть пустым")
                )
            )
        }
    }

    override fun render(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        deltaTicks: Float
    ) {

        super.render(context, mouseX, mouseY, deltaTicks)

        context.drawText(this.textRenderer, "Special Button", width / 2, height / 2, BLUE, true)
        context.drawText(this.textRenderer, mouseX.toString(), 0, 0, createColor(255, 255, 255), true)
        context.drawText(this.textRenderer, mouseY.toString(), 20, 0, createColor(255, 255, 255), true)
    }


    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        // Enter
        if (keyCode == 257 && textField.isFocused) {
            onSendButtonPressed()
            return true
        }

        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun close() {
        this.client?.setScreen(parent)
    }
}