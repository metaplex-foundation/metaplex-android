package com.metaplex.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint

class NftDetailsActivity : AppCompatActivity() {
    private lateinit var metaplex: Metaplex
    private val ownerPublicKey = PublicKey("CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7")

    private lateinit var mintAccountTextView : TextView

    companion object {
        const val MINT_ACCOUNT = "mintAccount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nft_details)

        mintAccountTextView = findViewById(R.id.mintAccountTextView)

        val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        val solanaIdentityDriver = ReadOnlyIdentityDriver(ownerPublicKey, solanaConnection.solanaRPC)
        val storageDriver = OkHttpSharedStorageDriver()

        metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)

        val mintAccount = intent?.extras?.getString(MINT_ACCOUNT).toString()

        metaplex.nft.findNftByMint(PublicKey(mintAccount)) { result ->
            result.onSuccess { nft ->
                fetchOffChainMetadata(nft)
            }
        }
    }

    private fun fetchOffChainMetadata(nft : NFT) {
        nft.metadata(metaplex) { result ->
            result.onSuccess {
                // Don't Use this change of thread hack. This is a over simplify example.
                Handler(Looper.getMainLooper()).post(Runnable {
                    mintAccountTextView.text = it.description
                })
            }
        }
    }
}