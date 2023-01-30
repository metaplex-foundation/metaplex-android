/*
 * FindTokenMasterEditionAccount
 * Metaplex
 * 
 * Created by Funkatronics on 8/31/2022
 */

package com.metaplex.lib.modules.token.operations

import com.metaplex.lib.drivers.solana.AccountInfo
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.programs.token_metadata.accounts.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.accounts.MasterEditionAccountSerializer
import com.metaplex.lib.shared.SuspendOperation
import com.solana.core.PublicKey

class FindTokenMasterEditionAccountOperation(override val connection: Connection)
    : SuspendOperation<PublicKey, AccountInfo<MasterEditionAccount>> {
    override suspend fun run(input: PublicKey): Result<AccountInfo<MasterEditionAccount>> =
        connection.getAccountInfo(
            MasterEditionAccountSerializer,
            MasterEditionAccount.pda(input).getOrThrows())
}