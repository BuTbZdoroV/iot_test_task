// network/MessageC2SPayload.kt
package com.example.network.payload

import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

data class MessageC2SPayload(
    val messageText: String
) : CustomPayload {

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }

    companion object {
        val MESSAGE_PAYLOAD_ID: Identifier = Identifier.of("iot_mod", "send_message")
        val ID: CustomPayload.Id<MessageC2SPayload> = CustomPayload.Id(MESSAGE_PAYLOAD_ID)
        
        val CODEC: PacketCodec<RegistryByteBuf, MessageC2SPayload> =
            PacketCodec.tuple(
                PacketCodecs.STRING,     
                MessageC2SPayload::messageText,
                ::MessageC2SPayload
            )
    }
}