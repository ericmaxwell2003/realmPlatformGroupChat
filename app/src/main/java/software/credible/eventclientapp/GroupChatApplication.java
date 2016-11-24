package software.credible.eventclientapp;


import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.log.AndroidLogger;
import io.realm.log.RealmLog;

public class GroupChatApplication extends Application {

    public static final String AUTH_URL = "http://" + BuildConfig.OBJECT_SERVER_IP + ":9080/auth";
    public static final String REALM_URL = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/c062404d538e5b04ac113651f4ac80a1/chat";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmLog.add(new AndroidLogger(Log.VERBOSE));
    }

}
