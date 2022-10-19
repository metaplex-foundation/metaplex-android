/*
 * FindTokenMetadataAccountOperation
 * Metaplex
 * 
 * Created by Funkatronics on 8/30/2022
 */

package com.metaplex.lib.modules.token.operations

import com.metaplex.lib.drivers.solana.*
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.metaplex.lib.serialization.serializers.solana.ByteDiscriminatorSerializer
import com.metaplex.lib.serialization.serializers.solana.SolanaResponseSerializer
import com.metaplex.lib.shared.SuspendOperation
import com.solana.core.PublicKey

class FindTokenMetadataAccountOperation(override val connection: Connection)
    : SuspendOperation<PublicKey, AccountInfo<MetadataAccount>> {
    override suspend fun run(input: PublicKey): Result<AccountInfo<MetadataAccount>> =
        connection.getAccountInfo(MetadataAccount.serializer(), MetadataAccount.pda(input).getOrThrows())
}