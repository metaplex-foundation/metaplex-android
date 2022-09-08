/*
 * IDL Models (for serialization)
 * metaplex-android
 *
 * Created by Funkatronics on 8/9/2022
 */
package com.metaplex.lib.experimental.jen

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Idl(val name: String, val version: String, val instructions: List<Instruction>,
               val accounts: List<Account>, val types: List<Type>, val errors: List<Error>,
               val metadata: IdlMetadata? = null)

//region Instructions
@Serializable
data class Instruction(val name: String, val accounts: List<AccountInput>, val args: List<Argument>)

@Serializable
data class AccountInput(val name: String, val isMut: Boolean, val isSigner: Boolean)

@Serializable
data class Argument(val name: String, val type: JsonElement)
//endregion

//region Accounts
@Serializable
data class Account(val name: String, val type: AccountType)

@Serializable
data class AccountType(val kind: String, val fields: List<Field>)

// TODO: type could be string or json object
@Serializable
data class Field(val name: String, val type: JsonElement? = null)
//endregion

//region Types
@Serializable
data class Type(val name: String, val type: TypeInfo)

// TODO: deal with nested object in variants
@Serializable
data class TypeInfo(val kind: String, val variants: List<Field>? = null, val fields: List<Field>? = null)
//endregion

@Serializable
data class Error(val code: Int, val name: String, @SerialName("msg") val message: String)

@Serializable
data class IdlMetadata(
    val address: String,
    val origin: String,
    val binaryVersion: String,
    val libVersion:	String
)