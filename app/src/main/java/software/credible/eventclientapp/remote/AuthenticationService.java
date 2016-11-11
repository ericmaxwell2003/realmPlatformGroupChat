package software.credible.eventclientapp.remote;

import software.credible.eventclientapp.remote.dto.LoginDto;
import software.credible.eventclientapp.remote.dto.OAuthTokenDto;
import software.credible.eventclientapp.remote.dto.RegistrationDto;
import retrofit.http.Body;
import retrofit.http.POST;

public interface AuthenticationService {

    @POST("/register")
    RegistrationDto register(@Body RegistrationDto registrationDto);

    @POST("/login")
    OAuthTokenDto login(@Body LoginDto loginDto);

}
