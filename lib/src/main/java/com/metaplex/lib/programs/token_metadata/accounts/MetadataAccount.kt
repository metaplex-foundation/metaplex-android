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
import com.squareup.moshi.ToJson
import org.bitcoinj.core.Base58
import java.lang.UnsupportedOperationException

class MetadataAccountJsonAdapterFactory : MoshiAdapterFactory {
    override fun create(borsh: Borsh): Object {
        return MetadataAccountJsonAdapter(borsh)
    }
}

class MetadataAccountJsonAdapter(val borsh: Borsh) : Object() {
    @FromJson
    fun fromJson(rawData: Any): Buffer<MetadataAccount> {
        return Buffer.create(borsh, rawData, MetadataAccount::class.java)
    }

    @ToJson
    fun toJson(metadataAccount: Buffer<MetadataAccount>): String {
        throw UnsupportedOperationException()
    }
}

enum class MetaplexTokenStandard {
    NonFungible, FungibleAsset, Fungible, NonFungibleEdition

}

data class MetaplexCollection(
    val verified: Boolean,
    val key: PublicKey,
) : BorshCodable

class MetaplexCollectionRule(
    override val clazz: Class<MetaplexCollection> = MetaplexCollection::class.java
) : BorshRule<MetaplexCollection> {
    override fun read(input: BorshInput): MetaplexCollection {
        val verified = input.readBoolean()
        val key = PublicKey(input.readFixedArray(32))
        return MetaplexCollection(verified, key)
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        TODO("Not yet implemented")
    }
}

data class MetadataAccount(
    val key: Int,
    val update_authority: PublicKey,
    val mint: PublicKey,
    val data: MetaplexData,
    val primarySaleHappened: Boolean,
    val isMutable: Boolean,
    val editionNonce: Int?,
    val tokenStandard: MetaplexTokenStandard?,
    val collection: MetaplexCollection?
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
        val primarySaleHappened = input.readBoolean()
        val isMutable: Boolean = input.readBoolean()
        val hasEditionNonce: Boolean = input.readBoolean()
        var editionNonce: Int? = null
        if (hasEditionNonce) {
            editionNonce = 256 + input.read().toInt() // The byte is inverted we fix the inversion on the byte to int.
        }
        val tokenStandard = MetaplexTokenStandard.values().getOrNull(input.read().toInt())
        val hasCollection: Boolean = input.readBoolean()
        var collection: MetaplexCollection? = null
        if (hasCollection) {
            collection = MetaplexCollectionRule().read(input)
        }
        return MetadataAccount(
            key = key,
            update_authority = updateAuthority,
            mint = mint,
            data = data,
            primarySaleHappened = primarySaleHappened,
            isMutable = isMutable,
            editionNonce = editionNonce,
            tokenStandard = tokenStandard,
            collection = collection
        )
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        TODO("Not yet implemented")
    }
}

class MetaplexDataAdapterJsonAdapterFactory : MoshiAdapterFactory {
    override fun create(borsh: Borsh): Object {
        return MetaplexDataAdapter(borsh)
    }
}

class MetaplexDataAdapter(val borsh: Borsh) : Object() {
    @FromJson
    fun fromJson(rawData: Any): Buffer<MetaplexData> {
        return Buffer.create(borsh, rawData, MetaplexData::class.java)
    }
}

data class MetaplexData(
    val name: String,
    val symbol: String,
    val uri: String,
    val sellerFeeBasisPoints: Int,
    val hasCreators: Boolean,
    val addressCount: Int,
    val creators: Array<MetaplexCreator>
) : BorshCodable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MetaplexData
        if (name != other.name) return false
        if (symbol != other.symbol) return false
        if (uri != other.uri) return false
        if (sellerFeeBasisPoints != other.sellerFeeBasisPoints) return false
        if (hasCreators != other.hasCreators) return false
        if (addressCount != other.addressCount) return false
        if (!creators.contentEquals(other.creators)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + symbol.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + sellerFeeBasisPoints
        result = 31 * result + hasCreators.hashCode()
        result = 31 * result + addressCount
        result = 31 * result + creators.contentHashCode()
        return result
    }
}

class MetaplexDataRule(
    override val clazz: Class<MetaplexData> = MetaplexData::class.java
) : BorshRule<MetaplexData> {
    override fun read(input: BorshInput): MetaplexData {
        val name = input.readString().replace("\u0000", "")
        val symbol = input.readString().replace("\u0000", "")
        val uri = input.readString().replace("\u0000", "")
        val sellerFeeBasisPoints = input.readU16().toInt()
        val hasCreators = input.readBoolean()
        val creatorsArray = arrayListOf<MetaplexCreator>()
        if (hasCreators) {
            val addressCount = input.readU32()
            for (i in 0 until addressCount) {
                val creator = MetaplexCreatorRule().read(input)
                creatorsArray.add(creator)
            }
        }
        return MetaplexData(
            name = name,
            symbol = symbol,
            uri = uri,
            sellerFeeBasisPoints = sellerFeeBasisPoints,
            hasCreators = hasCreators,
            addressCount = creatorsArray.count(),
            creators = creatorsArray.toTypedArray()
        )
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        TODO("Not yet implemented")
    }
}

data class MetaplexCreator(
    val address: PublicKey,
    val verified: Int,
    val share: Int,
) : BorshCodable

class MetaplexCreatorRule(
    override val clazz: Class<MetaplexCreator> = MetaplexCreator::class.java
) : BorshRule<MetaplexCreator> {
    override fun read(input: BorshInput): MetaplexCreator {
        val address = PublicKeyRule().read(input)
        val verified = input.readU8().toInt()
        val share = input.readU8().toInt()
        return MetaplexCreator(address, verified, share)
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        TODO("Not yet implemented")
    }
}