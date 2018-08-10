package org.myspecialway.android.session;

import org.myspecialway.android.session.jwt.TokenPayloadData;

import java.util.Objects;

public class UserData {

    public final String id;
    public final String username;
    public final String role;
    public final String firstname;
    public final String lastname;

    public UserData(String id, String username, String role, String firstname, String lastname){
        this.id = id;
        this.username = username;
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public UserData(TokenPayloadData tokenPayloadData) {
        this(tokenPayloadData.getId(),
                tokenPayloadData.getUsername(),
                tokenPayloadData.getRole(),
                tokenPayloadData.getFirstname(),
                tokenPayloadData.getLastname());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof  UserData)) return false;

        UserData userData = (UserData) obj;

        return userData.id.equals(id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
