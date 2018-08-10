package org.myspecialway.android.utils

import android.util.Base64

import com.google.gson.Gson

import org.myspecialway.android.session.jwt.TokenPayloadData

interface JWTBase64Decoder {

    fun decode(toDecode: String): String
}

class JWTParser @JvmOverloads constructor(private val decoder: JWTBase64Decoder = JWTBase64DecoderImpl()) {

    class JWTBase64DecoderImpl : JWTBase64Decoder {

        override fun decode(toDecode: String): String {
            return String(Base64.decode(toDecode.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING))
        }
    }

    fun parsePayload(token: String): TokenPayloadData {

        if (token.isBlank()) throw IllegalArgumentException("token should not be empty")

        val split = token.split("\\.".toRegex())

        val payload = decoder.decode(split[1])

        return Gson().fromJson(payload, TokenPayloadData::class.java)
    }
}
