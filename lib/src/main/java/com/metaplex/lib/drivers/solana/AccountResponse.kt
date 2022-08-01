/*
 * SolanaRpcSerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.experimental.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.metaplex.lib.experimental.serialization.serializers.solana.AnchorAccountSerializer
import com.metaplex.lib.experimental.serialization.serializers.solana.SolanaResponseSerializer
import com.solana.models.buffer.Buffer
import com.solana.models.buffer.BufferInfo
import com.solana.vendor.borshj.BorshCodable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
data class AccountInfo<D>(val data: D?, val executable: Boolean,
                          val lamports: Long, val owner: String?, val rentEpoch: Long)

internal fun <D, T: BorshCodable> AccountInfo<D>.toBufferInfo() =
    BufferInfo(data?.let { Buffer(data as T) }, executable, lamports, owner, rentEpoch)

internal fun <A> SolanaAccountSerializer(serializer: KSerializer<A>) =
    AccountInfoSerializer(
        BorshAsBase64JsonArraySerializer(
            AnchorAccountSerializer(serializer.descriptor.serialName, serializer)
        )
    )

internal inline fun <reified A> SolanaAccountSerializer() =
    AccountInfoSerializer<A?>(BorshAsBase64JsonArraySerializer(AnchorAccountSerializer()))

private fun <D> AccountInfoSerializer(serializer: KSerializer<D>) =
    SolanaResponseSerializer(AccountInfo.serializer(serializer))