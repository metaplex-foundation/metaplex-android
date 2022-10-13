/*
 * SignatureStatus
 * Metaplex
 * 
 * Created by Funkatronics on 9/2/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.RpcRequest
import com.metaplex.lib.serialization.serializers.solana.SolanaResponseSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.*

class SignatureStatusRequest(signatures: List<String>,
                             searchTransactionHistory: Boolean = false) : RpcRequest() {
    override val method = "getSignatureStatuses"
    override val params = buildJsonArray {
        addJsonArray {
            signatures.forEach {
                add(it)
            }
        }
        addJsonObject {
            put("searchTransactionHistory", searchTransactionHistory)
        }
    }
}

@Serializable
data class SignatureStatus(
    val slot: Long,
    val confirmations: Long?,
    var err: JsonObject?,
    var confirmationStatus: String?
)

internal fun SignatureStatusesSerializer() =
    SolanaResponseSerializer(ListSerializer(SignatureStatus.serializer().nullable))