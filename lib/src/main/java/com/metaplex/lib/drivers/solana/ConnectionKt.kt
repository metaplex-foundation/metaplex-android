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

// Inlines let us hide the serialization complexity while still providing full control
// For example, to find an AuctionHouse by address:
//      val auctionHouse = connectionKt.getAccountInfo<AuctionHouse>(address)
// And optionally, to find an account and supply your own serializer :
//      val customAccount = connectionKt.getAccountInfo(customSerializer, address)
abstract class ConnectionKt : Connection {

    abstract suspend fun <A> getAccountInfo(serializer: KSerializer<A>, account: PublicKey): Result<AccountInfo<A>>

    override fun <T: BorshCodable> getAccountInfo(
        account: PublicKey,
        decodeTo: Class<T>,
        onComplete: (Result<BufferInfo<T>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(getAccountInfo(BorshCodeableSerializer(decodeTo), account).map {
                val buffer = it.data?.let { Buffer(it as T) }
                BufferInfo(buffer, it.executable, it.lamports, it.owner, it.rentEpoch)
            })
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

suspend inline fun <reified A> ConnectionKt.getAccountInfo(account: PublicKey)
: Result<AccountInfo<A>> = getAccountInfo(kotlinx.serialization.serializer(), account)

@Serializer(forClass = BorshCodable::class)
class BorshCodeableSerializer<T>(val clazz: Class<T>) : KSerializer<BorshCodable?> {
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