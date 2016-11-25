package software.credible.eventclientapp.managers;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;
import software.credible.eventclientapp.GroupChatApplication;
import software.credible.eventclientapp.model.Message;
import software.credible.eventclientapp.model.Team;
import software.credible.eventclientapp.model.UserReference;

public class DataManager {

    public static Team findCurrentTeam(Realm realm) {
        UserReference currentUserReference = findUserByUserIdentity(realm, SyncUser.currentUser().getIdentity());
        return currentUserReference.getTeam();
    }

    public static RealmResults<Message> findAllMessagesAsync(Realm realm, String teamName) {
        return realm.where(Message.class)
                    .equalTo("team.teamName", teamName)
                    .findAllSortedAsync("sent");
    }

    public static void addMessageAsync(final String messageText) {
        try(Realm r = Realm.getDefaultInstance()) {
            r.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    // Assume happy path, user exists, is on a team, message text is not null.
                    UserReference sender = DataManager.findUserByUserIdentity(bgRealm, SyncUser.currentUser().getIdentity());

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

    public static void removeAllMessagesAsync(Realm realm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm r) {
                r.delete(Message.class);
            }
        });
    }

    public static void logoutActiveUser() {
        SyncUser.currentUser().logout();
    }

    public static void setActiveUser(SyncUser user) {
        SyncConfiguration defaultConfig = new SyncConfiguration.Builder(user, GroupChatApplication.REALM_URL).build();
        Realm.setDefaultConfiguration(defaultConfig);
    }

    public static void addUserToTeam(final String userIdentity, final String teamName) {
        try (Realm realm = Realm.getDefaultInstance()) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm r) {

                    UserReference userReference = findUserByUserIdentity(r, userIdentity);

                    // Create user if doesn't already exist.
                    if (userReference == null) {
                        userReference = new UserReference();
                        userReference.setUserIdentity(userIdentity);
                        userReference = r.copyToRealmOrUpdate(userReference);
                    }

                    Team team = findTeamByName(r, teamName);

                    // Create team if doesn't already exist.
                    if (team == null) {
                        team = new Team();
//                        team.setTeamId(UUID.randomUUID().toString());
                        team.setTeamName(teamName);
                        team = r.copyToRealmOrUpdate(team);
                    }

                    linkUserToTeam(userReference, team);

                }
            });
        }
    }

    public static UserReference findUserByUserIdentity(Realm r, String userIdentity) {
        return r.where(UserReference.class)
                .equalTo("userIdentity", userIdentity)
                .findFirst();
    }

    public static void linkUserToTeam(UserReference managedUserReference, Team managedTeam) {
        managedTeam.getMembers().add(managedUserReference);
        managedUserReference.setTeam(managedTeam);
    }

    public static Team findTeamByName(Realm r, String teamName) {
        return r.where(Team.class).equalTo("teamName", teamName).findFirst();
    }


}
