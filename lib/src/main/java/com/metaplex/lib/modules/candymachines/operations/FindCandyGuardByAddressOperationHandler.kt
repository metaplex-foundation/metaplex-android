/*
 * FindCandyGuardByAddressOperationHandler
 * Metaplex
 * 
 * Created by Funkatronics on 10/26/2022
 */

package com.metaplex.lib.modules.candymachines.operations

import com.metaplex.kborsh.Borsh
import com.metaplex.lib.drivers.solana.AccountInfo
import com.metaplex.lib.drivers.solana.AccountRequest
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.candymachines.models.*
import com.metaplex.lib.modules.candymachines.models.CandyGuard
import com.metaplex.lib.serialization.serializers.base64.ByteArrayAsBase64JsonArraySerializer
import com.metaplex.lib.serialization.serializers.solana.SolanaResponseSerializer
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.OperationHandler
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FindCandyGuardByAddressOperationHandler(
    override val connection: Connection,
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : OperationHandler<PublicKey, CandyGuard> {

    override suspend fun handle(input: PublicKey): Result<CandyGuard> = withContext(dispatcher) {
        connection.get(
            AccountRequest(input.toString(), connection.transactionOptions),
            SolanaResponseSerializer(AccountInfo.serializer(ByteArrayAsBase64JsonArraySerializer))
        ).map {
            it?.data?.let {
                Borsh.decodeFromByteArray(CandyGuardSerializer, it)
            } ?: throw OperationError.NilDataOnAccount
        }
    }
}