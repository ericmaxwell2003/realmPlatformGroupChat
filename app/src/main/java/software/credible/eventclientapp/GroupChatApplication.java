package software.credible.eventclientapp;


import android.app.Application;
import android.util.Log;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncManager;
import io.realm.SyncSession;
import io.realm.log.AndroidLogger;
import io.realm.log.RealmLog;

public class GroupChatApplication extends Application {

    public static final String AUTH_URL = "http://" + BuildConfig.OBJECT_SERVER_IP + ":9080/auth";
    public static final String REALM_URL = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/common4/chat";

    public GroupChatApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmLog.setLevel(Log.VERBOSE);
        SyncManager.setDefaultSessionErrorHandler(new SyncSession.ErrorHandler() {
            @Override
            public void onError(SyncSession session, ObjectServerError error) {
                Log.e(GroupChatApplication.class.getName(), session.toString());
                Log.e(GroupChatApplication.class.getName(), error.getMessage());
            }
        });

    }

}
