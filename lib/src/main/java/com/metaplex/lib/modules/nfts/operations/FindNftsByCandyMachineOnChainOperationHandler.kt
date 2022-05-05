package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.MetaplexContstants
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import org.bitcoinj.core.Base58

data class FindNftsByCandyMachineInput(
    val candyMachine : PublicKey,
    val version : UInt?,
)

val candyMachineId = PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ")
class FindNftsByCandyMachineOnChainOperationHandler(override var metaplex: Metaplex) :
    OperationHandler<FindNftsByCandyMachineInput, List<NFT?>> {
    override fun handle(operation: OperationResult<FindNftsByCandyMachineInput, OperationError>): OperationResult<List<NFT?>, OperationError> {
        val candyMachinePublicKeyAndVersion: OperationResult<PublicKey, OperationError> = operation.flatMap { input ->
            val candyMachine = input.candyMachine
            val version = input.version ?: 2
            if(version == 2){
                val pdaSeeds = listOf(
                    "candy_machine".toByteArray(),
                    candyMachineId.toByteArray(),
                )
                val pdaAddres = PublicKey.findProgramAddress(
                    pdaSeeds,
                    candyMachineId
                )
                return@flatMap OperationResult.success(pdaAddres.address)
            }
            return@flatMap OperationResult.success(candyMachine)
        }

        return candyMachinePublicKeyAndVersion.flatMap {
            val operation = FindNftsByCreatorOnChainOperationHandler(this.metaplex)
            operation.handle(FindNftsByCreatorOperation.pure(ResultWithCustomError.success(
                FindNftsByCreatorInput(it, 1)
            )))
        }
    }
}