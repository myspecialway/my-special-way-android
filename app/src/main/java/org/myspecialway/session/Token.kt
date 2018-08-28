package org.myspecialway.session

data class Token(var accessToken: String, val issuedTimeSec: Long, val expirationTimeSec: Long) {

    val isValid: Boolean
        get() {
            val now = System.currentTimeMillis()
            return accessToken.isNotBlank() && now - (issuedTimeSec*1000) > 0 && (expirationTimeSec*1000) - now > 0
        }
}
