package software.credible.eventclientapp.managers;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;
import software.credible.eventclientapp.model.Message;
import software.credible.eventclientapp.model.UserReference;

public class MessageManager {

    public static RealmResults<Message> findAllMessagesAsync(Realm realm) {
        return realm.where(Message.class)
                    .equalTo("team.members.userIdentity", SyncUser.currentUser().getIdentity())
                    .findAllSortedAsync("sent");
    }

    public static void addMessageAsync(final String messageText) {
        try(Realm r = Realm.getDefaultInstance()) {
            r.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    // Assume happy path, user exists, is on a team, message text is not null.
                    UserReference sender = UserManager.findUserByUserIdentity(bgRealm, SyncUser.currentUser().getIdentity());

                    Message message = new Message();
                    message.setUserReference(sender);
                    message.setMessageId(UUID.randomUUID().toString());
                    message.setSent(new Date());
                    message.setContents(messageText);

                    bgRealm.copyToRealm(message);
                }
            });
        }
    }

}
