/*
 * JenPoC
 * Metaplex
 * 
 * Created by Funkatronics on 8/10/2022
 */

package com.metaplex.lib.experimental.jen

import com.metaplex.lib.experimental.jen.auctionhouse.auctionHouseJson
import com.metaplex.lib.experimental.jen.candymachine.candyMachineJson
import com.metaplex.lib.experimental.serialization.format.Borsh
import com.metaplex.lib.experimental.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.io.File
import java.lang.reflect.Parameter
import java.time.LocalDate
import kotlin.reflect.KClass

/*
 * Code generation from IDL json Proof of Concept/Spike
 *
 * This code was purpose built for auction house, it will not work for other programs IDLs in its
 * current state (hard coded names, types etc). We will build this out into a functional library
 * and gradle plugin eventually but for now it is just an internal/experimental tool.
 *
 * usage: call jenerateAuctionHouse() from somewhere (like a test script), and the code files
 * will be generated in the package lib.experimental.jen.auctionhouse
 */
fun jenerateAuctionHouse() {

    // this was purpose built for auction house. Next step is to make this work for any idl
    val auctionHouseIdl: Idl = Json.decodeFromString(auctionHouseJson)

    FileSpec.builder("com.metaplex.lib.experimental.jen.auctionhouse",
        "Instructions").apply {

        addMyHeader()

        addType(
            TypeSpec.objectBuilder("AuctionHouseInstructions").apply {
                auctionHouseIdl.instructions.forEach { instruction ->

                    // Add args class so we can easily serialize the args into a byte array later
                    val argsClassName = "Args_${instruction.name}"
                    addType(TypeSpec.classBuilder(argsClassName)
                        .addAnnotation(AnnotationSpec.builder(Serializable::class).build())
                        .primaryConstructor(FunSpec.constructorBuilder().apply {
                            instruction.args.forEach {
                                    addParameter(it.name, it.type.jenType)
                            }
                        }.build())
                        .addProperties(instruction.args.map {
                                PropertySpec.builder(it.name, it.type.jenType)
                                    .initializer(it.name)
                                    .build()
                        })
                        .build()
                    )

                    // Add instruction methods
                    addFunction(
                        FunSpec.builder(instruction.name).apply {

                            returns(TransactionInstruction::class)

                            instruction.accounts.forEach { account ->
                                addParameter(account.name, PublicKey::class)
                            }

                            instruction.args.forEach {
                                addParameter(it.name, it.type.jenType)
                            }

                            val programId = "%2T(\"${AuctionHouse.PROGRAM_ID}\")"
                            val keys = "listOf(${
                                instruction.accounts.joinToString {
                                    "%3T(${it.name}, ${it.isSigner}, ${it.isMut})"
                                }
                            })"

                            val data = "%4T.encodeToByteArray($argsClassName.serializer(), " +
                                    "$argsClassName(${instruction.args.joinToString { it.name }}))"

                            addStatement("return %1T($programId, $keys, $data)",
                                TransactionInstruction::class, PublicKey::class,
                                AccountMeta::class, Borsh::class)
                        }.build()
                    )
                }
            }.build()
        )
    }.build().writeTo(File("src/main/java"))

    FileSpec.builder("com.metaplex.lib.experimental.jen.auctionhouse",
        "Accounts").apply {

        addMyHeader()

        addAnnotation(AnnotationSpec.builder(UseSerializers::class)
            .addMember("%T::class", PublicKeyAs32ByteSerializer::class).build())

        auctionHouseIdl.accounts.forEach { account ->

            if (account.type.kind == "struct")
                addType(TypeSpec.classBuilder(account.name)
                    .addAnnotation(AnnotationSpec.builder(Serializable::class).build())
                    .primaryConstructor(FunSpec.constructorBuilder().apply {
                        account.type.fields.forEach { field ->
                            println("Building Accounts: field type = ${field.type}")
                            field.type?.jenType?.let {
                                addParameter(field.name, it)
                            }
                        }
                    }.build())
                    .addProperties(account.type.fields.map { it.type?.jenType?.let { type ->
                        PropertySpec.builder(it.name, type)
                            .initializer(it.name)
                            .build()
                    } }.filterNotNull())
                    .build()
                )
        }

    }.build().writeTo(File("src/main/java"))

    FileSpec.builder("com.metaplex.lib.experimental.jen.auctionhouse",
        "Types").apply {

        addMyHeader()

        auctionHouseIdl.types.forEach { type ->

            if (type.type.kind == "enum")
                addType(TypeSpec.enumBuilder(type.name).apply {
                    type.type.variants?.forEach {
                        addEnumConstant(it.name)
                    }
                }.build()
                )
        }

    }.build().writeTo(File("src/main/java"))

    FileSpec.builder("com.metaplex.lib.experimental.jen.auctionhouse",
        "Errors").apply {

        addMyHeader()

        addType(TypeSpec.interfaceBuilder("AuctionHouseError")
            .addModifiers(KModifier.SEALED)
            .addProperty("code", Int::class)
            .addProperty("message", String::class)
            .build())

        auctionHouseIdl.errors.forEach { error ->

            addType(TypeSpec.classBuilder(error.name)
                .addSuperinterface(ClassName("com.metaplex.lib.experimental.jen.auctionhouse",
                    "AuctionHouseError"))
                .addProperty(PropertySpec.builder("code", Int::class, KModifier.OVERRIDE)
                    .initializer("%L", error.code).build())
                .addProperty(PropertySpec.builder("message", String::class, KModifier.OVERRIDE)
                    .initializer("%S", error.message).build())
                .build())
        }

    }.build().writeTo(File("src/main/java"))
}

/*
 * Code generation from IDL json Proof of Concept/Spike
 *
 * This code was purpose built for candy machine, it will not work for other programs IDLs in its
 * current state (hard coded names, types etc). We will build this out into a functional library
 * and gradle plugin eventually but for now it is just an internal/experimental tool.
 *
 * usage: call jenerateCandyMachine() from somewhere (like a test script), and the code files
 * will be generated in the package lib.experimental.jen.candymachine
 */
fun jenerateCandyMachine() {

    // this was purpose built for auction house. Next step is to make this work for any idl
    val candyMachineIdl: Idl = Json.decodeFromString(candyMachineJson)

    FileSpec.builder("com.metaplex.lib.experimental.jen.candymachine",
        "Instructions").apply {

        addMyHeader()

//        addAnnotation(AnnotationSpec.builder(UseSerializers::class)
//            .addMember("%T::class", PublicKeyAs32ByteSerializer::class).build())

        addType(
            TypeSpec.objectBuilder("CandyMachineInstructions").apply {
                candyMachineIdl.instructions.forEach { instruction ->

                    // Add args class so we can easily serialize the args into a byte array later
                    val argsClassName = "Args_${instruction.name}"
                    addType(TypeSpec.classBuilder(argsClassName)
                        .addAnnotation(AnnotationSpec.builder(Serializable::class).build())
                        .primaryConstructor(FunSpec.constructorBuilder().apply {
                            instruction.args.forEach {
                                addParameter(ParameterSpec.builder(it.name, it.type.jenType).apply {

                                    // crude example of how we can specify a serializer for a property
                                    // alternatively we can just add a file level annotation (@file:UseSerializers(...))
                                    if (it.type.jenType.toString().contains(PublicKey::class.asClassName().toString()))
                                        addAnnotation(AnnotationSpec.builder(Serializable::class)
                                            .addMember("with = %T::class", PublicKeyAs32ByteSerializer::class)
                                            .build())
                                }.build())
                            }
                        }.build())
                        .addProperties(instruction.args.map {
                            PropertySpec.builder(it.name, it.type.jenType)
                                .initializer(it.name)
                                .build()
                        })
                        .build()
                    )

                    // Add instruction methods
                    addFunction(
                        FunSpec.builder(instruction.name).apply {

                            returns(TransactionInstruction::class)

                            instruction.accounts.forEach { account ->
                                addParameter(account.name, PublicKey::class)
                            }

                            instruction.args.forEach {
                                addParameter(it.name, it.type.jenType)
                            }

                            val programId = "%2T(\"${candyMachineIdl.metadata?.address}\")"
                            val keys = "listOf(${
                                instruction.accounts.joinToString {
                                    "%3T(${it.name}, ${it.isSigner}, ${it.isMut})"
                                }
                            })"

                            val data = "%4T.encodeToByteArray($argsClassName.serializer(), " +
                                    "$argsClassName(${instruction.args.joinToString { it.name }}))"

                            addStatement("return %1T($programId, $keys, $data)",
                                TransactionInstruction::class, PublicKey::class,
                                AccountMeta::class, Borsh::class)
                        }.build()
                    )
                }
            }.build()
        )
    }.build().writeTo(File("src/main/java"))

    FileSpec.builder("com.metaplex.lib.experimental.jen.candymachine",
        "Accounts").apply {

        addMyHeader()

        addAnnotation(AnnotationSpec.builder(UseSerializers::class)
            .addMember("%T::class", PublicKeyAs32ByteSerializer::class).build())

        candyMachineIdl.accounts.forEach { account ->

            if (account.type.kind == "struct")
                addType(TypeSpec.classBuilder(account.name)
                    .addAnnotation(AnnotationSpec.builder(Serializable::class).build())
                    .primaryConstructor(FunSpec.constructorBuilder().apply {
                        account.type.fields.forEach { field ->
                            println("Building Accounts: field type = ${field.type}")
                            field.type?.jenType?.let {
                                addParameter(field.name, it)
                            }
                        }
                    }.build())
                    .addProperties(account.type.fields.map { it.type?.jenType?.let { type ->
                        PropertySpec.builder(it.name, type)
                            .initializer(it.name)
                            .build()
                    } }.filterNotNull())
                    .build()
                )
        }

    }.build().writeTo(File("src/main/java"))

    FileSpec.builder("com.metaplex.lib.experimental.jen.candymachine",
        "Types").apply {

        addMyHeader()

        addAnnotation(AnnotationSpec.builder(UseSerializers::class)
            .addMember("%T::class", PublicKeyAs32ByteSerializer::class).build())

        candyMachineIdl.types.forEach { type ->

            if (type.type.kind == "enum")
                addType(TypeSpec.enumBuilder(type.name).apply {
                    type.type.variants?.forEach {
                        addEnumConstant(it.name)
                    }
                }.build())

            if (type.type.kind == "struct")
                addType(TypeSpec.classBuilder(type.name).apply {
                    addModifiers(KModifier.DATA)
                    addAnnotation(Serializable::class)
                    primaryConstructor(FunSpec.constructorBuilder().apply {
                        type.type.fields?.forEach { field ->
                            field.type?.jenType?.let {
                                addParameter(field.name, it)
                            }
                        }
                    }.build())

                    type.type.fields?.forEach { field ->
                        field.type?.jenType?.let {
                            addProperty(PropertySpec.builder(field.name, it)
                                .initializer(field.name)
                                .build())
                        }
                    }
                }.build())
        }

    }.build().writeTo(File("src/main/java"))

    FileSpec.builder("com.metaplex.lib.experimental.jen.candymachine",
        "Errors").apply {

        addMyHeader()

        addType(TypeSpec.interfaceBuilder("CandyMachineError")
            .addModifiers(KModifier.SEALED)
            .addProperty("code", Int::class)
            .addProperty("message", String::class)
            .build())

        candyMachineIdl.errors.forEach { error ->

            addType(TypeSpec.classBuilder(error.name)
                .addSuperinterface(ClassName("com.metaplex.lib.experimental.jen.candymachine",
                    "CandyMachineError"))
                .addProperty(PropertySpec.builder("code", Int::class, KModifier.OVERRIDE)
                    .initializer("%L", error.code).build())
                .addProperty(PropertySpec.builder("message", String::class, KModifier.OVERRIDE)
                    .initializer("%S", error.message).build())
                .build())
        }

    }.build().writeTo(File("src/main/java"))
}

private fun FileSpec.Builder.addMyHeader() {
    addComment("""
            
             ${name}
             Metaplex
            
             This code was generated locally by Funkatronics on ${LocalDate.now()}
             
        """.trimIndent())
}

// disgusting manual parsing of the types - this will be replaced by KSerializers/beet
val JsonElement.jenType: TypeName get() =
    runCatching {
        List::class.asClassName().parameterizedBy(ClassName("com.metaplex.lib.experimental.jen.candymachine",
                jsonObject["vec"]?.jsonObject?.get("defined")?.jsonPrimitive?.content!!))
    }.getOrNull() ?: runCatching {
        typeMap[jsonObject["option"].toString().replace("\"", "")]!!.asClassName().copy(nullable = true)
    }.getOrNull() ?: (typeMap[
            runCatching {
                jsonPrimitive
            }.getOrElse {
                // else, need to deal with other types
                JsonPrimitive("i32")
            }.toString().replace("\"", "")
    ] ?: Int::class).asClassName()

val typeMap = mapOf<String, KClass<*>>(
    "publicKey" to PublicKey::class,
    "publickey" to PublicKey::class,
    "bool" to Boolean::class,
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