/*
 * MockRpcDriver
 * Metaplex
 * 
 * Created by Funkatronics on 7/29/2022
 */

package com.metaplex.mock.driver.rpc

import com.metaplex.lib.drivers.rpc.*
import com.metaplex.lib.drivers.solana.*
import com.solana.core.PublicKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray

class MockErrorRpcDriver(val errorMessage: String) : JsonRpcDriver {
    override suspend fun <R> makeRequest(
        request: RpcRequest,
        resultSerializer: KSerializer<R>
    ): RpcResponse<R> = RpcResponse(error = RpcError(1234, errorMessage), id = request.id)
}

class MockRpcDriver(val autoConfirmTransactions: Boolean = false,
                    val autoHandleBlockhashRequest: Boolean = true) : JsonRpcDriver {

    val willReturn = mutableMapOf<RpcRequest, Any>()
    val willError = mutableMapOf<RpcRequest, RpcError>()

    inline fun <reified R : Any> willReturn(forRequest: RpcRequest, willReturn: R) {
        this.willReturn[forRequest] = willReturn
    }

    fun willError(forRequest: RpcRequest, willError: RpcError) {
        this.willError[forRequest] = willError
    }

    override suspend fun <R> makeRequest(
        request: RpcRequest,
        resultSerializer: KSerializer<R>
    ): RpcResponse<R> {

        if (autoConfirmTransactions) when (request) {
            is SendTransactionRequest -> return RpcResponse("transaction signature" as R)
            is SignatureStatusRequest -> return RpcResponse(listOf(
                SignatureStatus(1, 30, null, "finalized")
            ) as R)
        }

        if (autoHandleBlockhashRequest && request is RecentBlockhashRequest)
            return RpcResponse(BlockhashResponse(
                PublicKey(ByteArray(PublicKey.PUBLIC_KEY_LENGTH)).toString(),
                buildJsonObject {  }
            ) as R)

        findErrorForRequest(request)?.let { error ->
            return RpcResponse(error = RpcError(error.code, error.message))
        }
        return RpcResponse(findReturnForRequest(request) as R)
    }

    private fun findErrorForRequest(request: RpcRequest): RpcError? = willError[
            willError.keys.find { it.compareWith(request) }
    ]

    private fun findReturnForRequest(request: RpcRequest): Any? = willReturn[
            willReturn.keys.find { it.compareWith(request) }
    ]

    private fun RpcRequest.compareWith(other: RpcRequest) = this.method == other.method
            // only comparing the first parameter for now, so that I don't have to worry
            // about matching commitment levels etc. This is fragile and possible tricky
            // so will revisit if this causes issues in our testing purposes
            && this.params?.jsonArray?.first() == other.params?.jsonArray?.first()
}