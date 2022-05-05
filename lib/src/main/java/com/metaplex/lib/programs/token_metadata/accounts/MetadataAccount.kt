package com.metaplex.lib.programs.token_metadata

import com.metaplex.lib.modules.nfts.models.MetaplexContstants
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey
import com.solana.vendor.borshj.BorshCodable
import com.solana.vendor.borshj.FieldOrder
import org.bitcoinj.core.Base58

data class MetadataAccount(
    @FieldOrder(0) val key: Byte,
    @FieldOrder(1) val update_authority: PublicKey,
    @FieldOrder(2) val mint: PublicKey,
    @FieldOrder(3) val data: MetaplexData
) : BorshCodable {
    companion object {
        fun pda(publicKey: PublicKey): ResultWithCustomError<PublicKey, OperationError> {
            val pdaSeeds = listOf(
                MetaplexContstants.METADATA_NAME.toByteArray(),
                Base58.decode(MetaplexContstants.METADATA_ACCOUNT_PUBKEY),
                publicKey.toByteArray()
            )

            val pdaAddres = PublicKey.findProgramAddress(
                pdaSeeds,
                PublicKey(MetaplexContstants.METADATA_ACCOUNT_PUBKEY)
            )
            return ResultWithCustomError.success(pdaAddres.address)
        }
    }
}

data class MetaplexData(
    @FieldOrder(0) val name: String,
    @FieldOrder(1) val symbol: String,
    @FieldOrder(2) val uri: String,
) : BorshCodable