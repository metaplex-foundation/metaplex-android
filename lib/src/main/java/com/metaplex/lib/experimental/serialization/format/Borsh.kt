@file:OptIn(ExperimentalSerializationApi::class)

package com.metaplex.lib.experimental.serialization.format

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.modules.EmptySerializersModule
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

sealed class Borsh : BinaryFormat {

    companion object Default : Borsh()

    override val serializersModule = EmptySerializersModule

    override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>,
                                         bytes: ByteArray): T =
        BorshDecoder(bytes).decodeSerializableValue(deserializer)

    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray =
        BorshEncoder().apply { encodeSerializableValue(serializer, value) }.borshEncodedBytes
}

class BorshDecoder(val bytes: ByteArray) : AbstractDecoder() {

    private val byteBuffer = ByteBuffer.wrap(bytes).apply {
        order(ByteOrder.LITTLE_ENDIAN)
        getLong() // consume the account discriminator (we don't need it)
    }

    override val serializersModule = EmptySerializersModule

    // Not called for sequential decoders
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = 0
    override fun decodeSequentially(): Boolean = true
    override fun decodeNotNullMark(): Boolean = decodeBoolean()

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = decodeInt()
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = decodeInt()

    override fun decodeBoolean(): Boolean = byteBuffer.get().toInt() != 0
    override fun decodeByte(): Byte = byteBuffer.get()
    override fun decodeShort(): Short = byteBuffer.short
    override fun decodeInt(): Int = byteBuffer.int
    override fun decodeLong(): Long = byteBuffer.long
    override fun decodeFloat(): Float = byteBuffer.float
    override fun decodeDouble(): Double = byteBuffer.double
    override fun decodeChar(): Char = byteBuffer.char
    override fun decodeString(): String {
        val length = byteBuffer.int
        val bytes = ByteArray(length)
        byteBuffer.get(bytes)
        return String(bytes.filter { it.toInt() != 0 }.toByteArray(), StandardCharsets.UTF_8)
    }
}

class BorshEncoder : AbstractEncoder() {
    // TODO: need to figure out how to compute the account discriminator,
    //  for now we are just putting all zeros for the discriminator
    private val bytes = mutableListOf<Byte>(0, 0, 0, 0, 0, 0, 0, 0)

    val borshEncodedBytes get() = bytes.toByteArray()

    override val serializersModule = EmptySerializersModule

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) =
        encodeBytes(numberToByteArray(index, 4))

    override fun encodeNull() = encodeByte(0)
    override fun encodeByte(value: Byte) = run { bytes.add(value); Unit }
    override fun encodeBoolean(value: Boolean) = encodeByte(if (value) 1 else 0)
    override fun encodeShort(value: Short) = encodeBytes(numberToByteArray(value, 2))
    override fun encodeInt(value: Int) = encodeBytes(numberToByteArray(value, 4))
    override fun encodeLong(value: Long) = encodeBytes(numberToByteArray(value, 8))
    override fun encodeFloat(value: Float) = encodeBytes(numberToByteArray(value, 4))
    override fun encodeDouble(value: Double) = encodeBytes(numberToByteArray(value, 8))
    override fun encodeChar(value: Char) =
        encodeBytes(numberToByteArray(value.code.toShort(), 2))

    override fun encodeString(value: String) {
        value.toByteArray(StandardCharsets.UTF_8).apply {
            encodeInt(size)
            encodeBytes(this)
        }
    }

    //region PRIVATE METHODS
    private fun encodeBytes(bytes: ByteArray) = bytes.forEach { b -> encodeByte(b) }
    private fun numberToByteArray(number: Number, sizeBytes: Int) =
        ByteArray(sizeBytes).apply {
            (0 until sizeBytes).forEach { b ->
                this[b] = ((number.toInt() shr (b*8)) and 0xFF).toByte()
            }
        }
    //endregion
}

