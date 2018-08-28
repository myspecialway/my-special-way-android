package org.myspecialway.session

import org.myspecialway.session.jwt.TokenPayloadData

import java.util.Objects

data class UserData(val id: String, val username: String, val role: String, val firstname: String, val lastname: String) {

    constructor(tokenPayloadData: TokenPayloadData) : this(
            tokenPayloadData.id,
            tokenPayloadData.username,
            tokenPayloadData.role,
            tokenPayloadData.firstname,
            tokenPayloadData.lastname)

}
