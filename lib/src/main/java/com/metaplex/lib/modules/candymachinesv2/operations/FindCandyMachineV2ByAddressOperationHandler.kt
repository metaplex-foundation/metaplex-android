/*
 * FindCandyMachineV2ByAddressOperationHandler
 * Metaplex
 * 
 * Created by Funkatronics on 9/21/2022
 */

package com.metaplex.lib.modules.candymachinesv2.operations

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.getAccountInfo
import com.metaplex.lib.experimental.jen.candymachinev2.CandyMachine
import com.metaplex.lib.modules.candymachinesv2.models.CandyMachineV2
import com.metaplex.lib.shared.OperationError
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class FindCandyMachineV2ByAddressOperationHandler(val connection: Connection,
                                                  private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun handle(input: PublicKey): Result<CandyMachineV2> = withContext(dispatcher) {
        connection.getAccountInfo<CandyMachine>(input).map {
            it.data?.let { cmAccount ->
                CandyMachineV2(
                    address = input,
                    authority = cmAccount.authority,
                    wallet = cmAccount.wallet,
                    price = cmAccount.data.price.toLong(),
                    sellerFeeBasisPoints = cmAccount.data.sellerFeeBasisPoints,
                    itemsAvailable = cmAccount.data.itemsAvailable.toLong(),
                    symbol = cmAccount.data.symbol,
                    goLiveDate = cmAccount.data.goLiveDate?.let { ms ->
                        ZonedDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault())
                    },
                    tokenMintAddress = cmAccount.tokenMint,
                    isMutable = cmAccount.data.isMutable,
                    retainAuthority = cmAccount.data.retainAuthority,
                    maxEditionSupply = cmAccount.data.maxSupply.toLong(),
                    endSettings = cmAccount.data.endSettings,
                    hiddenSettings = cmAccount.data.hiddenSettings,
                    whitelistMintSettings = cmAccount.data.whitelistMintSettings
                )
            } ?: throw OperationError.NilDataOnAccount
        }
    }
}