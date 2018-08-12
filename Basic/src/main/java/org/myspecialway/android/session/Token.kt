package org.myspecialway.android.session

data class Token(val accessToken: String, val issuedTimeSec: Long, val expirationTimeSec: Long) {

    val isValid: Boolean
        get() {
            val now = System.currentTimeMillis()
            return accessToken.isNotBlank() && now - (issuedTimeSec*1000) > 0 && (expirationTimeSec*1000) - now > 0
        }
}
