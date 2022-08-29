/*
 * SolanaConnection
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.experimental.serialization.serializers.legacy.BorshCodeableSerializer
import com.metaplex.lib.solana.Connection
import com.solana.core.PublicKey
import com.solana.models.buffer.BufferInfo
import com.solana.vendor.borshj.BorshCodable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.*

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

    abstract suspend fun getRecentBlockhash(): Result<String>

    override fun <T: BorshCodable> getAccountInfo(
        account: PublicKey,
        decodeTo: Class<T>,
        onComplete: (Result<BufferInfo<T>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(getAccountInfo(BorshCodeableSerializer(decodeTo), account)
                .map { it.toBufferInfo() })
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