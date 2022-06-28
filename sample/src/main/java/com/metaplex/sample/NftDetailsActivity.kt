package com.metaplex.sample

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCreator
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint

class NftDetailsActivity : AppCompatActivity() {
    private lateinit var metaplex: Metaplex
    private val ownerPublicKey = PublicKey("CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7")
    private var index : Int = -1

    companion object {
        const val MINT_ACCOUNT = "mintAccount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nft_metadata_cards)

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
                Handler(Looper.getMainLooper()).post(Runnable {
                    val hasCreators = nft.metadataAccount.data.hasCreators
                    val creators = nft.metadataAccount.data.creators
                    creators.sortByDescending { it.share }
                    makeCreatorsViews(findViewById(R.id.nftCreators), hasCreators, creators)

                    val salesStatus = if (nft.metadataAccount.data.hasCreators) "Secondary" else "Primary"
                    val royaltyFeeBasis = (nft.metadataAccount.data.sellerFeeBasisPoints.toDouble() / 100).toString()
                    val metadataStatus = if (nft.metadataAccount.isMutable) "Mutable" else "Immutable"
                    val nftOverviewProps = arrayOf<String>(salesStatus, royaltyFeeBasis.plus("%"), metadataStatus)
                    updateAllTextViews(findViewById(R.id.nftOverview), nftOverviewProps)

                    index = -1

                    val nftSummaryProps = arrayOf<String>(nft.metadataAccount.update_authority.toBase58(), nft.metadataAccount.mint.toBase58(), ownerPublicKey.toBase58())
                    updateAllTextViews(findViewById(R.id.nftSummary), nftSummaryProps)
                })
            }
        }
    }


    private fun updateAllTextViews(view: View, nftProps: Array<String>) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                updateAllTextViews(view.getChildAt(i), nftProps)
            }
        } else if (view is TextView) {
            if (view.parent is RelativeLayout) {
                view.text = nftProps[++index]
            }
        }
    }

    private fun makeCreatorsViews(view: ViewGroup, hasCreators: Boolean, creators: Array<MetaplexCreator>) {
        if (hasCreators) {
            for (creator in creators) {
                if (creator.share != 0) {
                    val creatorAddress = creator.address.toBase58()
                    val creatorRoyaltySplit = creator.share.toString().plus("%")

                    val linearLayout = LinearLayout(this)
                    linearLayout.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    linearLayout.setPadding(0, dpTopx(10),0, dpTopx(10))

                    val creatorAddressTextView = TextView(this)
                    creatorAddressTextView.layoutParams = LinearLayout.LayoutParams(
                        dpTopx(80),
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    creatorAddressTextView.setText(creatorAddress)
                    creatorAddressTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                    creatorAddressTextView.setTextColor(Color.parseColor("#FFFFFF"))
                    creatorAddressTextView.setMaxLines(1)
                    creatorAddressTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE)

                    linearLayout.addView(creatorAddressTextView)

                    val relativeLayout = RelativeLayout(this)
                    relativeLayout.layoutParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    val creatorRoyaltySplitTextView = TextView(this)
                    val lp = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    lp.addRule(RelativeLayout.ALIGN_PARENT_END)
                    creatorRoyaltySplitTextView.setLayoutParams(lp)
                    creatorRoyaltySplitTextView.setText(creatorRoyaltySplit)
                    creatorRoyaltySplitTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                    creatorRoyaltySplitTextView.setTextColor(Color.parseColor("#FFFFFF"))

                    relativeLayout.addView(creatorRoyaltySplitTextView)
                    linearLayout.addView((relativeLayout))
                    view.addView(linearLayout)
                }
            }
        } else {
            val nocreatorsTextView = TextView(this)
            nocreatorsTextView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            nocreatorsTextView.text = getString(R.string.no_creators_info)
            nocreatorsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            nocreatorsTextView.setTextColor(Color.parseColor("#FFFFFF"))

            view.addView(nocreatorsTextView)
        }
    }

    //Function to convert dp to pixels.
    private fun dpTopx(dp: Int): Int {
        return (dp * applicationContext.resources.displayMetrics.density).toInt()
    }

}