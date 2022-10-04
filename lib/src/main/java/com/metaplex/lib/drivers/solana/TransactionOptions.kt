/*
 * TransactionOptions
 * Metaplex
 * 
 * Created by Funkatronics on 10/4/2022
 */

package com.metaplex.lib.drivers.solana

import com.solana.models.RpcSendTransactionConfig
import java.time.Duration

typealias Encoding = RpcSendTransactionConfig.Encoding

enum class Commitment(val value: String) {
    PROCESSED("processed"),
    CONFIRMED("confirmed"),
    FINALIZED("finalized"),
    MAX("max");

    override fun toString(): String {
        return value
    }
}

data class TransactionOptions(
    val commitment: Commitment = Commitment.FINALIZED,
    val encoding: Encoding = Encoding.base64,
    val skipPreflight: Boolean = false,
    val preflightCommitment: Commitment = commitment,
    val timeout: Duration = Duration.ofSeconds(30)
)