/*
 * SendTransactionRequest
 * Metaplex
 * 
 * Created by Funkatronics on 9/22/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.RpcRequest
import kotlinx.serialization.json.*

class SendTransactionRequest(serializedMessage: String,
                             skipPreflight: Boolean = false,
                             preflightCommitment: Commitment = Commitment.FINALIZED,
                             encoding: Encoding = Encoding.base64) : RpcRequest() {

    constructor(serializedMessage: String, transactionOptions: TransactionOptions) : this(
        serializedMessage,
        transactionOptions.skipPreflight,
        transactionOptions.preflightCommitment,
        transactionOptions.encoding
    )

    override val method = "sendTransaction"
    override val params = buildJsonArray {
        add(serializedMessage)
        addJsonObject {
            put("skipPreflight", skipPreflight)
            put("preflightCommitment", preflightCommitment.toString())
            put("encoding", encoding.getEncoding())
        }
    }
}