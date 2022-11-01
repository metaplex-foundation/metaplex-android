/*
 * CandyMachine
 * Metaplex
 * 
 * Created by Funkatronics on 9/19/2022
 */

package com.metaplex.lib.modules.candymachines.models

import com.metaplex.lib.experimental.jen.candymachine.ConfigLineSettings
import com.metaplex.lib.experimental.jen.candymachine.Creator
import com.metaplex.lib.experimental.jen.candymachine.HiddenSettings
import com.metaplex.lib.modules.candymachines.CANDY_MACHINE_HIDDEN_SECTION
import com.metaplex.lib.modules.candymachines.models.CandyMachine.Companion.PROGRAM_ADDRESS
import com.metaplex.lib.modules.candymachinesv2.models.CandyMachineV2
import com.solana.core.PublicKey

class CandyMachine(
    val address: PublicKey,
    val authority: PublicKey,
    val mintAuthority: PublicKey,
    val sellerFeeBasisPoints: UShort,
    val itemsAvailable: Long,
    val itemsMinted: Long = 0,
    val itemsLoaded: Int = 0,
    val symbol: String? = null,
    val collectionMintAddress: PublicKey,
    val collectionUpdateAuthority: PublicKey,
    val creators: List<Creator>? = null,
    val isMutable: Boolean = true,
    val maxEditionSupply: Long = 0,
    val configLineSettings: ConfigLineSettings? = null,
    val hiddenSettings: HiddenSettings? = null,
    val items: List<CandyMachineItem> = listOf()
) {

    val itemsRemaining: Long = itemsAvailable - itemsMinted

    val accountSize: Long = CANDY_MACHINE_HIDDEN_SECTION + if(hiddenSettings != null) 0L else {
        4 + itemsAvailable * configLineSize + // config line data
        (4 + itemsAvailable/8 + 1) + // Bit mask to keep track of ConfigLines
        (4 + itemsAvailable*4) // Mint indices.
    }

    override fun equals(other: Any?): Boolean {
        return other is CandyMachineV2 && this.address == other.address
    }

    override fun hashCode() = address.hashCode()

    private val configLineSize: Int get() = configLineSettings?.let {
        it.nameLength.toInt() + it.uriLength.toInt()
    } ?: 0

    companion object {
        const val PROGRAM_NAME = "candy_machine_core"
        const val PROGRAM_ADDRESS = "CndyV3LdqHUfDLmE5naZjVN8rBZz4tqhdefbAnjHG3JR"
    }
}

//region PDAs
val CandyMachine.authorityPda get() =
    PublicKey.findProgramAddress(listOf(
        "candy_machine".toByteArray(Charsets.UTF_8),
        address.toByteArray()
    ), PublicKey(PROGRAM_ADDRESS))
//endregion