/*
 * SolanaConnection
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.experimental.serialization.format.BorshDecoder
import com.metaplex.lib.experimental.serialization.format.BorshEncoder
import com.metaplex.lib.programs.token_metadata.MasterEditionAccountRule
import com.metaplex.lib.programs.token_metadata.accounts.*
import com.metaplex.lib.shared.AccountPublicKeyRule
import com.metaplex.lib.solana.Connection
import com.solana.core.PublicKey
import com.solana.models.buffer.Buffer
import com.solana.models.buffer.BufferInfo
import com.solana.vendor.borshj.BorshBuffer
import com.solana.vendor.borshj.BorshCodable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Abstract [Connection] implementation that wraps the legacy async-callback API into the
 * suspendable API implementation where possible. Methods without a suspendable equivalent are
 * left unimplemented. The hope is to eventually deprecate the legacy callback API entirely and
 * move to a new combined API that offers both suspendable and callback interfaces and a better
 * serialization experience (no more decodeTo parameters and moshi/borsh rules)
 *
 * @author Funkatronics
 */
abstract class ConnectionKt : Connection {

    abstract suspend fun <A> getAccountInfo(serializer: KSerializer<A>, account: PublicKey): Result<AccountInfo<A>>

    override fun <T: BorshCodable> getAccountInfo(
        account: PublicKey,
        decodeTo: Class<T>,
        onComplete: (Result<BufferInfo<T>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(getAccountInfo(BorshCodeableSerializer(decodeTo), account)
                .map { it.toBufferInfo() })
//                .map { it as BufferInfo<T> })
        }
    }

//    abstract suspend fun <A> getProgramAccounts(serializer: KSerializer<A>, account: PublicKey,
//                                                programAccountConfig: ProgramAccountConfig): Result<List<ProgramAccount<A>>>
//
//    abstract suspend fun <A> getMultipleAccountsInfo(serializer: KSerializer<A>,
//                                                     accounts: List<PublicKey>): Result<List<AccountInfo<A>?>>
//
//    abstract suspend fun getSignatureStatuses(signatures: List<String>,
//                                              configs: SignatureStatusRequestConfiguration?): Result<List<SignatureStatus>>
}

// Inlines let us hide the serialization complexity while still providing full control
// For example, to find an AuctionHouse by address:
//      val auctionHouse = connectionKt.getAccountInfo<AuctionHouse>(address)
// And optionally, to find an account and supply your own serializer :
//      val customAccount = connectionKt.getAccountInfo(customSerializer, address)
suspend inline fun <reified A> ConnectionKt.getAccountInfo(account: PublicKey)
: Result<AccountInfo<A>> = getAccountInfo(serializer(), account)

@Serializer(forClass = BorshCodable::class)
internal class BorshCodeableSerializer<T>(val clazz: Class<T>) : KSerializer<BorshCodable?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(clazz.simpleName) {}

    val rule = listOf(
        MetadataAccountRule(),
        MetaplexDataRule(),
        MetaplexCollectionRule(),
        AccountPublicKeyRule(),
        MasterEditionAccountRule(),
        MetaplexCreatorRule()
    ).find { it.clazz == clazz }

    override fun deserialize(decoder: Decoder): BorshCodable? =
        rule?.read(BorshBuffer.wrap((decoder as? BorshDecoder)?.bytes))

    override fun serialize(encoder: Encoder, value: BorshCodable?) {
        value?.let {
            rule?.write(value, BorshBuffer.wrap((encoder as? BorshEncoder)?.borshEncodedBytes))
        }
    }
}