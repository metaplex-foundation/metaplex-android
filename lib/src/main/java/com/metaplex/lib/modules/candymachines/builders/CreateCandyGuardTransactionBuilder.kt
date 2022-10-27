/*
 * CreateCandyGuardTransactionBuilder
 * metaplex-android
 * 
 * Created by Funkatronics on 10/13/2022
 */

package com.metaplex.lib.modules.candymachines.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candyguard.*
import com.metaplex.lib.modules.candymachines.models.*
import com.metaplex.lib.modules.candymachines.models.CandyGuard
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CreateCandyGuardTransactionBuilder(
    val base: PublicKey, payer: PublicKey, val authority: PublicKey = payer,
    connection: Connection, dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionBuilder(payer, connection, dispatcher) {

    val emptyGuardSet = GuardSet(
        botTax = null,
        solPayment = null,
        tokenPayment = null,
        startDate = null,
        thirdPartySigner = null,
        tokenGate = null,
        gatekeeper = null,
        endDate = null,
        allowList = null,
        mintLimit = null,
        nftPayment = null,
        redeemedAmount = null,
        addressGate = null,
        nftGate = null,
        nftBurn = null,
        tokenBurn = null
    )

    val defaultGuards = mutableSetOf<Guard>()
    val groups = mutableMapOf<String, Set<Guard>>()

    fun addGuards(guards: List<Guard>) = defaultGuards.addAll(guards).let { this }

    fun addGuardGroup(label: String, guards: List<Guard>) =
        groups.set(label, guards.toSet()).let { this }

    fun addGuardGroups(groups: Map<String, List<Guard>>) =
        groups.forEach { l, g -> addGuardGroup(l, g) }.let { this }

    override suspend fun build(): Result<Transaction> = Result.success(Transaction().apply {
        addInstruction(CandyGuardInstructions.initialize(
            candyGuard = CandyGuard.pda(base).address,
            base = base,
            authority = authority,
            payer = payer,
            systemProgram = SystemProgram.PROGRAM_ID,
            data = CandyGuardData(
                default = defaultGuards.toGuardSet(),
                groups = if (groups.isNotEmpty()) groups.map {
                    Group(it.key, it.value.toGuardSet())
                } else null
            )
        ))
    })

    // there has got to be a better way to do this
    private fun Set<Guard>.toGuardSet() = map { it.idlObj }.run {
        GuardSet(
            botTax = find { it is BotTax } as? BotTax,
            solPayment = find { it is SolPayment }  as? SolPayment,
            tokenPayment = find { it is TokenPayment } as? TokenPayment,
            startDate = find { it is StartDate } as? StartDate,
            thirdPartySigner = find { it is ThirdPartySigner } as? ThirdPartySigner,
            tokenGate = find { it is TokenGate } as? TokenGate,
            gatekeeper = find { it is Gatekeeper } as? Gatekeeper,
            endDate = find { it is EndDate } as? EndDate,
            allowList = find { it is AllowList } as? AllowList,
            mintLimit = find { it is MintLimit } as? MintLimit,
            nftPayment = find { it is NftPayment } as? NftPayment,
            redeemedAmount = find { it is RedeemedAmount } as? RedeemedAmount,
            addressGate = find { it is AddressGate } as? AddressGate,
            nftGate = find { it is NftGate } as? NftGate,
            nftBurn = find { it is NftBurn } as? NftBurn,
            tokenBurn = find { it is TokenBurn }  as? TokenBurn
        )
    }
}