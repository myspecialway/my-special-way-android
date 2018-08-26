package org.myspecialway.ui.login;

import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.Mockito;
import org.myspecialway.ui.login.gateway.ILoginGateway;
import org.myspecialway.ui.login.gateway.InvalidLoginCredentials;
import org.myspecialway.session.UserData;
import org.myspecialway.session.UserSession;
import org.myspecialway.session.UserSessionManager;
import org.myspecialway.session.jwt.TokenHeaderData;
import org.myspecialway.session.jwt.TokenPayloadData;
import org.myspecialway.utils.JWTBase64;
import org.myspecialway.utils.JWTParser;

import java.util.Base64;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserSessionManagerTest {

    private static final long TWO_HOURS_SEC = 7200;

    private static JWTParser jwtParser = new JWTParser(new JWTBase64() {
        @NotNull
        @Override
        public String decode(@NotNull String toDecode) {
            return new String(Base64.getUrlDecoder().decode(toDecode));
        }

        @NotNull
        @Override
        public String encode(@NotNull String toEncode) {
            return Base64.getUrlEncoder().encodeToString(toEncode.getBytes());
        }
    });

    private String createFakeToken(boolean isExpired){
        long nowSec = System.currentTimeMillis() / 1000;

        TokenHeaderData fakeTokenHeader = new TokenHeaderData("HS256", "JWT");
        TokenPayloadData fakeTokenPayload = new TokenPayloadData("id",
                "username",
                "role",
                "firstname",
                "lastname",
                isExpired ? nowSec - TWO_HOURS_SEC : nowSec,
                isExpired ? nowSec : nowSec + TWO_HOURS_SEC);

        return jwtParser.createJWTToken(fakeTokenHeader, fakeTokenPayload);
    }

    @Test
    public void login_emptyUsername_throwIllegalArgumentException() {

        UserSessionManager userSessionManager = createASuccessfulUserSessionManager(createFakeToken(false));

        try {
            userSessionManager.login("", "password", new RequestCallback<UserSession>() {
                @Override
                public void onSuccess(UserSession result) {

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
            fail("No exception was thrown");
        }
        catch (Exception e){
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    public void login_emptyPassword_throwIllegalArgumentException() {

        UserSessionManager userSessionManager = createASuccessfulUserSessionManager(createFakeToken(false));

        try {
            userSessionManager.login("username", "", new RequestCallback<UserSession>() {
                @Override
                public void onSuccess(UserSession result) {

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
            fail("No exception was thrown");
        }
        catch (Exception e){
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    public void login_invalidUsernameOrPasswordEntered_returnErrorResponse(){

        ILoginGateway fakeGateway = (username, password, callback) -> callback.onFailure(new InvalidLoginCredentials());
        UserSessionManager userSessionManager = new UserSessionManager(fakeGateway, new JWTParser(), getSharedPreferencesMock());
        RequestCallback<UserSession> requestCallbackMock = mock(RequestCallback.class);

        userSessionManager.login("username", "password", requestCallbackMock);

        verify(requestCallbackMock, only()).onFailure(any(InvalidLoginCredentials.class));
    }

    @Test
    public void login_validUsernameOrPasswordEntered_returnUserSessionData(){

        String fakeToken = createFakeToken(false);
        TokenPayloadData tokenPayloadData = jwtParser.parsePayload(fakeToken);
        UserData expectedUserData = new UserData(tokenPayloadData.getId(), tokenPayloadData.getUsername(), tokenPayloadData.getRole(), tokenPayloadData.getFirstname(), tokenPayloadData.getLastname());
        UserSessionManager userSessionManager = createASuccessfulUserSessionManager(fakeToken);
        RequestCallback<UserSession> requestCallbackMock = mock(RequestCallback.class);

        userSessionManager.login("username", "password", requestCallbackMock);

        verify(requestCallbackMock, only()).onSuccess(argThat(userSession ->
                userSession.getToken().getAccessToken().equals(fakeToken) && userSession.getUserData().equals(expectedUserData)));
    }

    @Test
    public void isLoggedIn_userNeverLoggedIn_returnFalse(){

        UserSessionManager userSessionManager = createASuccessfulUserSessionManager(createFakeToken(false));

        assertThat(userSessionManager.isLoggedIn()).isFalse();
    }

    @Test
    public void isLoggedIn_userLoggedIn_returnTrue(){
        UserSessionManager userSessionManager = createASuccessfulUserSessionManager(createFakeToken(false));
        userSessionManager.login("username", "password", new RequestCallback<UserSession>() {
            @Override
            public void onSuccess(UserSession result) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        assertThat(userSessionManager.isLoggedIn()).isTrue();
    }

    @Test
    public void isLoggedIn_userLoggedInAndTokenExpired_returnFalse(){

        UserSessionManager userSessionManager = createASuccessfulUserSessionManager(createFakeToken(true));
        userSessionManager.login("username", "password", new RequestCallback<UserSession>() {
            @Override
            public void onSuccess(UserSession result) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        assertThat(userSessionManager.isLoggedIn()).isFalse();
    }

    @Test
    public void refreshSessionIfNeeded_userNeverLoggedIn_onFailureWithInvalidCredentialsException(){
        UserSessionManager userSessionManager = createASuccessfulUserSessionManager(createFakeToken(false));
        RequestCallback requestCallbackMock = mock(RequestCallback.class);

        userSessionManager.refreshSessionIfNeeded(requestCallbackMock);

        verify(requestCallbackMock, only()).onFailure(any(InvalidLoginCredentials.class));
    }

//    @Test
//    public void refreshSessionIfNeeded_userAlreadyLoggedIn_onSuccessWithSameUserSession(){
//
//    }

    private UserSessionManager createASuccessfulUserSessionManager(String token){
        ILoginGateway fakeGateway = (username, password, callback) -> callback.onSuccess(token);
        return new UserSessionManager(fakeGateway, jwtParser, getSharedPreferencesMock());
    }

    private SharedPreferences getSharedPreferencesMock(){
        SharedPreferences sharedPreferencesMock = Mockito.mock(SharedPreferences.class);
        when(sharedPreferencesMock.edit()).thenReturn(mock(SharedPreferences.Editor.class));
        when(sharedPreferencesMock.getString(anyString(), anyString())).thenReturn("");
        return sharedPreferencesMock;
    }
}