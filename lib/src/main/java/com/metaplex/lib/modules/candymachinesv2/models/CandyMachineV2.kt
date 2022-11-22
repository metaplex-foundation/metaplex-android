/*
 * CandyMachine
 * Metaplex
 *
 * Created by Funkatronics on 9/9/2022
 */

package com.metaplex.lib.modules.candymachinesv2.models

import com.metaplex.lib.experimental.jen.candymachinev2.EndSettings
import com.metaplex.lib.experimental.jen.candymachinev2.HiddenSettings
import com.metaplex.lib.experimental.jen.candymachinev2.WhitelistMintSettings
import com.metaplex.lib.modules.candymachinesv2.models.CandyMachineV2.Companion.PROGRAM_ADDRESS
import com.metaplex.lib.modules.candymachinesv2.models.CandyMachineV2.Companion.PROGRAM_NAME
import com.solana.core.PublicKey
import java.time.ZonedDateTime

data class CandyMachineV2(
    val address: PublicKey,
    val authority: PublicKey,
    val wallet: PublicKey,
    val price: Long,
    val sellerFeeBasisPoints: UShort,
    val itemsAvailable: Long,
    val symbol: String? = null,
    val goLiveDate: ZonedDateTime? = null,
    val tokenMintAddress: PublicKey? = null,
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
        return other is CandyMachineV2 && this.address == other.address
    }

    override fun hashCode() = address.hashCode()

    companion object {
        const val PROGRAM_NAME = "candy_machine"
        const val PROGRAM_ADDRESS = "cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"
    }
}

//region PDAs
val CandyMachineV2.creatorPda get() =
    PublicKey.findProgramAddress(listOf(
        PROGRAM_NAME.toByteArray(),
        address.toByteArray()
    ), PublicKey(PROGRAM_ADDRESS))

val CandyMachineV2.collectionPda get() =
    PublicKey.findProgramAddress(listOf(
        "collection".toByteArray(),
        address.toByteArray()
    ), PublicKey(PROGRAM_ADDRESS))
//endregion
