/*
 * FindCandyGuardByAddressOperationHandler
 * Metaplex
 * 
 * Created by Funkatronics on 10/26/2022
 */

package com.metaplex.lib.modules.candymachines.operations

import com.metaplex.lib.drivers.solana.AccountInfo
import com.metaplex.lib.drivers.solana.AccountRequest
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candyguard.*
import com.metaplex.lib.modules.candymachines.CANDY_GUARD_LABEL_SIZE
import com.metaplex.lib.modules.candymachines.models.*
import com.metaplex.lib.modules.candymachines.models.CandyGuard
import com.metaplex.lib.serialization.format.Borsh
import com.metaplex.lib.serialization.serializers.base64.ByteArrayAsBase64JsonArraySerializer
import com.metaplex.lib.serialization.serializers.solana.AnchorAccountSerializer
import com.metaplex.lib.serialization.serializers.solana.SolanaResponseSerializer
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.OperationHandler
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import java.nio.charset.StandardCharsets

import com.metaplex.lib.experimental.jen.candyguard.CandyGuard as CandyGuardAccount

class FindCandyGuardByAddressOperationHandler(
    override val connection: Connection,
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : OperationHandler<PublicKey, CandyGuard> {

    override suspend fun handle(input: PublicKey): Result<CandyGuard> = withContext(dispatcher) {
        connection.get(
            AccountRequest(input.toString(), connection.transactionOptions),
            SolanaResponseSerializer(AccountInfo.serializer(ByteArrayAsBase64JsonArraySerializer))
        ).map {
            it?.data?.let { accountData ->
                Borsh.decodeFromByteArray(object : DeserializationStrategy<CandyGuard> {
                    override val descriptor: SerialDescriptor =
                        CandyGuardAccount.serializer().descriptor
                    override fun deserialize(decoder: Decoder): CandyGuard {
                        // decode account data
                        val account = decoder.decodeSerializableValue(AnchorAccountSerializer<CandyGuardAccount>())

                        // decode default guards
                        val defaultGuardSet = GuardSetDeserializer().deserialize(decoder)

                        // decode guard groups
                        val groupCount = decoder.decodeInt()
                        val groups = (0 until groupCount).associate {
                            // decode fixed size label string
                            val bytes = ByteArray(CANDY_GUARD_LABEL_SIZE) { decoder.decodeByte() }
                            val label = String(bytes, StandardCharsets.UTF_8)
                                .replace("\u0000", "")

                            // build guard group
                            label to GuardSetDeserializer().deserialize(decoder)
                        }

                        return CandyGuard(account.base, account.authority, defaultGuardSet, groups)
                    }
                }, accountData)
            } ?: throw OperationError.NilDataOnAccount
        }
    }
}

fun List<Any>.toGuardSet() = GuardSet(
    botTax = find { it is BotTax } as? BotTax,
    solPayment = find { it is SolPayment } as? SolPayment,
    tokenPayment = find { it is TokenPayment } as? TokenPayment,
    startDate = find { it is StartDate } as? StartDate,
    thirdPartySigner = find { it is ThirdPartySigner } as? ThirdPartySigner,
    tokenGate = find { it is TokenGate } as? TokenGate,
    gatekeeper = find { it is Gatekeeper } as? Gatekeeper,
    endDate = find { it is EndDate } as? EndDate,
    allowList = find { it is AllowList } as? AllowList,
    mintLimit = find { it is MintLimit } as? MintLimit,
    nftPayment = find { it is NftPayment } as? NftPayment,
    redeemedAmount = find { it is RedeemedAmount } as? RedeemedAmount,
    addressGate = find { it is AddressGate } as? AddressGate,
    nftGate = find { it is NftGate } as? NftGate,
    nftBurn = find { it is NftBurn } as? NftBurn,
    tokenBurn = find { it is TokenBurn } as? TokenBurn
)