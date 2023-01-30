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
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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
                    addType(
                        TypeSpec.classBuilder(argsClassName)
                            .addAnnotation(AnnotationSpec.builder(Serializable::class).build())
                            .primaryConstructor(FunSpec.constructorBuilder().apply {
                                instruction.args.forEach {
                                    addParameter(
                                        ParameterSpec.builder(it.name, it.type.jenType).apply {
                                            // crude example of how we can specify a serializer for a property
                                            // alternatively we can just add a file level annotation (@file:UseSerializers(...))
                                            if (it.type.jenType.toString().contains(
                                                    PublicKey::class.asClassName().toString()
                                                )
                                            )
                                                addAnnotation(
                                                    AnnotationSpec.builder(Serializable::class)
                                                        .addMember(
                                                            "with = %T::class",
                                                            PublicKeyAs32ByteSerializer::class
                                                        )
                                                        .build()
                                                )
                                        }.build()
                                    )
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
                                if(account.optional){
                                    addParameter(account.name, PublicKey::class.asClassName().copy(nullable = true))
                                } else {
                                    addParameter(account.name, PublicKey::class)
                                }
                            }
                            val programId = "%2T(\"${programIdl.metadata?.address}\")"

                            instruction.args.forEach {
                                addParameter(it.name, it.type.jenType)
                            }

                            addStatement("val keys = mutableListOf<%1T>()", AccountMeta::class)

                            instruction.accounts.forEach {
                                if(it.optional){
                                    addStatement("${it.name}?.let { keys.add(%1T(it, ${it.isSigner}, ${it.isMut})) }", AccountMeta::class)
                                    if(instruction.defaultOptionalAccounts == true){
                                        addStatement("      ?: run { keys.add(%1T($programId, false, false)) }", AccountMeta::class, PublicKey::class)
                                    }
                                } else {
                                    addStatement("keys.add(%1T(${it.name}, ${it.isSigner}, ${it.isMut}))", AccountMeta::class)
                                }
                            }

                            instruction.discriminant?.let {

                                val data =
                                    "%3T.encodeToByteArray(%4T(${it.value.jsonPrimitive.int.toByte()}), " +
                                            "$argsClassName(${instruction.args.joinToString { it.name }}))"

                                addStatement(
                                    "return %1T($programId, keys, $data)",
                                    TransactionInstruction::class,
                                    PublicKey::class,
                                    Borsh::class,
                                    ByteDiscriminatorSerializer::class
                                )
                            } ?: run {

                                val ixNameSnakeCase = instruction.name.snakeCase
                                val data = "%3T.encodeToByteArray(%4T(\"$ixNameSnakeCase\"), " +
                                        "$argsClassName(${instruction.args.joinToString { it.name }}))"

                                addStatement(
                                    "return %1T($programId, keys, $data)",
                                    TransactionInstruction::class,
                                    PublicKey::class,
                                    Borsh::class,
                                    AnchorInstructionSerializer::class
                                )
                            }
                        }.build()
                    )
                }
            }.build()
        )
    }.build().writeTo(File("src/main/java"))

    FileSpec.builder(packageName, "Accounts").apply {

        addMyHeader()

        addAnnotation(
            AnnotationSpec.builder(UseSerializers::class)
                .addMember("%T::class", PublicKeyAs32ByteSerializer::class).build()
        )

        programIdl.accounts.forEach { account ->

            if (account.type.kind == "struct")
                addType(
                    TypeSpec.classBuilder(account.name)
                        .addAnnotation(AnnotationSpec.builder(Serializable::class).build())
                        .primaryConstructor(FunSpec.constructorBuilder().apply {
                            account.type.fields.forEach { field ->
                                field.type?.jenType?.let {
                                    addParameter(field.name, it)
                                }
                            }
                        }.build())
                        .addProperties(account.type.fields.map {
                            it.type?.jenType?.let { type ->
                                PropertySpec.builder(it.name, type)
                                    .initializer(it.name)
                                    .build()
                            }
                        }.filterNotNull())
                        .build()
                )
        }

    }.build().writeTo(File("src/main/java"))

    FileSpec.builder(packageName, "Types").apply {

        addMyHeader()

        addAnnotation(
            AnnotationSpec.builder(UseSerializers::class)
                .addMember("%T::class", PublicKeyAs32ByteSerializer::class).build()
        )

        programIdl.types.forEach { type ->
            when (type.type) {
                is StructTypeInfo -> {
                    addType(TypeSpec.classBuilder(type.name).apply {
                        addModifiers(KModifier.DATA)
                        addAnnotation(
                            AnnotationSpec
                                .builder(Serializable::class)
                                .build()
                        )
                        primaryConstructor(FunSpec.constructorBuilder().apply {
                            type.type.fields?.forEach { field ->
                                field.type?.jenType?.let {
                                    addParameter(field.name, it)
                                }
                            }
                        }.build())

                        type.type.fields?.forEach { field ->
                            field.type?.jenType?.let {
                                addProperty(
                                    PropertySpec.builder(field.name, it)
                                        .initializer(field.name)
                                        .build()
                                )
                            }
                        }
                    }.build())
                }
                is EnumTypeInfo -> {

                    if (type.type.variants.all { it.fields == null }) {
                        // Pure enums get converted to enums
                        addType(TypeSpec.enumBuilder(type.name).apply {
                            addAnnotation(
                                AnnotationSpec
                                    .builder(Serializable::class)
                                    .build()
                            )
                            type.type.variants.forEach {
                                addEnumConstant(it.name)
                            }
                        }.build())
                    } else {
                        // Enums with associated values that need to be converted into sealed classes.
                        // Even if only one variant has associated values it has to be sealed.
                        // If the variant doesn't have values it is converted into an object
                        // If the variant has values it will be a data class with properties. If the
                        // property is nameless the name will be the be the name of the type lowercase.
                        //
                        // Additional serializer is also generated.
                        addType(TypeSpec.classBuilder(type.name).apply {
                            addModifiers(KModifier.SEALED)
                            addAnnotation(
                                AnnotationSpec
                                .builder(Serializable::class)
                                .addMember("with = ${type.name}Serializer::class")
                                .build()
                            )

                            type.type.variants.forEach { variant ->
                                // If it has associated values
                                variant.fields?.let { fields ->
                                    addType(TypeSpec.classBuilder(variant.name)
                                        .superclass(ClassName(packageName, type.name))
                                        .addSuperclassConstructorParameter("")
                                        .apply {
                                            addModifiers(KModifier.DATA)
                                            primaryConstructor(
                                                FunSpec.constructorBuilder().apply {
                                                    fields.forEach { field ->
                                                        when (field) {
                                                            is VariantDefinedField -> addParameter(
                                                                field.name,
                                                                ClassName(
                                                                    packageName,
                                                                    field.defined
                                                                )
                                                            )
                                                            is VariantTypeField -> addParameter(
                                                                field.name,
                                                                field.type.jenType
                                                            )
                                                        }
                                                    }
                                                }.build()
                                            )

                                            fields.forEach { field ->
                                                when (field) {
                                                    is VariantDefinedField -> {
                                                        addProperty(
                                                            PropertySpec.builder(
                                                                field.name,
                                                                ClassName(
                                                                    packageName,
                                                                    field.defined
                                                                )
                                                            )
                                                                .initializer(field.name)
                                                                .build()
                                                        )
                                                    }
                                                    is VariantTypeField -> {
                                                        addProperty(
                                                            PropertySpec.builder(
                                                                field.name,
                                                                field.type.jenType
                                                            )
                                                                .initializer(field.name)
                                                                .build()
                                                        )
                                                    }
                                                }
                                            }

                                        }.build()
                                    )
                                    // If it doesn't has associated values
                                } ?: run {
                                    addType(
                                        TypeSpec.objectBuilder(variant.name)
                                            .superclass(ClassName(packageName, type.name))
                                            .addSuperclassConstructorParameter("")
                                            .build()
                                    )
                                }
                            }
                        }.build())

                        addType(TypeSpec.classBuilder("${type.name}Serializer")
                            .apply {
                                addSuperinterface(
                                    KSerializer::class.asClassName().parameterizedBy(
                                        ClassName(
                                            packageName,
                                            type.name
                                        )
                                    )
                                )
                                addProperty(
                                    PropertySpec.builder("descriptor", SerialDescriptor::class)
                                        .addModifiers(KModifier.OVERRIDE)
                                        .initializer("${JsonObject::class.asClassName().canonicalName}.serializer().descriptor", )
                                        .build()
                                )
                                addImport("kotlinx.serialization.builtins", "serializer")
                                addImport("kotlinx.serialization.builtins", "nullable")

                                addFunction(
                                    FunSpec.builder("serialize")
                                        .addModifiers(KModifier.OVERRIDE)
                                        .addParameter("encoder", Encoder::class)
                                        .addParameter(
                                            "value",
                                            ClassName(packageName, type.name)
                                        ).apply {
                                            addCode("when(value){ \n")
                                            type.type.variants.forEachIndexed { index, variant ->
                                                addCode("   is ${type.name}.${variant.name} -> { \n")
                                                addStatement("       encoder.encodeSerializableValue(Byte.serializer(), ${index}.toByte()) \n")
                                                variant.fields?.forEach { field ->
                                                    when (field) {
                                                        is VariantDefinedField -> {
                                                            addCode("       encoder.encodeSerializableValue(${field.defined}.serializer(), value.${field.name})\n")
                                                        } // (field.name, ClassName(packageName, field.defined))
                                                        is VariantTypeField -> {
                                                            addCode("       encoder.encodeSerializableValue(${field.type.serializer}, value.${field.name})\n")
                                                        } // (field.name, field.type.jenType)
                                                    }
                                                }
                                                addCode("   }\n")
                                            }
                                            addCode("   else -> { throw Throwable(\"Can not serialize\")}\n")
                                            addCode("}\n")
                                        }
                                        .build()
                                )

                                addFunction(
                                    FunSpec.builder("deserialize")
                                        .addModifiers(KModifier.OVERRIDE)
                                        .addParameter("decoder", Decoder::class)
                                        .returns(ClassName(packageName, type.name))
                                        .apply {
                                            addCode("return when(decoder.decodeByte().toInt()){\n")
                                            type.type.variants.forEachIndexed { index, variant ->
                                                addCode("   $index -> ${type.name}.${variant.name} ${ if(variant.fields != null){ "("} else { "" } }\n")
                                                variant.fields?.forEach { field ->
                                                    when (field) {
                                                        is VariantDefinedField -> {
                                                            addCode("       ${field.name} = decoder.decodeSerializableValue(${field.defined}.serializer()),\n")
                                                        } // (field.name, ClassName(packageName, field.defined))
                                                        is VariantTypeField -> {
                                                            addCode("       ${field.name} = decoder.decodeSerializableValue(${field.type.serializer}),\n")
                                                        } // (field.name, field.type.jenType)
                                                    }
                                                }
                                                variant.fields?.let { addCode(" )") }
                                            }
                                            addCode("   else -> { throw Throwable(\"Can not deserialize\")}\n")
                                            addCode("}\n")
                                        }
                                        .build()
                                )
                            }.build()
                        )
                    }
                }
            }
        }

    }.build().writeTo(File("src/main/java"))

    FileSpec.builder(packageName, "Errors").apply {

        addMyHeader()

        addType(
            TypeSpec.interfaceBuilder("${programName}Error")
                .addModifiers(KModifier.SEALED)
                .addProperty("code", Int::class)
                .addProperty("message", String::class)
                .build()
        )

        programIdl.errors.forEach { error ->

            addType(
                TypeSpec.classBuilder(error.name)
                    .addSuperinterface(
                        ClassName(
                            packageName,
                            "${programName}Error"
                        )
                    )
                    .addProperty(
                        PropertySpec.builder("code", Int::class, KModifier.OVERRIDE)
                            .initializer("%L", error.code).build()
                    )
                    .addProperty(
                        PropertySpec.builder("message", String::class, KModifier.OVERRIDE)
                            .initializer("%S", error.message).build()
                    )
                    .build()
            )
        }

    }.build().writeTo(File("src/main/java"))
}

private fun FileSpec.Builder.addMyHeader() {
    addComment(
        """
            
             ${name}
             Metaplex
            
             This code was generated locally by Funkatronics on ${LocalDate.now()}
             
        """.trimIndent()
    )
}

internal var packageName = ""

internal val FieldType.jenType: TypeName
    get() = when (this) {
        is ListField -> List::class.asClassName().parameterizedBy(vec.jenType)
        is NullableField -> option.jenType.copy(nullable = true)
        is PrimitiveField -> type?.asClassName() ?: ClassName(packageName, name)
        is HashmapField -> HashMap::class.asClassName().parameterizedBy(key.jenType, value.jenType)
    }

internal val FieldType.serializer: String
    get() = when (this) {
        is ListField -> "ListSerializer(${vec.jenType}.serializer())"
        is NullableField -> "${option.serializer}.nullable"
        is PrimitiveField -> {
            val typeString = type?.asClassName() ?: ClassName(packageName, name)
            if(typeString.toString() == "com.solana.core.PublicKey"){
                "PublicKeyAs32ByteSerializer"
            } else {
                "${ typeString }.serializer()"
            }
        }
        is HashmapField ->  "MapSerializer(${key.jenType}, ${value.jenType})"
    }

internal val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

val String.snakeCase get() = replace(camelRegex) { "_${it.value}" }.lowercase(Locale.US)