/*
 * TestAccountInfo
 * Metaplex
 * 
 * Created by Funkatronics on 8/2/2022
 */

package com.metaplex.data.model

import com.metaplex.lib.drivers.solana.AccountInfo
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.intellij.lang.annotations.Language

typealias TestAccountResponse<A> = AccountInfo<A>

fun <A> TestAccountResponse(account: A) =
    TestAccountResponse(account, true, 0, "", 0)

@Language("json")
internal fun <A> TestAccountResponse<A>.json(serializer: KSerializer<A?>) = """
    {
        "data": ${Json.encodeToString(serializer, data!!)},
        "executable": $executable,
        "lamports": $lamports,
        "owner": "${owner}",
        "rentEpoch": $rentEpoch
    }
    """

@Language("json")
internal fun <A> TestAccountResponse<A>.responseJson(serializer: KSerializer<A?>) = """
    {
        "value": ${this.json(serializer)} 
    }
    """