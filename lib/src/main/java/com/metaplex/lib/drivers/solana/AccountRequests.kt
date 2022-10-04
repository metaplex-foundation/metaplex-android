/*
 * SolanaAccountResponse
 * Metaplex
 * 
 * Created by Funkatronics on 7/28/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.RpcRequest
import com.solana.core.PublicKey
import com.solana.models.DataSlice
import com.solana.models.RpcSendTransactionConfig
import kotlinx.serialization.json.*

class AccountRequest(
    accountAddress: String,
    encoding: Encoding = Encoding.base64,
    commitment: String = Commitment.MAX.toString(),
    length: Int? = null,
    offset: Int? = length?.let { 0 }
) : RpcRequest() {

    constructor(account: String, transactionOptions: TransactionOptions) : this(account,
        transactionOptions.encoding, transactionOptions.commitment.toString())

    override val method = "getAccountInfo"
    override val params = buildJsonArray {
        add(accountAddress)
        addJsonObject {
            put("encoding", encoding.getEncoding())
            put("commitment", commitment)
            length?.let {
                putJsonObject("dataSlice") {
                    put("length", length)
                    put("offset", offset)
                }
            }
        }
    }
}

class MultipleAccountsRequest(
    accounts: List<String>,
    encoding: Encoding = Encoding.base64,
    commitment: String = Commitment.MAX.toString(),
    length: Int? = null,
    offset: Int? = length?.let { 0 }
) : RpcRequest() {

    constructor(accounts: List<String>, transactionOptions: TransactionOptions) : this(accounts,
        transactionOptions.encoding, transactionOptions.commitment.toString())

    override val method = "getMultipleAccounts"
    override val params = buildJsonArray {
        addJsonArray {
            accounts.forEach {
                add(it)
            }
        }
        addJsonObject {
            put("encoding", encoding.getEncoding())
            put("commitment", commitment)
            length?.let {
                putJsonObject("dataSlice") {
                    put("length", length)
                    put("offset", offset)
                }
            }
        }
    }
}

class ProgramAccountRequest(
    account: String,
    encoding: Encoding = Encoding.base64,
    filters: List<Any>? = null,
    dataSlice: DataSlice? = null,
    commitment: String = "processed"
) : RpcRequest() {

    constructor(account: String, transactionOptions: TransactionOptions) : this(account,
        transactionOptions.encoding, commitment = transactionOptions.commitment.toString())

    override val method = "getProgramAccounts"
    override val params = buildJsonArray {
        add(account)
        addJsonObject {
            put("encoding", encoding.getEncoding())
            put("commitment", commitment)

            dataSlice?.let {
                putJsonObject("dataSlice") {
                    put("length", dataSlice.length)
                    put("offset", dataSlice.offset)
                }
            }

            filters?.let {
                put("filters", filters.toJsonArray())
            }
        }
    }

    private fun List<Any>.toJsonArray(): JsonArray = buildJsonArray {
        this@toJsonArray.forEach { filter ->
            (filter as? Map<String, Any>)?.let { filterMap ->
                add(filterMap.toJsonObject())
            } ?: when (filter) {
                is Number -> add(filter)
                is String -> add(filter)
                is Boolean -> add(filter)
            }
        }
    }

    private fun Map<String, Any>.toJsonObject(): JsonObject = buildJsonObject {
        this@toJsonObject.forEach { (k, v) ->
            (v as? Map<String, Any>)?.let {
                put(k, v.toJsonObject())
            } ?: when (v) {
                is Number -> put(k, v)
                is String -> put(k, v)
                is Boolean -> put(k, v)
            }
        }
    }
}

class MinAccountBalanceRequest(accountSize: Long, commitment: Commitment = Commitment.FINALIZED)
    : RpcRequest() {
    override val method = "getMinimumBalanceForRentExemption"
    override val params = buildJsonArray {
        add(accountSize)
        addJsonObject {
            put("commitment", commitment.toString())
        }
    }
}

class AccountBalanceRequest(wallet: PublicKey)  : RpcRequest() {
    override val method: String = "getBalance"
    override val params: JsonElement = buildJsonArray {
        add(wallet.toBase58())
    }
}