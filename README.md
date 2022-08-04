# Metaplex Android SDK

This SDK helps developers get started with the on-chain tools provided by Metaplex. It focuses its API on common use-cases to offer a smooth developer experience. 

⚠️ Please note that this SDK has been implemented from scratch and is currently in alpha. It means some of the core API and interfaces might change from one version to another. Feel free to contact me about bugs, improvements, and new use cases. 

Please check the [Sample App](https://github.com/metaplex-foundation/metaplex-android/tree/main/sample).

## Installation

### JitPack [![Release](https://jitpack.io/v/metaplex-foundation/metaplex-android.svg)](https://jitpack.io/#metaplex-foundation/metaplex-android)

The library is now available through [JitPack.io](https://jitpack.io/#metaplex-foundation/metaplex-android)

First, add the JitPack repository to your build:

```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

Then add the dependency to the 'build.gradle' file for your app/module:

```
dependencies {
    ...
    implementation 'com.github.metaplex-foundation:metaplex-android:{version}'
}
```

### GitHub Package

You can also add the dependency directly from GitHub. We recommend using the GitHub recommended way to load Artifacts. First, get a GitHub Token from your [account settings](https://github.com/settings/tokens).


Inside settings.gradle add a maven repository:

```
repositories {
    ...
    maven {
       name = "GitHubPackages"
       url = "https://github.com/metaplex-foundation/metaplex-android"
       credentials {
           username = "<YOUR_GITHUB_USERNAME>"
           password = "<YOUR_GITHUB_TOKENS>"
       }
    }
}
 
```

Then at your build.gradle:

```
dependencies {
    ...
    implementation 'com.metaplex:metaplex:+' // Set version
    implementation 'com.solana:solana:+' // Required
}
```

After that, perform Gradle sync.

## Requirements

- Android 27+

## Setup
The entry point to the Android SDK is a `Metaplex` instance that will give you access to its API.

Set the `SolanaConnectionDriver` and set up your environment. Provide a `StorageDriver` and `IdentityDriver`. You can also use the concrete implementations OkHttpSharedStorageDriver for OKHttp and ReadOnlyIdentityDriver for a read-only Identity Driver. 

You can customize who the SDK should interact on behalf of and which storage provider to use when uploading assets. We might provide a default and straightforward implementation in the future.

```kotlin
val ownerPublicKey = PublicKey("<Any PublicKey>")
val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
val solanaIdentityDriver = ReadOnlyIdentityDriver(ownerPublicKey, solanaConnection.solanaRPC)
val storageDriver = OkHttpSharedStorageDriver()
val metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
```

# Usage
Once properly configured, that `Metaplex` instance can be used to access modules providing different sets of features. Currently, only one NFT module can be accessed via the `nft` property. From that module, you will be able to find, create and update NFTs with more features to come.

Let's dive into the `nfts` module. 

## NFTs
The NFT module can be accessed via `Metaplex.nft` and provides the following methods. Currently, we only support read methods. Writing and creating NFTs will be supported in the future.

- [`findByMint(mint, callback)`](#findByMint)
- [`findAllByMintList(mints, callback)`](#findAllByMintList)
- [`findAllByOwner(owner, callback)`](#findAllByOwner)
- [`findAllByCreator(creator, position = 1, callback)`](#findAllByCreator)
- [`findAllByCandyMachine(candyMachine, version = 2, callback)`](#findAllByCandyMachine)

All the methods return a callback. It's also possible to wrap them inside RX, async Result, or Combine. We only provide this interface since it is the most compatible without forcing any specific framework. 

### findByMint

The `findByMint` method accepts a `mint` public key and returns the NFT object.

```kotlin
metaplex.nft.findByMint(mintPublicKey){
    it.onSuccess { 
        ...
    }.onFailure { 
        ...
    }
}
```

The returned `Nft` object will NOT contain JSON data. It will only contain on-chain data. If you need access to the JSON off-chain metadata, the call requires the metaplex object.

```kotlin
nft.metadata(metaplex) { result -> 
    it.onSuccess { 
        ...
    }.onFailure { 
        ...
    }
}
```

Similarly, the `MasterEditionAccount` account of the NFT will also be already loaded, and if it exists on that NFT, you can use it like so.

```kotlin
val masterEdition = nft.masterEditionAccount
```

Depending on the MasterEditionAccount version, it can return v1 or v2 enums. 

You can [read more about the `NFT` model below](#the-nft-model).

### findAllByMintList

The `findAllByMintList` method accepts an array of mint addresses and returns an array of `Nft`s. However, `null` values will be returned for each provided mint address that is not associated with an NFT.

```kotlin
metaplex.nft.findAllByMintList(listOf(mintPublicKey, mintPublicKey)){ result ->
    result.onSuccess { nfts ->
       val nftList = nfts.filterNotNull() // useful to remove null
       ...
    }
}
```

NFTs retrieved via `findAllByMintList` will not have their JSON metadata loaded because this would require one request per NFT. This could be inefficient if you provide a long list of mint addresses. Additionally, you might want to fetch this on-demand, as the NFTs are displayed on your web app, for instance.

Thus, if you want to load the JSON metadata of an NFT, you may do this like so.

```kotlin
nft.metadata(metaplex) { result -> 
    it.onSuccess { 
        ...
    }.onFailure { 
        ...
    }
}
```

We'll talk more about these tasks when documenting [the `NFT` model](#the-nft-model).

### findAllByOwner

The `findAllByOwner` method accepts a public key and returns all `Nft`s owned by that owner's public key.

```kotlin
metaplex.nft.findAllByOwner(ownerPublicKey){ result ->
    result.onSuccess { nfts ->
        val nftList = nfts.filterNotNull() // useful to remove null
        ...
    }.onFailure { 
        ...
    }
}
```

Similar to `findAllByMintList`, the returned `Nft`s will not have their JSON metadata. This method, along with `findByMint`, is used in the [Sample App](https://github.com/metaplex-foundation/metaplex-android/tree/main/sample).


### The `Nft` model

All the methods above either return or interact with an `Nft` object. The `Nft` object is a read-only data representation of your NFT that contains all the information you need at the top level.

You can see [its full data representation by checking the code](https://github.com/metaplex-foundation/metaplex-android/blob/main/lib/src/main/java/com/metaplex/lib/modules/nfts/models/NFT.kt) but here is an overview of the properties that are available on the `Nft` object.

```kotlin
class NFT(
    val metadataAccount: MetadataAccount,
    val masterEditionAccount: MasterEditionAccount?
) {

    val updateAuthority: PublicKey = metadataAccount.update_authority
    val mint: PublicKey = metadataAccount.mint
    val name: String = metadataAccount.data.name
    val symbol: String = metadataAccount.data.symbol
    val uri: String = metadataAccount.data.uri
    val sellerFeeBasisPoints: Int = metadataAccount.data.sellerFeeBasisPoints
    val creators: Array<MetaplexCreator> = metadataAccount.data.creators
    val primarySaleHappened: Boolean = metadataAccount.primarySaleHappened
    val isMutable: Boolean = metadataAccount.isMutable
    val editionNonce: Int? = metadataAccount.editionNonce
    val tokenStandard: MetaplexTokenStandard? = metadataAccount.tokenStandard
    val collection: MetaplexCollection? = metadataAccount.collection
    ...
}
```

As you can see, some of the properties are loaded on demand. It is because they are not always needed, which can be expensive to load.

To load these properties, you may run the `metadata` properties of the `Nft` object.

```kotlin
nft.metadata(metaplex: self.metaplex) { result in
    switch result {
    case .success(let metadata):
        ...
    case .failure:
        ...
    }
}
```

## Identity
The current identity of a `Metaplex` instance can be accessed via `metaplex.identity()` and provide information on the wallet we act on behalf of when interacting with the SDK.

This method returns an identity object with the following interface. All the methods require a Solana API instance.

```ts
public protocol IdentityDriver {
    var publicKey: PublicKey { get }
    func sendTransaction(serializedTransaction: String, onComplete: @escaping(Result<TransactionID, IdentityDriverError>) -> Void)
    func signTransaction(transaction: Transaction, onComplete: @escaping (Result<Transaction, IdentityDriverError>) -> Void)
    func signAllTransactions(transactions: [Transaction], onComplete: @escaping (Result<[Transaction?], IdentityDriverError>) -> Void)
}
```

The implementation of these methods depends on the concrete identity driver being used. For example, use a KeypairIdentity or a Guest(no public key added)

Let’s quickly look at the concrete identity drivers available to us.

### GuestIdentityDriver

The `GuestIdentityDriver` driver is the simplest identity driver. It is essentially a `null` driver that can be useful when we don’t need to send any signed transactions. It will return failure if you use `signTransaction` methods.


### KeypairIdentityDriver

The `KeypairIdentityDriver` driver accepts an `Account` object as a parameter.


### ReadOnlyIdentityDriver

The `KeypairIdentityDriver` driver accepts a `PublicKey` object as a parameter. It's a read-only similar to the GuestIdentity, but it has a provided `PublicKey`. It will return failure if you use `signTransaction` methods.

## Storage

You may access the current storage driver using `metaplex.storage()`, which will give you access to the following interface.

```swift
public protocol StorageDriver {
    func download(url: URL, onComplete: @escaping(Result<NetworkingResponse, StorageDriverError>) -> Void)
}
```

Currently, it's only used to retrieve JSON data off-chain. 

### OkHttpSharedStorageDriver

This will use OkHttp networking, the most popular Android networking implementation library. It might be the most useful implementation.

### MemoryStorageDriver

This will use the returned Empty Data object with size 0.

## Next steps
As mentioned above, this SDK is still in very early stages. We plan to add a lot more features to it. Here’s a quick overview of what we plan to work on next.
- New features in the NFT module.
- Upload, Create NFTs to match JS-Next SDK.
- More documentation, tutorials, starter kits, etc.

## Acknowledgment

The SDK is heavily inspired by the [JS-Next](https://github.com/metaplex-foundation/js-next) SDK. The objective of this is to have one Metaplex-wide interface for all NFTs. If you use the JS-Next SDK, this SDK should be familiar.
