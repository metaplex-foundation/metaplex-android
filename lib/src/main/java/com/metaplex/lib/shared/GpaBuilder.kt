package com.metaplex.lib.shared

import com.metaplex.lib.solana.Connection
import com.solana.core.PublicKey
import com.solana.models.ProgramAccount
import com.solana.models.ProgramAccountConfig
import com.solana.models.RpcSendTransactionConfig
import com.solana.models.buffer.BufferInfo
import com.solana.vendor.borshj.BorshCodable
import org.bitcoinj.core.Base58
import java.lang.RuntimeException
import java.nio.ByteBuffer

class GetProgramAccountsConfig(val encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
                               val commitment: String = "processed",
                               val filters: List<Map<String, Any>>? = null)

data class AccountPublicKey (
    val publicKey: PublicKey
): BorshCodable

data class AccountInfoWithPublicKey<B: BorshCodable> (
    val pubkey: PublicKey,
    val account: BufferInfo<B>
)

fun GetProgramAccountsConfig.merge(
    other: GetProgramAccountsConfig
) = GetProgramAccountsConfig(
    other.encoding,
    other.commitment,
    other.filters ?: this.filters
)

data class RequestMemCmpFilter (
    val offset: UInt,
    val bytes: String
) {
    fun toDict() = mapOf<String, Any>(
        "offset" to offset,
        "bytes" to bytes
    )
}

typealias RequestDataSizeFilter = UInt

fun GetProgramAccountsConfig.copyAndReplace(
     encoding: RpcSendTransactionConfig.Encoding? = null,
     commitment: String? = null,
     filters: List<Map<String, Any>>? = null
) = GetProgramAccountsConfig(
    encoding ?: this.encoding,
    commitment ?: this.commitment,
    filters ?: this.filters
)


class GpaBuilder(val connection: Connection, val programId: PublicKey) {

    var config: GetProgramAccountsConfig = GetProgramAccountsConfig()

    fun mergeConfig(config: GetProgramAccountsConfig): GpaBuilder {
        this.config = this.config.merge(config)
        return this
    }

    fun slice(offset: Int, length: Int): GpaBuilder {
        this.config = this.config.copyAndReplace()
        return this
    }

    fun withoutData(): GpaBuilder {
        return this.slice(0, 0)
    }

    fun addFilter(filter: Map<String, Any>): GpaBuilder {
        val filters: MutableList<Map<String, Any>> = (this.config.filters?.toMutableList() ?: mutableListOf())
        filters.add(filter)
        this.config = this.config.copyAndReplace(filters = filters)
        return this
    }

    fun where(offset: UInt, publicKey: PublicKey): GpaBuilder {
        val memcmpParams = RequestMemCmpFilter(offset, publicKey.toBase58())
        return this.addFilter(mapOf(
                "memcmp" to memcmpParams.toDict()
            )
        )
    }

    fun where(offset: UInt, bytes: ByteArray): GpaBuilder {
        Base58.encode(bytes)
        val memcmpParams = RequestMemCmpFilter(offset, Base58.encode(bytes))
        return this.addFilter(mapOf(
                "memcmp" to memcmpParams.toDict()
            )
        )
    }

    fun where(offset: UInt, string: String): GpaBuilder {
        val memcmpParams = RequestMemCmpFilter(offset, string)
        return this.addFilter(mapOf(
                "memcmp" to memcmpParams.toDict()
            )
        )
    }

    fun where(offset: UInt, int: Int): GpaBuilder {
        val memcmpParams = RequestMemCmpFilter(offset, Base58.encode(intToBytes(int)))
        return this.addFilter(mapOf(
                "memcmp" to memcmpParams.toDict()
            )
        )
    }

    fun where(offset: UInt, byte: Byte): GpaBuilder {
        val memcmpParams = RequestMemCmpFilter(offset, Base58.encode(listOf(byte).toByteArray()))
        return this.addFilter(mapOf(
                "memcmp" to memcmpParams.toDict()
            )
        )
    }

    fun whereSize(dataSize: UInt): GpaBuilder {
        val requestDataSize: RequestDataSizeFilter = dataSize
        return this.addFilter(mapOf("dataSize" to requestDataSize))
    }

    inline fun <reified B: BorshCodable> get(): OperationResult<List<AccountInfoWithPublicKey<B>>, Exception> {
        return OperationResult<List<ProgramAccount<B>>, ResultError> { cb ->
            this.connection.getProgramAccounts(
                this.programId,
                ProgramAccountConfig(this.config.encoding, this.config.filters, this.config.commitment),
                B::class.java
            ){ result ->
                result.onSuccess {
                    cb(ResultWithCustomError.success(it))
                }.onFailure {
                    cb(ResultWithCustomError.failure(RuntimeException(it)))
                }
            }
        }.map { programAccounts ->
            val infoAccounts = mutableListOf<AccountInfoWithPublicKey<B>>()
            for (programAccount in programAccounts){
                val infoAccount = AccountInfoWithPublicKey(PublicKey(programAccount.pubkey), programAccount.account)
                infoAccounts.add(infoAccount)
            }
            infoAccounts
        }
    }

    inline fun <reified B: BorshCodable, T> getAndMap(noinline callback: (account: List<AccountInfoWithPublicKey<B>>) -> T): OperationResult<T, Exception> {
        return this.get<B>().map(callback)
    }

    fun getPublicKeys(): OperationResult<List<PublicKey>, Exception> {
        return this.getAndMap { account: List<AccountInfoWithPublicKey<AccountPublicKey>> ->
            account.map { it.pubkey }
        }
    }

    fun getDataAsPublicKeys(): OperationResult<List<PublicKey>, Exception> {
        return this.getAndMap { account: List<AccountInfoWithPublicKey<AccountPublicKey>> ->
            account.map {
                it.account.data!!.value!!.publicKey
            }
        }
    }
}

fun intToBytes(i: Int): ByteArray =
    ByteBuffer.allocate(Int.SIZE_BYTES).putInt(i).array()