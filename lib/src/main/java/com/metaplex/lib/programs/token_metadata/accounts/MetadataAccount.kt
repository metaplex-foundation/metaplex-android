package com.metaplex.lib.programs.token_metadata.accounts

import com.metaplex.lib.modules.nfts.models.MetaplexContstants
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey
import com.solana.core.PublicKeyRule
import com.solana.models.buffer.Buffer
import com.solana.networking.MoshiAdapterFactory
import com.solana.vendor.borshj.*
import com.squareup.moshi.FromJson
import org.bitcoinj.core.Base58

class MetadataAccountJsonAdapterFactory: MoshiAdapterFactory {
    override fun create(borsh: Borsh): Object {
        return MetadataAccountJsonAdapter(borsh)
    }
}

class MetadataAccountJsonAdapter(val borsh: Borsh): Object() {
    @FromJson
    fun fromJson(rawData: Any): Buffer<MetadataAccount> {
        return Buffer.create(borsh, rawData, MetadataAccount::class.java)
    }
}

data class MetadataAccount(
    val key: Int,
    val update_authority: PublicKey,
    val mint: PublicKey,
    val data: MetaplexData
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

class MetadataAccountRule(
    override val clazz: Class<MetadataAccount> = MetadataAccount::class.java
) : BorshRule<MetadataAccount> {
    override fun read(input: BorshInput): MetadataAccount {
        val key: Int = input.read().toInt()
        val updateAuthority: PublicKey = PublicKeyRule().read(input)
        val mint: PublicKey = PublicKeyRule().read(input)
        val data = MetaplexDataRule().read(input)
        return MetadataAccount(key, updateAuthority, mint, data)
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        TODO("Not yet implemented")
    }
}

class MetaplexDataAdapterJsonAdapterFactory: MoshiAdapterFactory {
    override fun create(borsh: Borsh): Object {
        return MetaplexDataAdapter(borsh)
    }
}

class MetaplexDataAdapter(val borsh: Borsh): Object() {
    @FromJson
    fun fromJson(rawData: Any): Buffer<MetaplexData> {
        return Buffer.create(borsh, rawData, MetaplexData::class.java)
    }
}

data class MetaplexData(
    val name: String,
    val symbol: String,
    val uri: String,
) : BorshCodable

class MetaplexDataRule(
    override val clazz: Class<MetaplexData> = MetaplexData::class.java
) : BorshRule<MetaplexData> {
    override fun read(input: BorshInput): MetaplexData {
        val name: String = input.readString().replace("\u0000", "")
        val symbol: String = input.readString().replace("\u0000", "")
        val uri: String = input.readString().replace("\u0000", "")
        return MetaplexData(name, symbol, uri)
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        TODO("Not yet implemented")
    }
}