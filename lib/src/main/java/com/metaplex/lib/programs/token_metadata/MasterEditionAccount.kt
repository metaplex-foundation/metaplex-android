package com.metaplex.lib.programs.token_metadata

import com.google.protobuf.UInt64Value
import com.metaplex.lib.modules.nfts.models.MetaplexContstants
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey
import com.solana.vendor.borshj.BorshCodable
import com.solana.vendor.borshj.FieldOrder
import org.bitcoinj.core.Base58

data class MasterEditionAccount(@FieldOrder(0) val type: Byte): BorshCodable {
    companion object {
        fun pda(publicKey: PublicKey): ResultWithCustomError<PublicKey, OperationError> {
            val pdaSeeds = listOf(
                MetaplexContstants.METADATA_NAME.toByteArray(),
                Base58.decode(MetaplexContstants.METADATA_ACCOUNT_PUBKEY),
                publicKey.toByteArray(),
                MetaplexContstants.METADATA_EDITION.toByteArray(),
            )

            val pdaAddres = PublicKey.findProgramAddress(
                pdaSeeds,
                PublicKey(MetaplexContstants.METADATA_ACCOUNT_PUBKEY)
            )
            return ResultWithCustomError.success(pdaAddres.address)
        }
    }
}

data class MasterEditionV1 (
    @FieldOrder(0) val supply: UInt64Value?,
    @FieldOrder(1) val max_supply: UInt64Value?,
    @FieldOrder(2) val printing_mint: PublicKey,
    @FieldOrder(3) val one_time_printing_authorization_mint: PublicKey
): BorshCodable

data class MasterEditionV2 (
    @FieldOrder(0) val supply: UInt64Value?,
    @FieldOrder(1) val max_supply: UInt64Value?
): BorshCodable