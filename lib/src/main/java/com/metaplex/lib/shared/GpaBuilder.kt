package com.metaplex.lib.shared

import com.metaplex.lib.drivers.solana.Connection
import com.solana.core.PublicKey
import com.solana.models.DataSlice
import com.solana.models.ProgramAccountConfig
import com.solana.models.RpcSendTransactionConfig
import org.bitcoinj.core.Base58
import java.math.BigInteger

class GetProgramAccountsConfig(val encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
                               val commitment: String = "processed",
                               val dataSlice: DataSlice? = null,
                               val filters: List<Map<String, Any>>? = null)

fun GetProgramAccountsConfig.merge(
    other: GetProgramAccountsConfig
) = GetProgramAccountsConfig(
    other.encoding,
    other.commitment,
    other.dataSlice ?: this.dataSlice,
    other.filters ?: this.filters
)

data class RequestMemCmpFilter (
    val offset: Int,
    val bytes: String
) {
    fun toDict() = mapOf<String, Any>(
        "offset" to offset,
        "bytes" to bytes
    )
}

typealias RequestDataSizeFilter = Int

fun GetProgramAccountsConfig.copyAndReplace(
     encoding: RpcSendTransactionConfig.Encoding? = null,
     commitment: String? = null,
     dataSlice: DataSlice? = null,
     filters: List<Map<String, Any>>? = null
) = GetProgramAccountsConfig(
    encoding ?: this.encoding,
    commitment ?: this.commitment,
    dataSlice ?: this.dataSlice,
    filters ?: this.filters
)

class GpaBuilderFactory {
    companion object {
        fun <T: GpaBuilder>from(instance: Class<T>, builder: GpaBuilder): T {
            val newBuilder = instance.constructors.first().newInstance(builder.connection, builder.programId) as T
            return newBuilder.mergeConfig(builder.config)
        }
    }
}

abstract class GpaBuilder(open val connection: Connection, open val programId: PublicKey) {

    var config: GetProgramAccountsConfig = GetProgramAccountsConfig()

    fun <T: GpaBuilder>mergeConfig(config: GetProgramAccountsConfig): T {
        this.config = this.config.merge(config)
        return this as T
    }

    fun <T: GpaBuilder>slice(offset: Int, length: Int): T {
        this.config = this.config.copyAndReplace(dataSlice = DataSlice(offset, length))
        return this as T
    }

    fun <T: GpaBuilder>withoutData(): T {
        return this.slice(0, 0)
    }

    fun <T: GpaBuilder> addFilter(filter: Map<String, Any>): T {
        val filters: MutableList<Map<String, Any>> = (this.config.filters?.toMutableList() ?: mutableListOf())
        filters.add(filter)
        this.config = this.config.copyAndReplace(filters = filters)
        return this as T
    }

    fun <T: GpaBuilder>where(offset: Int, publicKey: PublicKey): T {
        val memcmpParams = RequestMemCmpFilter(offset, publicKey.toBase58())
        return this.addFilter(mapOf(
                "memcmp" to memcmpParams.toDict()
            )
        )
    }

    fun <T: GpaBuilder>where(offset: Int, bytes: ByteArray): T {
        Base58.encode(bytes)
        val memcmpParams = RequestMemCmpFilter(offset, Base58.encode(bytes))
        return this.addFilter(mapOf(
                "memcmp" to memcmpParams.toDict()
            )
        )
    }

    fun <T: GpaBuilder>where(offset: Int, string: String): T {
        val memcmpParams = RequestMemCmpFilter(offset, string)
        return this.addFilter(mapOf(
                "memcmp" to memcmpParams.toDict()
            )
        )
    }

    fun <T: GpaBuilder>where(offset: Int, int: Long): T {
        val memcmpParams = RequestMemCmpFilter(offset, Base58.encode(BigInteger.valueOf(int).toByteArray()))
        return this.addFilter(mapOf(
                "memcmp" to memcmpParams.toDict()
            )
        )
    }

    fun <T: GpaBuilder>where(offset: Int, byte: Byte): T {
        val memcmpParams = RequestMemCmpFilter(offset, Base58.encode(listOf(byte).toByteArray()))
        return this.addFilter(mapOf(
                "memcmp" to memcmpParams.toDict()
            )
        )
    }

    fun <T: GpaBuilder>whereSize(dataSize: Int): T {
        val requestDataSize: RequestDataSizeFilter = dataSize
        return this.addFilter(mapOf("dataSize" to requestDataSize))
    }

    suspend fun get() = connection.getProgramAccounts(
        com.metaplex.lib.drivers.solana.AccountPublicKey.serializer(),
        programId,
        ProgramAccountConfig(config.encoding, config.filters, config.dataSlice, config.commitment)
    )

    suspend fun getPublicKeys(): Result<List<PublicKey>> =
        this.get().map { account -> account.map { PublicKey(it.publicKey) } }

    suspend fun getDataAsPublicKeys(): Result<List<PublicKey>> =
        this.get().map { account -> account.map { it.account.data!!.publicKey } }
}
