package com.metaplex.lib

// it might make sense to move this to a shared test director later
typealias AccountKeyMnemonicPair = Pair<List<String>, String>
val AccountKeyMnemonicPair.mnemonic get() = first
val AccountKeyMnemonicPair.publicKey get() = second

object SolanaTestData {

    //region TEST DATA
    // We could eventually move this data to a json file so
    // this test account data could be easily swapped out
    val TEST_ACCOUNT_PUBLICKEY_STRING = "Geh5Ss5knQGym81toYGXDbH3MFU2JCMK7E4QyeBHor1b"

    val TEST_ACCOUNT_MNEMONIC_PAIR = AccountKeyMnemonicPair(listOf(
        "across", "start", "ancient", "solid",
        "bid", "sentence", "visit", "old",
        "have", "hobby", "magic", "bomb",
        "boring", "grunt", "rule", "extra",
        "place", "strong", "myth", "episode",
        "dinner", "thrive", "wave", "decide"
    ), "FJyTK5ggCyWaZoJoQ9YAeRokNZtHbN4UwzeSWa2HxNyy")

    // TODO: We need a way to generate test models so we can easily inject test data
//    val TEST_DATA_NFT_1 = mapOf(
//        "name" to "DeGod #5116",
//        "mintKey" to "7A1R6HLKVEnu74kCM7qb59TpmJGyUX8f5t7p3FCrFTVR",
//        "collectionKey" to "6XxjKYFbcndh2gDcsUrmZgVEsoDxXMnfsaGY6fpTJzNr",
//        "editionNonce" to 254,
//        "creators" to listOf(
//            mapOf("address" to "9MynErYQ5Qi6obp4YwwdoDmXkZ1hYVtPUqYmJJ3rZ9Kn")
//        )
//    )
//
//    val TEST_DATA_NFT_2 = mapOf(
//        "name" to "Degen Ape #6125",
//        "mintKey" to "GU6wXYYyCiXA2KMBd1e1eXttpK1mzjRPjK6rA2QD1fN2",
//        "collectionKey" to "DSwfRF1jhhu6HpSuzaig1G19kzP73PfLZBPLofkw6fLD",
//        "editionNonce" to 255,
//        "creators" to listOf(
//            mapOf("address" to "9BKWqDHfHZh9j39xakYVMdr6hXmCLHH5VfCpeq2idU9L")
//        )
//    )
    //endregion
}

