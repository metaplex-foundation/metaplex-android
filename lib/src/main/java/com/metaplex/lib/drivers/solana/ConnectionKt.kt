/*
 * SolanaConnection
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.experimental.serialization.format.BorshDecoder
import com.metaplex.lib.experimental.serialization.format.BorshEncoder
import com.metaplex.lib.programs.token_metadata.MasterEditionAccountJsonAdapterFactory
import com.metaplex.lib.programs.token_metadata.MasterEditionAccountRule
import com.metaplex.lib.programs.token_metadata.accounts.*
import com.metaplex.lib.shared.AccountPublicKeyJsonAdapterFactory
import com.metaplex.lib.shared.AccountPublicKeyRule
import com.metaplex.lib.solana.Connection
import com.solana.api.Api
import com.solana.api.getMultipleAccounts
import com.solana.api.getProgramAccounts
import com.solana.api.getSignatureStatuses
import com.solana.core.PublicKey
import com.solana.models.ProgramAccountConfig
import com.solana.models.SignatureStatusRequestConfiguration
import com.solana.models.buffer.Buffer
import com.solana.models.buffer.BufferInfo
import com.solana.networking.NetworkingRouterConfig
import com.solana.networking.OkHttpNetworkingRouter
import com.solana.networking.RPCEndpoint
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
abstract class ConnectionKt(endpoint: RPCEndpoint = RPCEndpoint.devnetSolana) : Connection {

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

    // Temporary, until we complete getProgramAccounts, getMultipleAccountsInfo and getSignatureStatuses
    val solanaRPC: Api = Api(
        OkHttpNetworkingRouter(endpoint,
            config = NetworkingRouterConfig(
                listOf(
                    MetadataAccountRule(),
                    MetaplexDataRule(),
                    MetaplexCollectionRule(),
                    AccountPublicKeyRule(),
                    MasterEditionAccountRule(),
                    MetaplexCreatorRule()
                ),
                listOf(
                    MetadataAccountJsonAdapterFactory(),
                    MetaplexDataAdapterJsonAdapterFactory(),
                    AccountPublicKeyJsonAdapterFactory(),
                    MasterEditionAccountJsonAdapterFactory()
                )
            )
        )
    )

    override fun <T : BorshCodable> getProgramAccounts(
        account: PublicKey,
        programAccountConfig: ProgramAccountConfig,
        decodeTo: Class<T>,
        onComplete: (Result<List<com.solana.models.ProgramAccount<T>>>) -> Unit) {
        solanaRPC.getProgramAccounts(account, programAccountConfig, decodeTo, onComplete)
    }

    override fun <T: BorshCodable> getMultipleAccountsInfo(
        accounts: List<PublicKey>,
        decodeTo: Class<T>,
        onComplete: ((Result<List<BufferInfo<T>?>>) -> Unit)
    ) {
        solanaRPC.getMultipleAccounts(accounts, decodeTo, onComplete)
    }

    override fun getSignatureStatuses(signatures: List<String>,
                                      configs: SignatureStatusRequestConfiguration?,
                                      onComplete: ((Result<com.solana.models.SignatureStatus>) -> Unit)) {
        solanaRPC.getSignatureStatuses(signatures, configs, onComplete)
    }
}

suspend inline fun <reified A> ConnectionKt.getAccountInfo(account: PublicKey)
: Result<AccountInfo<A>> = getAccountInfo(serializer(), account)

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