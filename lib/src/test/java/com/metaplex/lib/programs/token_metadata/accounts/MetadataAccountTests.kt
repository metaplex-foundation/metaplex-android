package com.metaplex.lib.programs.token_metadata.accounts

import com.metaplex.lib.modules.nfts.models.MetaplexContstants
import com.solana.core.PublicKey
import org.bitcoinj.core.Base58
import org.junit.Assert
import org.junit.Test

class MetadataAccountTests {
    @Test
    fun testFindAddress() {
        // given
        val mintKey = PublicKey("HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn")
        val pdaSeeds = listOf(
            MetaplexContstants.METADATA_NAME.toByteArray(),
            Base58.decode(MetaplexContstants.METADATA_ACCOUNT_PUBKEY),
            mintKey.toByteArray()
        )

        val expectedPdaAddress = PublicKey.findProgramAddress(
            pdaSeeds,
            PublicKey(MetaplexContstants.METADATA_ACCOUNT_PUBKEY)
        )

        // when
        val pda = MetadataAccount.pda(mintKey).getOrThrows()

        // then
        Assert.assertEquals(expectedPdaAddress.address.toBase58(), pda.toBase58())
    }
}