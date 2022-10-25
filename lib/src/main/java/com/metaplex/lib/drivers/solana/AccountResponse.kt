/*
 * SolanaRpcSerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.metaplex.lib.serialization.serializers.solana.SolanaResponseSerializer
import com.solana.core.PublicKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable

@Serializable
data class AccountInfo<D>(val data: D?, val executable: Boolean,
                          val lamports: Long, val owner: String?, val rentEpoch: Long)

@Serializable
data class AccountInfoWithPublicKey<P>(val account: AccountInfo<P>, @SerialName("pubkey") val publicKey: String)

@Serializable
data class AccountPublicKey(@Serializable(with = PublicKeyAs32ByteSerializer::class) val publicKey: PublicKey)

internal fun <A> SolanaAccountSerializer(serializer: KSerializer<A>) =
    AccountInfoSerializer(BorshAsBase64JsonArraySerializer(serializer))

internal fun <A> MultipleAccountsSerializer(serializer: KSerializer<A>) =
    MultipleAccountsInfoSerializer(BorshAsBase64JsonArraySerializer(serializer))

internal fun <A> ProgramAccountsSerializer(serializer: KSerializer<A>) =
    ListSerializer(
        AccountInfoWithPublicKey.serializer(
            BorshAsBase64JsonArraySerializer(serializer)
        ).nullable
    )

private fun <D> AccountInfoSerializer(serializer: KSerializer<D>) =
    SolanaResponseSerializer(AccountInfo.serializer(serializer))

private fun <D> MultipleAccountsInfoSerializer(serializer: KSerializer<D>) =
    SolanaResponseSerializer(ListSerializer(AccountInfo.serializer(serializer).nullable))