/*
 * RpcResponse
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.experimental.serialization.serializers.rpc

import kotlinx.serialization.Serializable

//@Serializable
//data class RpcResponse<out R>(val result: R?, val error: Error?)
//
//@Serializable
//data class Error(val code: Int, val message: String)

sealed interface RpcResponseI

@Serializable
data class RpcResult<out R>(val result: R?) : RpcResponseI

sealed interface RpcCallError : RpcResponseI

@Serializable
data class RpcError(val code: Int, val message: String) : RpcCallError

@Serializable
data class RpcErrorWithData<D>(val code: Int, val message: String,
                               val data: D? = null) : RpcCallError