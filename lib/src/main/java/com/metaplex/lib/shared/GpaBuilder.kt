package com.metaplex.lib.shared

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.ProgramAccountRequest
import com.solana.core.PublicKey
import com.solana.core.PublicKeyRule
import com.solana.models.DataSlice
import com.solana.models.ProgramAccount
import com.solana.models.ProgramAccountConfig
import com.solana.models.RpcSendTransactionConfig
import com.solana.models.buffer.Buffer
import com.solana.models.buffer.BufferInfo
import com.solana.networking.MoshiAdapterFactory
import com.solana.vendor.borshj.*
import com.squareup.moshi.FromJson
import org.bitcoinj.core.Base58
import java.lang.RuntimeException
import java.math.BigInteger

class GetProgramAccountsConfig(val encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
                               val commitment: String = "processed",
                               val dataSlice: DataSlice? = null,
                               val filters: List<Map<String, Any>>? = null)

class AccountPublicKeyJsonAdapterFactory: MoshiAdapterFactory {
    override fun create(borsh: Borsh): Object {
        return AccountPublicKeyJsonAdapter(borsh)
    }
}

class AccountPublicKeyJsonAdapter(val borsh: Borsh): Object() {
    @FromJson
    fun fromJson(rawData: Any): Buffer<AccountPublicKey> {
        return Buffer.create(borsh, rawData, AccountPublicKey::class.java)
    }
}

class AccountPublicKeyRule(
    override val clazz: Class<AccountPublicKey> = AccountPublicKey::class.java
) : BorshRule<AccountPublicKey> {
    override fun read(input: BorshInput): AccountPublicKey {
        val publicKey = PublicKeyRule().read(input)
        return AccountPublicKey(publicKey)
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        TODO("Not yet implemented")
    }
}

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

    inline fun <reified B: BorshCodable> get(): OperationResult<List<AccountInfoWithPublicKey<B>>, Exception> {
        return OperationResult<List<ProgramAccount<B>>, ResultError> { cb ->
            this.connection.getProgramAccounts(
                this.programId,
                ProgramAccountConfig(this.config.encoding,  this.config.filters, this.config.dataSlice, this.config.commitment),
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

suspend fun GpaBuilder.getSuspend() = connection.getProgramAccounts(
    com.metaplex.lib.drivers.solana.AccountPublicKey.serializer(),
    programId,
    ProgramAccountConfig(config.encoding, config.filters, config.dataSlice, config.commitment)
)
