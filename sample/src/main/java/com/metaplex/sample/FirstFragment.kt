package com.metaplex.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.metaplex.sample.databinding.FragmentFirstBinding
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    private lateinit var metaplex: Metaplex

    private val ownerPublicKey = PublicKey("CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finish()
        }

        val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        val solanaIdentityDriver = ReadOnlyIdentityDriver(ownerPublicKey, solanaConnection.solanaRPC)
        val storageDriver = OkHttpSharedStorageDriver()
        
        metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
        metaplex.nft.findNftsByOwner(ownerPublicKey){ result ->
            result.onSuccess { nfts ->
                val nftList = nfts.filterNotNull()
                val adapter = NFTRecycleViewAdapter(requireContext(), metaplex, nftList.toTypedArray())
                requireActivity().runOnUiThread {
                    binding.nftsRecyclerView.layoutManager = GridLayoutManager(context, 2)
                    binding.nftsRecyclerView.adapter = adapter
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setIcon(R.drawable.ic_metaplex_logo_mark)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class NFTRecycleViewAdapter(private val context: Context, private val metaplex: Metaplex, private val dataSet: Array<NFT>) :
    RecyclerView.Adapter<NFTRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView
        val mintTextView: TextView
        val nftImageView: ImageView

        init {
            nameTextView = view.findViewById(R.id.nameTextView)
            mintTextView = view.findViewById(R.id.mintTextView)
            nftImageView = view.findViewById(R.id.nftImageView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.nft_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.nameTextView.text = dataSet[position].metadataAccount.data.name
        viewHolder.mintTextView.text = dataSet[position].metadataAccount.mint.toBase58()
        viewHolder.nftImageView.tag = position
        viewHolder.nftImageView.setImageResource(0)

        viewHolder.itemView.setOnClickListener {
            val context = viewHolder.itemView.context

            val intent = Intent(context, NftDetailsActivity::class.java)
            val extras = Bundle()
            extras.putString(NftDetailsActivity.NFT_NAME, dataSet[position].metadataAccount.data.name)
            extras.putString(NftDetailsActivity.MINT_ACCOUNT, dataSet[position].metadataAccount.mint.toBase58())
            intent.putExtras(extras)
            context.startActivity(intent)
        }

        dataSet[position].metadata(metaplex) { result ->
            result.onSuccess {
                if(viewHolder.nftImageView.tag == position) {
                    // Don't Use this change of thread hack. This is a over simplify example.
                    Handler(Looper.getMainLooper()).post(Runnable {
                        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                        Glide
                            .with(context)
                            .load(it.image)
                            .transition(withCrossFade(factory))
                            .centerCrop()
                            .apply(RequestOptions().transform(RoundedCorners(16)).skipMemoryCache(true))
                            .into(viewHolder.nftImageView)
                    })
                }
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
