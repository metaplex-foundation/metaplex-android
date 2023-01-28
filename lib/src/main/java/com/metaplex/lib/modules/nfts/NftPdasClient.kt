package com.metaplex.lib.modules.nfts

import com.metaplex.lib.modules.nfts.models.MetaplexContstants
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey
import org.bitcoinj.core.Base58

class NftPdasClient {

    companion object {
        fun metadata(mint: PublicKey): Result<PublicKey> {
            val pdaSeeds = listOf(
                MetaplexContstants.METADATA_NAME.toByteArray(),
                Base58.decode(MetaplexContstants.METADATA_ACCOUNT_PUBKEY),
                mint.toByteArray()
            )

            val pdaAddres = PublicKey.findProgramAddress(
                pdaSeeds,
                PublicKey(MetaplexContstants.METADATA_ACCOUNT_PUBKEY)
            )
            return Result.success(pdaAddres.address)
        }

        fun masterEdition(mint: PublicKey): Result<PublicKey> {
            val pdaSeeds = listOf(
                MetaplexContstants.METADATA_NAME.toByteArray(),
                Base58.decode(MetaplexContstants.METADATA_ACCOUNT_PUBKEY),
                mint.toByteArray(),
                MetaplexContstants.METADATA_EDITION.toByteArray(),
            )

            val pdaAddres = PublicKey.findProgramAddress(
                pdaSeeds,
                PublicKey(MetaplexContstants.METADATA_ACCOUNT_PUBKEY)
            )
            return Result.success(pdaAddres.address)
        }

        /** Finds the record PDA for a given NFT and delegate authority. */
        fun tokenRecord(
            /** The address of the NFT's mint account. */
            mint: PublicKey,
            /** The address of the token account */
            token: PublicKey
        ): Result<PublicKey> {
            val pdaSeeds = listOf(
                MetaplexContstants.METADATA_NAME.toByteArray(),
                Base58.decode(MetaplexContstants.METADATA_ACCOUNT_PUBKEY),
                mint.toByteArray(),
                "token_record".toByteArray(),
                token.toByteArray(),
            )

            val pdaAddres = PublicKey.findProgramAddress(
                pdaSeeds,
                PublicKey(MetaplexContstants.METADATA_ACCOUNT_PUBKEY)
            )
            return Result.success(pdaAddres.address)
        }

        /** Finds the record PDA for a given NFT and delegate authority. */
        fun metadataDelegateRecord(
            /** The address of the NFT's mint account. */
            mint: PublicKey,
            /** The role of the delegate authority. */
            type: MetadataDelegateType,
            /** The address of the metadata's update authority. */
            updateAuthority: PublicKey,
            /** The address of delegate authority. */
            delegate: PublicKey
        ): Result<PublicKey> {
            val pdaSeeds = listOf(
                MetaplexContstants.METADATA_NAME.toByteArray(),
                Base58.decode(MetaplexContstants.METADATA_ACCOUNT_PUBKEY),
                mint.toByteArray(),
                getMetadataDelegateRoleSeed(type).toByteArray(),
                updateAuthority.toByteArray(),
                delegate.toByteArray(),
            )

            val pdaAddres = PublicKey.findProgramAddress(
                pdaSeeds,
                PublicKey(MetaplexContstants.METADATA_ACCOUNT_PUBKEY)
            )
            return Result.success(pdaAddres.address)
        }
    }
}