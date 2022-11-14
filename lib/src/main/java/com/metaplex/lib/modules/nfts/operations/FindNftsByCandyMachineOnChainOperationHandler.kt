package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachinesv2.models.CandyMachineV2
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class FindNftsByCandyMachineInput(
    val candyMachine : PublicKey,
    val version : Int?,
)

class FindNftsByCandyMachineOnChainOperationHandler(override val connection: Connection,
                                                    override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<FindNftsByCandyMachineInput, List<NFT?>> {

    constructor(metaplex: Metaplex) : this(metaplex.connection)

    override suspend fun handle(input: FindNftsByCandyMachineInput): Result<List<NFT?>> {
        val cmAddress = if(input.version == 3){
            PublicKey.findProgramAddress(listOf(
                "candy_machine".toByteArray(),
                input.candyMachine.toByteArray(),
            ), PublicKey(CandyMachine.PROGRAM_ADDRESS)).address
        } else if(input.version == 2){
            PublicKey.findProgramAddress(listOf(
                CandyMachineV2.PROGRAM_NAME.toByteArray(),
                input.candyMachine.toByteArray()
            ), PublicKey(CandyMachineV2.PROGRAM_ADDRESS)).address
        } else input.candyMachine

        return FindNftsByCreatorOnChainOperationHandler(connection, dispatcher)
            .handle(FindNftsByCreatorInput(cmAddress, 1))
    }
}