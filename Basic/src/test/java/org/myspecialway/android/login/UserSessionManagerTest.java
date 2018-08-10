package org.myspecialway.android.login;

import org.junit.Test;
import org.myspecialway.android.session.UserData;
import org.myspecialway.android.login.gateway.ILoginGateway;
import org.myspecialway.android.login.gateway.InvalidLoginCredentials;
import org.myspecialway.android.session.UserSession;
import org.myspecialway.android.session.UserSessionManager;
import org.myspecialway.android.utils.JWTBase64Decoder;
import org.myspecialway.android.utils.JWTParser;

import java.util.Base64;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class UserSessionManagerTest {

    private static final String FAKE_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjViNWViZDhjMDE0NGFjNjI4ZjlkZTllZCIsInVzZXJuYW1lIjoidGVhY2hlciIsInJvbGUiOiJURUFDSEVSIiwiZmlyc3RuYW1lIjoiTXN3IiwibGFzdG5hbWUiOiJUZWFjaGVyIiwiaWF0IjoxNTMzNzE5MDQyLCJleHAiOjE1MzM3MjYyNDJ9.42VVsZPL8DDIQGmqLwFNI8TF-RGoTvWwqXwV2gQbUUE";

    @Test
    public void login_emptyUsername_throwIllegalArgumentException() {

        UserSessionManager userSessionManager = createASuccessfulUserSessionManager();

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

        UserSessionManager userSessionManager = createASuccessfulUserSessionManager();

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
        UserSessionManager userSessionManager = new UserSessionManager(fakeGateway, new JWTParser());
        RequestCallback<UserSession> requestCallbackMock = mock(RequestCallback.class);

        userSessionManager.login("username", "password", requestCallbackMock);

        verify(requestCallbackMock, only()).onFailure(any(InvalidLoginCredentials.class));
    }

    @Test
    public void login_validUsernameOrPasswordEntered_returnUserSessionData(){

        UserData expectedUserData = new UserData("5b5ebd8c0144ac628f9de9ed", "teacher", "TEACHER", "Msw", "Teacher");
        UserSessionManager userSessionManager = createASuccessfulUserSessionManager();
        RequestCallback<UserSession> requestCallbackMock = mock(RequestCallback.class);

        userSessionManager.login("username", "password", requestCallbackMock);

        verify(requestCallbackMock, only()).onSuccess(argThat(userSession ->
                userSession.getToken().getAccessToken().equals(FAKE_TOKEN) && userSession.getUserData().equals(expectedUserData)));
    }

    @Test
    public void isLoggedIn_userNotLoggedIn_returnFalse(){

        UserSessionManager userSessionManager = createASuccessfulUserSessionManager();

        boolean loggedIn = userSessionManager.isLoggedIn();

        assertThat(loggedIn).isFalse();
    }

    @Test
    public void isLoggedIn_userLoggedIn_returnTrue(){
//        UserSessionManager userSessionManager = createASuccessfulUserSessionManager();
//        userSessionManager.login("username", "password", null);
//
//        boolean loggedIn = userSessionManager.isLoggedIn();
//
//        assertThat(loggedIn).isTrue();
    }

    private UserSessionManager createASuccessfulUserSessionManager(){
        ILoginGateway fakeGateway = (username, password, callback) -> callback.onSuccess(FAKE_TOKEN);
        JWTBase64Decoder decoder = toDecode -> new String(Base64.getUrlDecoder().decode(toDecode));
        return new UserSessionManager(fakeGateway, new JWTParser(decoder));
    }
}