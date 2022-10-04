/*
 * Airdrop
 * Metaplex
 * 
 * Created by Funkatronics on 9/23/2022
 */

package com.util

import com.metaplex.lib.drivers.rpc.RpcRequest
import com.metaplex.lib.drivers.solana.Connection
import com.solana.core.PublicKey
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
    get(AirdropRequest(wallet, amountSol), String.serializer())

//suspend fun Connection.airdropAndConfirm(wallet: PublicKey, amountSol: Float) {
//
//    val startingLamports =
//        get(AccountBalanceRequest(wallet), SolanaResponseSerializer(Long.serializer())).getOrNull()
//            ?: throw Error("could not read starting balance of wallet")
//
//    val expectedLamports = startingLamports + (amountSol*10f.pow(9)).toLong()
//
//    get(AirdropRequest(wallet, amountSol), String.serializer()).apply {
//
//        val result = getOrNull()
//
//        // wait for confirmation (hacky)
//        while (get(SignatureStatusRequest(listOf(result!!)), SignatureStatusesSerializer())
//                .getOrNull()?.first()?.confirmationStatus != "confirmed") {
//            val millis = System.currentTimeMillis(); var dummy = 0
//            while (System.currentTimeMillis() - millis < 500) dummy++
//        }
//
//        // wait for the balance to actually show up (super hacky)
//        while (get(AccountBalanceRequest(wallet), SolanaResponseSerializer(Long.serializer()))
//                .getOrNull() != expectedLamports) {
//            val millis = System.currentTimeMillis(); var dummy = 0
//            while (System.currentTimeMillis() - millis < 500) dummy++
//        }
//    }
//}