/*
 * SolanaRpcSerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.experimental.serialization.serializers.rpc.solana

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class SolanaResult<D>(val value: SolanaValue<D>) {
    constructor(data: D, executable: Boolean, lamports: Long,  owner: String?, rentEpoch: Long)
            : this(SolanaValue(data, executable, lamports, owner, rentEpoch))
}

@Serializable
data class SolanaValue<D>(@Contextual val data: DataWrapper<D>, val executable: Boolean,
                          val lamports: Long, val owner: String?, val rentEpoch: Long) {
    constructor(data: D, executable: Boolean, lamports: Long,  owner: String?, rentEpoch: Long)
            : this(DataWrapper(data), executable, lamports, owner, rentEpoch)
}

// region SUGAR ACCESSORS
val <T> SolanaResult<T>.data get() = value.data.data