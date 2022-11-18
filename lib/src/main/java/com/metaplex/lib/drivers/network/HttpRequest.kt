/*
 * HttpRequest
 * metaplex-android
 * 
 * Created by Funkatronics on 11/6/2022
 */

package com.metaplex.lib.drivers.network

enum class HttpMethod(name: String){
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PUT("PUT"),
    DELETE("DELETE"),
    TRACE("TRACE")
}

interface HttpRequest {
    val url: String
    val method: String
    val properties: Map<String, String>
    val body: String?
}

class HttpGetRequest(
    override val url: String,
    override val properties: Map<String, String>
) : HttpRequest {
    override val method = HttpMethod.GET.name
    override val body = null
}

class HttpPostRequest(
    override val url: String,
    override val properties: Map<String, String>,
    override val body: String? = null
) : HttpRequest {
    override val method = HttpMethod.POST.name
}