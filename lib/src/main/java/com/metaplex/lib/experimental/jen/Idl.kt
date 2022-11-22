/*
 * IDL Models (for serialization)
 * metaplex-android
 *
 * Created by Funkatronics on 8/9/2022
 */
package com.metaplex.lib.experimental.jen

import com.solana.core.PublicKey
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlin.reflect.KClass

@Serializable
data class Idl(val name: String, val version: String, val instructions: List<Instruction>,
               val accounts: List<Account>, val types: List<Type>, val errors: List<Error>,
               val metadata: IdlMetadata? = null)

//region Instructions
@Serializable
data class Instruction(val name: String, val accounts: List<AccountInput>,
                       val args: List<Argument>, val docs: List<String>? = null,
                       val discriminant: Discriminant? = null)

@Serializable
data class AccountInput(val name: String, val isMut: Boolean, val isSigner: Boolean,
                        val desc: String? = null, val optional: Boolean = false, val docs: List<String>? = null)

@Serializable
data class Argument(val name: String, @Serializable(with = FTSerializer::class) val type: FieldType)

@Serializable
data class Discriminant(@Serializable(with = FTSerializer::class) val type: FieldType, val value: JsonElement)
//endregion

//region Accounts
@Serializable
data class Account(val name: String, val type: AccountType, val docs: List<String>? = null)

@Serializable
data class AccountType(val kind: String, val fields: List<Field>)
//endregion

//region Types
@Serializable
data class Type(val name: String, val type: TypeInfo, val docs: List<String>? = null)

@Serializable
data class TypeInfo(val kind: String, val variants: List<Variant>? = null, val fields: List<Field>? = null)

@Serializable
data class Variant(val name: String, val docs: List<String>? = null, val fields: List<Field>? = null,
                   @Serializable(with = FTSerializer::class) val type: FieldType? = null)
//endregion

@Serializable
data class Error(val code: Int, val name: String, @SerialName("msg") val message: String)

@Serializable
data class IdlMetadata(
    val address: String,
    val origin: String,
    val binaryVersion: String? = null,
    val libVersion:	String? = null
)

// Below is the serialization code to convert the idl types to usable objects for kotlin code
// generation. This code is not as clean or efficient as it could be, and has not been fully
// tested. Only Auction House and Candy Machine programs have been generated using this code
@Serializable
data class Field(val name: String, val docs: List<String>? = null,
                 @Serializable(with = FTSerializer::class) val type: FieldType? = null)

sealed class FieldType
data class PrimitiveField(val name: String) : FieldType()
data class ListField(val vec: FieldType) : FieldType()
data class NullableField(val option: FieldType) : FieldType()

val PrimitiveField.type get() = typeMap[name]

private val typeMap = mapOf<String, KClass<*>>(
    "publicKey" to PublicKey::class,
    "publickey" to PublicKey::class,
    "bytes" to ByteArray::class,
    "bool" to Boolean::class,
    "string" to String::class,
    "u8" to UByte::class,
    "u16" to UShort::class,
    "u32" to UInt::class,
    "u64" to ULong::class,
    "i8" to Byte::class,
    "i16" to Short::class,
    "i32" to Int::class,
    "i64" to Long::class,
    "f32" to Float::class,
    "f64" to Double::class,
)

internal object FTSerializer : KSerializer<FieldType> {
    private val surrogateSerializer = JsonElement.serializer()
    override val descriptor = surrogateSerializer.descriptor
    override fun serialize(encoder: Encoder, value: FieldType) = TODO("Not implemented")
    override fun deserialize(decoder: Decoder): FieldType =
        parseJson(decoder.decodeSerializableValue(surrogateSerializer))

    private fun parseJson(json: JsonElement): FieldType {
        runCatching {
            return when (json.jsonObject.keys.first()) {
                "vec" -> ListField(parseJson(json.jsonObject["vec"]!!))
                "array" -> ListField(parseJson(json.jsonObject["array"]?.jsonArray?.first()!!))
                "option" -> NullableField(parseJson(json.jsonObject["option"]!!))
                "defined" -> PrimitiveField(json.jsonObject["defined"]?.jsonPrimitive?.content!!)
                else -> throw Error()
            }
        }.getOrElse {
            // base case
            return PrimitiveField(json.jsonPrimitive.content)
        }
    }
}