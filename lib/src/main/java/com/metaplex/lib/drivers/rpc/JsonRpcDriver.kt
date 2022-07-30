/*
 * JsonRpcDriver
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.rpc

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement

/*
 * Trying out a new pattern here where the native interface supports serialization, but we also
 * provide a method overload that hides the serialization complexity through an extension method.
 * This way we can provide a more 'traditional' service layer api while simultaneously supporting
 * more advanced usage with custom response objects and/or serializers
 */

/**
 * JsonRpcDriver
 *
 * Makes a standard JSON-RPC 2.0 request and returns a [RpcResponse]
 *
 * @author Funkatronics
 */
interface JsonRpcDriver {

    /**
     * Performs the [request] and returns the resulting [RpcResponse]
     */
    suspend fun <R> makeRequest(request: RpcRequest,
                                resultSerializer: KSerializer<R>): RpcResponse<R>
}

suspend fun JsonRpcDriver.makeRequest(request: RpcRequest): DefaultRpcResponse =
    makeRequest(request, JsonElement.serializer())