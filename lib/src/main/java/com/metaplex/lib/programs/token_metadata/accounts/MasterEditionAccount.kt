package com.metaplex.lib.programs.token_metadata

import com.google.protobuf.UInt64Value
import com.metaplex.lib.modules.nfts.models.MetaplexContstants
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexDataRule
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey
import com.solana.core.PublicKeyRule
import com.solana.models.buffer.Buffer
import com.solana.networking.MoshiAdapterFactory
import com.solana.vendor.borshj.*
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.bitcoinj.core.Base58
import java.lang.UnsupportedOperationException

enum class MetadataKey {
     Uninitialized, // 0
     EditionV1, // 1
     MasterEditionV1, // 2
     ReservationListV1, // 3
     MetadataV1, // 4,
     ReservationListV2, // 5
     MasterEditionV2, // 6,
     EditionMarker, // 7
     UseAuthorityRecord, // 8
     CollectionAuthorityRecord, // 9
}

class MasterEditionAccountJsonAdapterFactory: MoshiAdapterFactory {
    override fun create(borsh: Borsh): Object {
        return MasterEditionAccountJsonAdapter(borsh)
    }
}

class MasterEditionAccountJsonAdapter(val borsh: Borsh): Object() {
    @FromJson
    fun fromJson(rawData: Any): Buffer<MasterEditionAccount> {
        return Buffer.create(borsh, rawData, MasterEditionAccount::class.java)
    }

    @ToJson
    fun toJson(masterEditionAccount: Buffer<MasterEditionAccount>): String {
        throw UnsupportedOperationException()
    }
}

class MasterEditionAccountRule(
    override val clazz: Class<MasterEditionAccount> = MasterEditionAccount::class.java
) : BorshRule<MasterEditionAccount> {
    override fun read(input: BorshInput): MasterEditionAccount {
        val key: Int = input.read().toInt()
        val masterEditionVersion = if (key == MetadataKey.MasterEditionV1.ordinal) {
            val masterEditionV1 = MasterEditionV1AccountRule().read(input)
            MasterEditionVersion.masterEditionV1(masterEditionV1)
        } else {
            val masterEditionV2 = MasterEditionV2AccountRule().read(input)
            MasterEditionVersion.masterEditionV2(masterEditionV2)
        }
        return MasterEditionAccount(key, masterEditionVersion)
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        throw UnsupportedOperationException()
    }
}
sealed class MasterEditionVersion {
    data class masterEditionV1(val masterEditionV1: MasterEditionV1): MasterEditionVersion()
    data class masterEditionV2(val masterEditionV2: MasterEditionV2): MasterEditionVersion()
}
data class MasterEditionAccount(
    val type: Int,
    val masterEditionVersion: MasterEditionVersion
): BorshCodable {
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

class MasterEditionV1AccountRule(
    override val clazz: Class<MasterEditionV1> = MasterEditionV1::class.java
) : BorshRule<MasterEditionV1> {
    override fun read(input: BorshInput): MasterEditionV1 {
        val supply = input.readU64()
        val max_supply = input.readU64()
        val printing_mint = PublicKeyRule().read(input)
        val one_time_printing_authorization_mint = PublicKeyRule().read(input)
        return MasterEditionV1(supply, max_supply, printing_mint, one_time_printing_authorization_mint)
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        throw UnsupportedOperationException()
    }
}

data class MasterEditionV1 (
    val supply: Long?,
    val max_supply: Long?,
    val printing_mint: PublicKey,
    val one_time_printing_authorization_mint: PublicKey
): BorshCodable

class MasterEditionV2AccountRule(
    override val clazz: Class<MasterEditionV2> = MasterEditionV2::class.java
) : BorshRule<MasterEditionV2> {
    override fun read(input: BorshInput): MasterEditionV2 {
        val supply = input.readU64()
        val max_supply = input.readU64()
        return MasterEditionV2(supply, max_supply)
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        TODO("Not yet implemented")
    }
}

data class MasterEditionV2 (
    val supply: Long?,
    val max_supply: Long?
): BorshCodable