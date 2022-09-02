/*
 * FindTokenMetadataAccountOperation
 * Metaplex
 * 
 * Created by Funkatronics on 8/30/2022
 */

package com.metaplex.lib.modules.token.operations

import com.metaplex.lib.drivers.solana.AccountInfo
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.serialization.serializers.legacy.BorshCodeableSerializer
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.shared.SuspendOperation
import com.solana.core.PublicKey

class FindTokenMetadataAccountOperation(override val connection: Connection)
    : SuspendOperation<PublicKey, AccountInfo<MetadataAccount>> {
    override suspend fun run(input: PublicKey): Result<AccountInfo<MetadataAccount>> =
        @Suppress("UNCHECKED_CAST")
        connection.getAccountInfo(BorshCodeableSerializer(MetadataAccount::class.java),
            MetadataAccount.pda(input).getOrThrows()).map {
            it as AccountInfo<MetadataAccount> // safe cast, we know the returned type
        }
}