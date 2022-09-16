/*
 * CandyMachine
 * Metaplex
 * 
 * Created by Funkatronics on 9/9/2022
 */

package com.metaplex.lib.modules.candymachines.models

import com.metaplex.lib.experimental.jen.candymachine.*
import com.metaplex.lib.extensions.epochMillis
import com.metaplex.lib.modules.candymachines.models.CandyMachine.Companion.PROGRAM_ADDRESS
import com.metaplex.lib.modules.candymachines.models.CandyMachine.Companion.PROGRAM_NAME
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import java.time.ZonedDateTime

data class CandyMachine(
    val address: PublicKey,
    val authority: PublicKey,
    val wallet: PublicKey,
    val price: Long,
    val sellerFeeBasisPoints: UShort,
    val itemsAvailable: Long,
    val symbol: String? = null,
    val goLiveDate: ZonedDateTime? = null,
    val tokenMintAddress: PublicKey? = null,
    val collectionMintAddress: PublicKey? = null,
    val isMutable: Boolean = true,
    val retainAuthority: Boolean = true,
    val maxEditionSupply: Long = 0,
    val endSettings: EndSettings? = null,
    val hiddenSettings: HiddenSettings? = null,
    val whitelistMintSettings: WhitelistMintSettings? = null
) {

    val uuid = address.toBase58().slice(0 until 6)
    val accountSize: Long = hiddenSettings?.let { CONFIG_ARRAY_START.toLong() } ?:
        (CONFIG_ARRAY_START + 4 + itemsAvailable * CONFIG_LINE_SIZE + 8 + 2 * (itemsAvailable / 8 + 1))

    override fun equals(other: Any?): Boolean {
        return other is CandyMachine && this.address == other.address
    }

    companion object {
        const val PROGRAM_NAME = "candy_machine"
        const val PROGRAM_ADDRESS = "cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"
    }
}

//region PDAs
val CandyMachine.creatorPda get() =
    PublicKey.findProgramAddress(listOf(
        PROGRAM_NAME.toByteArray(),
        address.toByteArray()
    ), PublicKey(PROGRAM_ADDRESS))

val CandyMachine.collectionPda get() =
    PublicKey.findProgramAddress(listOf(
        "collection".toByteArray(),
        address.toByteArray()
    ), PublicKey(PROGRAM_ADDRESS))
//endregion

//region TRANSACTION BUILDERS
fun CandyMachine.buildInitializeCandyMachineTransaction(payer: PublicKey) = Transaction().apply {

    // Create an empty account for the candy machine.
    addInstruction(SystemProgram.createAccount(
        fromPublicKey = payer,
        newAccountPublickey = address,
        lamports = 0,
        space = accountSize,
        PublicKey(PROGRAM_ADDRESS)
    ))

    // Initialize the candy machine account.
    addInstruction(CandyMachineInstructions.initializeCandyMachine(
        candyMachine = address,
        wallet = wallet,
        authority = authority,
        payer = payer,
        systemProgram = SystemProgram.PROGRAM_ID,
        rent = Sysvar.SYSVAR_RENT_ADDRESS,
        data = CandyMachineData(
            uuid = uuid,
            price = price.toULong(),
            symbol = symbol ?: String(),
            sellerFeeBasisPoints = sellerFeeBasisPoints,
            maxSupply = maxEditionSupply.toULong(),
            isMutable = isMutable,
            retainAuthority = retainAuthority,
            goLiveDate = goLiveDate?.epochMillis(),
            endSettings = endSettings,
            creators = listOf(Creator(payer, false, 100.toUByte())),
            hiddenSettings = hiddenSettings, // not supported in v0.1
            whitelistMintSettings = whitelistMintSettings, // not supported in v0.1
            itemsAvailable = itemsAvailable.toULong(),
            gatekeeper = null // not supported in v0.1
        )
    ))
}
//endregion
