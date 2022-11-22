/*
 * Constants
 * Metaplex
 * 
 * Created by Funkatronics on 9/14/2022
 */

package com.metaplex.lib.modules.candymachinesv2.models

const val MAX_NAME_LENGTH = 32
const val MAX_SYMBOL_LENGTH = 10
const val MAX_URI_LENGTH = 200
const val MAX_CREATOR_LIMIT = 5
const val MAX_CREATOR_LEN = 32 + 1 + 1
const val CONFIG_LINE_SIZE = 4 + MAX_NAME_LENGTH + 4 + MAX_URI_LENGTH
const val CONFIG_ARRAY_START =
    8 + // key
    32 + // authority
    32 + // wallet
    33 + // token mint
    4 +
    6 + // uuid
    8 + // price
    8 + // items available
    9 + // go live
    10 + // end settings
    4 +
    MAX_SYMBOL_LENGTH + // u32 len + symbol
    2 + // seller fee basis points
    4 +
    MAX_CREATOR_LIMIT * MAX_CREATOR_LEN + // optional + u32 len + actual vec
    8 + // max supply
    1 + // is mutable
    1 + // retain authority
    1 + // option for hidden setting
    4 +
    MAX_NAME_LENGTH + // name length,
    4 +
    MAX_URI_LENGTH + // uri length,
    32 + // hash
    4 + // max number of lines;
    8 + // items redeemed
    1 + // whitelist option
    1 + // whitelist mint mode
    1 + // allow presale
    9 + // discount price
    32 + // mint key for whitelist
    1 +
    32 +
    1 // gatekeeper