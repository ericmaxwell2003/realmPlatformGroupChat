package software.credible.eventclientapp;


import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.log.AndroidLogger;
import io.realm.log.RealmLog;

public class GroupChatApplication extends Application {

    public static final String AUTH_URL = "http://" + BuildConfig.OBJECT_SERVER_IP + ":9080/auth";
    public static final String REALM_URL = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/79e0ee5df7260dd51ba8424c7a8ab23f/chat";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmLog.add(new AndroidLogger(Log.VERBOSE));
    }

}
