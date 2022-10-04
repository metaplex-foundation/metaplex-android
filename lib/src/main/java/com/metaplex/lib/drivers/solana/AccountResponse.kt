/*
 * SolanaRpcSerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.metaplex.lib.serialization.serializers.solana.AnchorAccountSerializer
import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.metaplex.lib.serialization.serializers.solana.SolanaResponseSerializer
import com.solana.core.PublicKey
import com.solana.models.buffer.Buffer
import com.solana.models.buffer.BufferInfo
import com.solana.vendor.borshj.BorshCodable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable

@Serializable
data class AccountInfo<D>(val data: D?, val executable: Boolean,
                          val lamports: Long, val owner: String?, val rentEpoch: Long)

internal fun <D, T: BorshCodable> AccountInfo<D>.toBufferInfo() =
    BufferInfo(data?.let { Buffer(data as T) }, executable, lamports, owner, rentEpoch)

@Serializable
data class AccountInfoWithPublicKey<P>(val account: AccountInfo<P>, @SerialName("pubkey") val publicKey: String)

@Serializable
data class AccountPublicKey(@Serializable(with = PublicKeyAs32ByteSerializer::class) val publicKey: PublicKey)

internal fun <A> SolanaAccountSerializer(serializer: KSerializer<A>) =
    AccountInfoSerializer(
        BorshAsBase64JsonArraySerializer(
            AnchorAccountSerializer(serializer.descriptor.serialName, serializer)
        )
    )

internal fun <A> MultipleAccountsSerializer(serializer: KSerializer<A>) =
    MultipleAccountsInfoSerializer(
        BorshAsBase64JsonArraySerializer(
            AnchorAccountSerializer(serializer.descriptor.serialName, serializer)
        )
    )

internal fun <A> ProgramAccountsSerializer(serializer: KSerializer<A>) =
    ListSerializer(
        AccountInfoWithPublicKey.serializer(
            BorshAsBase64JsonArraySerializer(serializer)
        ).nullable
    )

internal inline fun <reified A> SolanaAccountSerializer() =
    AccountInfoSerializer<A?>(BorshAsBase64JsonArraySerializer(AnchorAccountSerializer()))

internal inline fun <reified A> MultipleAccountsSerializer() =
    MultipleAccountsInfoSerializer<A?>(BorshAsBase64JsonArraySerializer(AnchorAccountSerializer()))

private fun <D> AccountInfoSerializer(serializer: KSerializer<D>) =
    SolanaResponseSerializer(AccountInfo.serializer(serializer))

private fun <D> MultipleAccountsInfoSerializer(serializer: KSerializer<D>) =
    SolanaResponseSerializer(ListSerializer(AccountInfo.serializer(serializer).nullable))