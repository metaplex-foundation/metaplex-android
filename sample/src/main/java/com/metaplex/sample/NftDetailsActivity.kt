package com.metaplex.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.metaplex.lib.modules.nfts.models.NFT

class NftDetailsActivity : AppCompatActivity() {
    private lateinit var mintAccountTextView : TextView

    companion object {
        const val MINT_ACCOUNT = "mintAccount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nft_details)

        mintAccountTextView = findViewById(R.id.mintAccountTextView)

        val mintAccount = intent?.extras?.getString(MINT_ACCOUNT).toString()

        mintAccountTextView.text = getString(R.string.mint_account, mintAccount)
    }
}