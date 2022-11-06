/*
 * NetworkDriver
 * metaplex-android
 * 
 * Created by Funkatronics on 11/6/2022
 */

package com.metaplex.lib.drivers.network

interface HttpNetworkDriver {
    suspend fun makeHttpRequest(request: HttpRequest): String
}