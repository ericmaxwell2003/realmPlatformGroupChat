package software.credible.eventclientapp;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;

import software.credible.eventclientapp.remote.AuthenticationService;
import software.credible.eventclientapp.remote.EventService;
import software.credible.eventclientapp.remote.ServiceGenerator;
import software.credible.eventclientapp.remote.TokenHolder;

public class EventModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    public EventService eventServiceProvider(TokenHolder tokenHolder) {
        try {
            return ServiceGenerator.createService(EventService.class, tokenHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Provides
    public AuthenticationService authenticationServiceProvider() {
        try {
            return ServiceGenerator.createService(AuthenticationService.class, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
