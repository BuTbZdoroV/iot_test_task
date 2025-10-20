// network/MessageC2SPayload.kt
package com.example.network.payload

import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

data class MessageS2CPayload(
    val messageText: String
) : CustomPayload {

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }

    companion object {
        val MESSAGE_PAYLOAD_ID: Identifier = Identifier.of("iot_mod", "send_message")
        val ID: CustomPayload.Id<MessageS2CPayload> = CustomPayload.Id(MESSAGE_PAYLOAD_ID)
        
        val CODEC: PacketCodec<RegistryByteBuf, MessageS2CPayload> =
            PacketCodec.tuple(
                PacketCodecs.STRING,     
                MessageS2CPayload::messageText,
                ::MessageS2CPayload
            )
    }
}