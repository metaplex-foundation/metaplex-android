package com.metaplex.lib.drivers.indenty

import com.solana.core.PublicKey

interface IdentityDriver {
    val publicKey: PublicKey
}