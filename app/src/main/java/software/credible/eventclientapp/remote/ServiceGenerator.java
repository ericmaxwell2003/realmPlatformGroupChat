package software.credible.eventclientapp.remote;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Inject;
import javax.inject.Provider;

import software.credible.eventclientapp.BuildConfig;
import software.credible.eventclientapp.remote.dto.OAuthTokenDto;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class ServiceGenerator {

    private static final String API_BASE_URL = "https://192.168.57.1:8443";

    public static <S> S createService(Class<S> serviceClass, final TokenHolder tokenHolder) {

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(API_BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL);

        if(BuildConfig.DEBUG) {
            builder.setClient(new OkClient(new LocalOkHttpClientProvider().get()));
        } else {
            builder.setClient(new OkClient(new OkHttpClient()));
        }

        boolean shouldAddAuthHeader = !serviceClass.isAssignableFrom(AuthenticationService.class) && tokenHolder != null && tokenHolder.getToken() != null;
        if(shouldAddAuthHeader) {
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Authorization", String.format("Bearer %s", tokenHolder.getToken().getAccessToken()));
                }
            });
        }
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }

}
