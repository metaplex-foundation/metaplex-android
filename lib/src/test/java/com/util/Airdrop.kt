/*
 * Airdrop
 * Metaplex
 * 
 * Created by Funkatronics on 9/23/2022
 */

package com.util

import com.metaplex.lib.drivers.rpc.RpcRequest
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.extensions.confirmTransaction
import com.solana.core.PublicKey
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import kotlin.math.pow

class AirdropRequest(wallet: PublicKey, lamports: Long, commitment: String = "confirmed") : RpcRequest() {

    constructor(wallet: PublicKey, amountSol: Float, commitment: String = "confirmed")
            : this(wallet, (amountSol*10f.pow(9)).toLong(), commitment)

    override val method: String = "requestAirdrop"
    override val params: JsonElement = buildJsonArray {
        add(wallet.toBase58())
        add(lamports)
        addJsonObject {
            put("commitment", commitment)
        }
    }
}

suspend fun Connection.airdrop(wallet: PublicKey, amountSol: Float) =
    get(AirdropRequest(wallet, amountSol), String.serializer()).confirmTransaction(this)