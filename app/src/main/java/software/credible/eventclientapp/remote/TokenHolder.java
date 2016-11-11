package software.credible.eventclientapp.remote;

import javax.inject.Singleton;

import software.credible.eventclientapp.remote.dto.OAuthTokenDto;

@Singleton
public class TokenHolder {

    private OAuthTokenDto token;

    public OAuthTokenDto getToken() {
        return token;
    }

    public void setToken(OAuthTokenDto token) {
        this.token = token;
    }
}
