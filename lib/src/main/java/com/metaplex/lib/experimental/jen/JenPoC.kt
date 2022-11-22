/*
 * JenPoC
 * Metaplex
 * 
 * Created by Funkatronics on 8/10/2022
 */

package com.metaplex.lib.experimental.jen

import com.metaplex.kborsh.Borsh
import com.metaplex.lib.experimental.jen.auctionhouse.auctionHouseJson
import com.metaplex.lib.experimental.jen.candyguard.candyGuardJson
import com.metaplex.lib.experimental.jen.candymachinev2.candyMachineJson
import com.metaplex.lib.experimental.jen.candymachine.candyCoreJson
import com.metaplex.lib.experimental.jen.tokenmetadata.tokenMetadataJson
import com.metaplex.lib.serialization.serializers.solana.AnchorInstructionSerializer
import com.metaplex.lib.serialization.serializers.solana.ByteDiscriminatorSerializer
import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
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
import java.time.LocalDate
import java.util.*

/*
 * Code generation from IDL json Proof of Concept/Spike
 *
 * usage: call jenerateCandyGuard() from somewhere (like a test script), and the code files
 * will be generated in the package lib.experimental.jen.candyguard
 */
fun jenerateTokenMetadata() = jenerate("TokenMetadata", tokenMetadataJson)

/*
 * Code generation from IDL json Proof of Concept/Spike
 *
 * usage: call jenerateAuctionHouse() from somewhere (like a test script), and the code files
 * will be generated in the package lib.experimental.jen.auctionhouse
 */
fun jenerateAuctionHouse() = jenerate("AuctionHouse", auctionHouseJson)

/*
 * Code generation from IDL json Proof of Concept/Spike
 *
 * usage: call jenerateCandyMachine() from somewhere (like a test script), and the code files
 * will be generated in the package lib.experimental.jen.candymachine
 */
fun jenerateCandyMachineV2() = jenerate("CandyMachineV2", candyMachineJson)

/*
 * Code generation from IDL json Proof of Concept/Spike
 *
 * usage: call jenerateCandyCore() from somewhere (like a test script), and the code files
 * will be generated in the package lib.experimental.jen.candymachinecore
 */
fun jenerateCandyMachine() = jenerate("CandyMachine", candyCoreJson)

/*
 * Code generation from IDL json Proof of Concept/Spike
 *
 * usage: call jenerateCandyGuard() from somewhere (like a test script), and the code files
 * will be generated in the package lib.experimental.jen.candyguard
 */
fun jenerateCandyGuard() = jenerate("CandyGuard", candyGuardJson)

/*
 * Code generation from IDL json Proof of Concept/Spike
 *
 * usage: call jenerate() from somewhere (like a test script), passing in the program IDL and
 * the desired name (used for the generated files/objects). The code files will be generated
 * in the package lib.experimental.jen.candymachinecore
 */
private fun jenerate(programName: String, idl: String) {

    packageName = "com.metaplex.lib.experimental.jen.${programName.lowercase()}"

    val programIdl: Idl = Json.decodeFromString(idl)

    FileSpec.builder(packageName, "Instructions").apply {

        addMyHeader()

//        addAnnotation(AnnotationSpec.builder(UseSerializers::class)
//            .addMember("%T::class", PublicKeyAs32ByteSerializer::class).build())

        addType(
            TypeSpec.objectBuilder("${programName}Instructions").apply {
                programIdl.instructions.forEach { instruction ->

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

                            val programId = "%2T(\"${programIdl.metadata?.address}\")"
                            val keys = "listOf(${
                                instruction.accounts.joinToString {
                                    "%3T(${it.name}, ${it.isSigner}, ${it.isMut})"
                                }
                            })"

                            instruction.discriminant?.let {

                                val data = "%4T.encodeToByteArray(%5T(${it.value.jsonPrimitive.int.toByte()}), " +
                                        "$argsClassName(${instruction.args.joinToString { it.name }}))"

                                addStatement("return %1T($programId, $keys, $data)",
                                    TransactionInstruction::class, PublicKey::class, AccountMeta::class,
                                    Borsh::class, ByteDiscriminatorSerializer::class)
                            } ?: run {

                                val ixNameSnakeCase = instruction.name.snakeCase
                                val data = "%4T.encodeToByteArray(%5T(\"$ixNameSnakeCase\"), " +
                                        "$argsClassName(${instruction.args.joinToString { it.name }}))"

                                addStatement("return %1T($programId, $keys, $data)",
                                    TransactionInstruction::class, PublicKey::class, AccountMeta::class,
                                    Borsh::class, AnchorInstructionSerializer::class)
                            }
                        }.build()
                    )
                }
            }.build()
        )
    }.build().writeTo(File("src/main/java"))

    FileSpec.builder(packageName, "Accounts").apply {

        addMyHeader()

        addAnnotation(AnnotationSpec.builder(UseSerializers::class)
            .addMember("%T::class", PublicKeyAs32ByteSerializer::class).build())

        programIdl.accounts.forEach { account ->

            if (account.type.kind == "struct")
                addType(TypeSpec.classBuilder(account.name)
                    .addAnnotation(AnnotationSpec.builder(Serializable::class).build())
                    .primaryConstructor(FunSpec.constructorBuilder().apply {
                        account.type.fields.forEach { field ->
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

    FileSpec.builder(packageName, "Types").apply {

        addMyHeader()

        addAnnotation(AnnotationSpec.builder(UseSerializers::class)
            .addMember("%T::class", PublicKeyAs32ByteSerializer::class).build())

        programIdl.types.forEach { type ->

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

    FileSpec.builder(packageName, "Errors").apply {

        addMyHeader()

        addType(TypeSpec.interfaceBuilder("${programName}Error")
            .addModifiers(KModifier.SEALED)
            .addProperty("code", Int::class)
            .addProperty("message", String::class)
            .build())

        programIdl.errors.forEach { error ->

            addType(TypeSpec.classBuilder(error.name)
                .addSuperinterface(ClassName(packageName,
                    "${programName}Error"))
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

internal var packageName = ""

internal val FieldType.jenType: TypeName get() = when (this) {
    is ListField -> List::class.asClassName().parameterizedBy(vec.jenType)
    is NullableField -> option.jenType.copy(nullable = true)
    is PrimitiveField -> type?.asClassName() ?: ClassName(packageName, name)
}

internal val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

val String.snakeCase get() = replace(camelRegex) { "_${it.value}" }.lowercase(Locale.US)