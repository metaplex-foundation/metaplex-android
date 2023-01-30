package com.metaplex.lib.modules.nfts.builders

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.tokenmetadata.TokenMetadataInstructions.Transfer
import com.metaplex.lib.experimental.jen.tokenmetadata.TransferArgs
import com.metaplex.lib.modules.nfts.*
import com.metaplex.lib.programs.token_metadata.TokenMetadataProgram
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Sysvar.SYSVAR_INSTRUCTIONS_PUBKEY
import com.solana.core.Transaction
import com.solana.programs.AssociatedTokenProgram
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class TransferNftInput(
    val mintKey: PublicKey, // also know as Token Address
    val authority: TokenMetadataAuthority? = null,
    val authorizationDetails: TokenMetadataAuthorizationDetails? = null,
    val fromOwner : PublicKey? = null,
    val fromToken : PublicKey? = null,
    val toOwner : PublicKey,
    val toToken : PublicKey? = null,
    val amount : ULong = 1u,
)

data class TransferNftBuilderParams(val input: TransferNftInput, val instructionKey: String? = null)

class TransferNftBuilder(
    val params: TransferNftBuilderParams,
    private val identityDriver: IdentityDriver,
    connection: Connection,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionBuilder(
    payer = params.input.fromOwner ?:
            getSignerFromTokenMetadataAuthority(TokenMetadataAuthority.Signer(identityDriver)).publicKey(),
    connection,
    dispatcher
) {
    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {
            val mintKey = params.input.mintKey
            val authority = params.input.authority ?: TokenMetadataAuthority.Signer(identityDriver)
            val toOwner = params.input.toOwner
            val amount = params.input.amount
            val authorizationDetails = params.input.authorizationDetails

            // From owner.
            val fromOwner = params.input.fromOwner ?: getSignerFromTokenMetadataAuthority(authority).publicKey()

            // Programs.
            val tokenMetadataProgram = TokenMetadataProgram.publicKey
            val ataProgram = AssociatedTokenProgram.SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID
            val tokenProgram = TokenProgram.PROGRAM_ID
            val systemProgram = SystemProgram.PROGRAM_ID

            // PDAs.
            val metadata = NftPdasClient.metadata(mintKey).getOrThrow()
            val edition = NftPdasClient.masterEdition(mintKey).getOrThrow()

            val fromToken = params.input.fromToken ?: PublicKey.associatedTokenAddress(fromOwner, mintKey).address
            val toToken = params.input.toToken ?: PublicKey.associatedTokenAddress(toOwner, mintKey).address

            val ownerTokenRecord = NftPdasClient.tokenRecord(mintKey, fromToken).getOrThrow()
            val destinationTokenRecord = NftPdasClient.tokenRecord(mintKey, toToken).getOrThrow()

            // Auth.
            val auth = parseTokenMetadataAuthorization(
                mintKey,
                when(authority){
                    is TokenMetadataAuthority.Auth -> authority
                    is TokenMetadataAuthority.Signer -> TokenMetadataAuthority.Auth.TokenMetadataAuthorityHolder(
                        AccountOrPK.isAccount(authority.account),
                        fromToken
                    )
                },
                authorizationDetails,
            )

            addInstruction(
                Transfer(
                    token= fromToken,
                    tokenOwner= fromOwner,
                    destination= toToken,
                    destinationOwner= toOwner,
                    mint= mintKey,
                    metadata= metadata,
                    edition = edition,
                    ownerTokenRecord = ownerTokenRecord,
                    destinationTokenRecord= destinationTokenRecord,
                    authority= auth.accounts.authority,
                    payer= payer,
                    systemProgram= systemProgram,
                    sysvarInstructions= SYSVAR_INSTRUCTIONS_PUBKEY,
                    splTokenProgram= tokenProgram,
                    splAtaProgram= ataProgram,
                    authorizationRules= auth.accounts.authorizationRules,
                    authorizationRulesProgram = auth.accounts.authorizationRules,
                    transferArgs = TransferArgs.V1(amount, auth.data.authorizationData),
                )
            )
        })
    }

}