package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class FindNftsByCandyMachineInput(
    val candyMachine : PublicKey,
    val version : UInt?,
)

val candyMachineId = PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ")
class FindNftsByCandyMachineOnChainOperationHandler(override val connection: Connection,
                                                    override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<FindNftsByCandyMachineInput, List<NFT?>> {

    constructor(metaplex: Metaplex) : this(metaplex.connection)

    override suspend fun handle(input: FindNftsByCandyMachineInput): Result<List<NFT?>> {
        val cmAddress = if((input.version ?: 2) == 2){
            val pdaSeeds = listOf(
                "candy_machine".toByteArray(),
                candyMachineId.toByteArray(),
            )
            val pdaAddres = PublicKey.findProgramAddress(
                pdaSeeds,
                candyMachineId
            )
            pdaAddres.address
        } else input.candyMachine

        return FindNftsByCreatorOnChainOperationHandler(connection, dispatcher)
            .handle(FindNftsByCreatorInput(cmAddress, 1))
    }
}