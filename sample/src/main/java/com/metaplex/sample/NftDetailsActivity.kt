package com.metaplex.sample

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.google.android.flexbox.*
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.modules.nfts.models.JsonMetadataAttribute
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.models.Value
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCreator
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.core.PublicKey
import com.solana.networking.Network
import com.solana.networking.RPCEndpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL


class NftDetailsActivity : AppCompatActivity() {
    private lateinit var metaplex: Metaplex
    private lateinit var ownerPublicKey : PublicKey
    private var index : Int = -1

    private lateinit var nftImageBackground : ImageView
    private lateinit var nftImage : ImageView
    private lateinit var nftName : TextView
    private lateinit var nftDescription : TextView

    private lateinit var recyclerView: RecyclerView

    companion object {
        const val NFT_NAME = "nftName"
        const val NFT_OWNER = "nftOwner"
        const val MINT_ACCOUNT = "mintAccount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nft_details)

        nftImageBackground = findViewById(R.id.nftImageBackground)
        nftImage = findViewById(R.id.nftImage)
        nftName = findViewById(R.id.nftName)
        nftDescription = findViewById(R.id.nftDescription)

        val nftName = intent?.extras?.getString(NFT_NAME).toString()
        val nftOwner = intent?.extras?.getString(NFT_OWNER).toString()
        val mintAccount = intent?.extras?.getString(MINT_ACCOUNT).toString()

        ownerPublicKey = PublicKey(nftOwner)
        setTitle(nftName)

        val solanaConnection = SolanaConnectionDriver(
            RPCEndpoint.custom(URL("https://api.metaplex.solana.com"), URL("https://api.metaplex.solana.com"), Network.mainnetBeta)
        )
        val solanaIdentityDriver = ReadOnlyIdentityDriver(ownerPublicKey, solanaConnection)
        val storageDriver = OkHttpSharedStorageDriver()

        metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)

        CoroutineScope(Dispatchers.IO).launch {
            metaplex.nft.findByMint(PublicKey(mintAccount))
                .onSuccess { nft ->
                    fetchOffChainMetadata(this@NftDetailsActivity, nft)
                }
        }
    }

    private fun fetchOffChainMetadata(context : Context, nft : NFT) {
        nft.metadata(metaplex) { result ->
            result.onSuccess {
                Handler(Looper.getMainLooper()).post(Runnable {
                    nftName.text = nft.name
                    nftDescription.text = it.description
                    nftImage.setImageResource(0)
                    nftImageBackground.setImageResource(0)

                    val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                    showGlide(context, factory, it.image, nftImage)
                    showGlide(context, factory, it.image, nftImageBackground)

                    recyclerView = findViewById(R.id.nftAttributesRecyclerView)
                    val layoutManager = FlexboxLayoutManager(context)
                    layoutManager.flexDirection = FlexDirection.ROW
                    layoutManager.justifyContent = JustifyContent.SPACE_BETWEEN
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = NftAttributesRecyclerViewAdapter(it.attributes!!.toTypedArray())

                    val hasCreators = nft.metadataAccount.data.creators?.isNotEmpty() == true
                    val creators = (nft.metadataAccount.data.creators ?: arrayOf()).also {
                        it.sortByDescending { it.share }
                    }
                    makeCreatorsViews(findViewById(R.id.nftCreators), hasCreators, creators)

                    val salesStatus = if (hasCreators) "Secondary" else "Primary"
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
                if (creator.share.toInt() != 0) {
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

    private fun showGlide(context: Context, factory:  DrawableCrossFadeFactory, image: String?, imageView: ImageView) {
        Glide
            .with(context)
            .load(image)
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .centerCrop()
            .apply(RequestOptions().transform(RoundedCorners(18)).skipMemoryCache(true))
            .into(imageView)
    }

    private fun dpTopx(dp: Int): Int {
        return (dp * applicationContext.resources.displayMetrics.density).toInt()
    }

}

class NftAttributesRecyclerViewAdapter(private val dataSet: Array<JsonMetadataAttribute>) :
    RecyclerView.Adapter<NftAttributesRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nftTraitType: TextView
        val nftTraitValue: TextView

        init {
            nftTraitType = view.findViewById(R.id.nftTraitType)
            nftTraitValue = view.findViewById(R.id.nftTraitValue)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.nft_attributes_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.nftTraitType.text = dataSet[position].trait_type!!.uppercase()
        viewHolder.nftTraitValue.text = getNftTraitValue(dataSet[position].value)
    }

    override fun getItemCount() = dataSet.size

    private fun getNftTraitValue(traitObject : Value?) = when(traitObject) {
        is Value.number -> traitObject.value.toString()
        is Value.string -> traitObject.value
        Value.unkown -> "None"
        else -> "None"
    }
}