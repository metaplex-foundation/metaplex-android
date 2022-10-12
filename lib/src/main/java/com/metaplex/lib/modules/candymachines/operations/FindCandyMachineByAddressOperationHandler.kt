/*
 * FindCandyMachineByAddressOperationHandler
 * Metaplex
 * 
 * Created by Funkatronics on 9/21/2022
 */

package com.metaplex.lib.modules.candymachines.operations

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.getAccountInfo
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.shared.OperationError
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.metaplex.lib.experimental.jen.candymachine.CandyMachine as CandyMachineAccount

class FindCandyMachineByAddressOperationHandler(val connection: Connection,
                                                private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun handle(input: PublicKey): Result<CandyMachine> = withContext(dispatcher) {
        connection.getAccountInfo<CandyMachineAccount>(input).map {
            it.data?.let { cmAccount ->
                CandyMachine(
                    address = input,
                    authority = cmAccount.authority,
                    sellerFeeBasisPoints = cmAccount.data.sellerFeeBasisPoints,
                    itemsAvailable = cmAccount.data.itemsAvailable.toLong(),
                    symbol = cmAccount.data.symbol,
                    collectionMintAddress = cmAccount.collectionMint,
                    collectionUpdateAuthority = cmAccount.authority,
                    creators = cmAccount.data.creators,
                    isMutable = cmAccount.data.isMutable,
                    maxEditionSupply = cmAccount.data.maxSupply.toLong(),
                    configLineSettings = cmAccount.data.configLineSettings,
                    hiddenSettings = cmAccount.data.hiddenSettings,
                )
            } ?: throw OperationError.NilDataOnAccount
        }
    }
}