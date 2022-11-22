/*
 * FindCandyMachineByAddressOperationHandler
 * Metaplex
 * 
 * Created by Funkatronics on 9/21/2022
 */

package com.metaplex.lib.modules.candymachines.operations

import com.metaplex.kborsh.Borsh
import com.metaplex.lib.drivers.solana.*
import com.metaplex.lib.modules.candymachines.CANDY_MACHINE_HIDDEN_SECTION
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachines.models.CandyMachineHiddenSectionDeserializer
import com.metaplex.lib.serialization.serializers.base64.ByteArrayAsBase64JsonArraySerializer
import com.metaplex.lib.serialization.serializers.solana.AnchorAccountSerializer
import com.metaplex.lib.serialization.serializers.solana.SolanaResponseSerializer
import com.metaplex.lib.shared.OperationError
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.metaplex.lib.experimental.jen.candymachine.CandyMachine as CandyMachineAccount

class FindCandyMachineByAddressOperationHandler(val connection: Connection,
                                                private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun handle(input: PublicKey): Result<CandyMachine> = withContext(dispatcher) {
        connection.get(
            AccountRequest(input.toString(), connection.transactionOptions),
            SolanaResponseSerializer(AccountInfo.serializer(ByteArrayAsBase64JsonArraySerializer))
        ).map {
            it?.data?.let { accountData ->
                val cmAccount = Borsh.decodeFromByteArray(AnchorAccountSerializer<CandyMachineAccount>(), accountData)
                val hiddenSection = Borsh.decodeFromByteArray(
                    CandyMachineHiddenSectionDeserializer(cmAccount.data.itemsAvailable.toInt(),
                        cmAccount.data.configLineSettings!!),
                    accountData.sliceArray(CANDY_MACHINE_HIDDEN_SECTION until accountData.size)
                )

                CandyMachine(
                    address = input,
                    authority = cmAccount.authority,
                    mintAuthority = cmAccount.mintAuthority,
                    sellerFeeBasisPoints = cmAccount.data.sellerFeeBasisPoints,
                    itemsAvailable = cmAccount.data.itemsAvailable.toLong(),
                    itemsMinted = cmAccount.itemsRedeemed.toLong(),
                    itemsLoaded = hiddenSection.itemsLoaded,
                    symbol = cmAccount.data.symbol,
                    collectionMintAddress = cmAccount.collectionMint,
                    collectionUpdateAuthority = cmAccount.authority,
                    creators = cmAccount.data.creators,
                    isMutable = cmAccount.data.isMutable,
                    maxEditionSupply = cmAccount.data.maxSupply.toLong(),
                    configLineSettings = cmAccount.data.configLineSettings,
                    hiddenSettings = cmAccount.data.hiddenSettings,
                    items = hiddenSection.items
                )
            } ?: throw OperationError.NilDataOnAccount
        }
    }
}