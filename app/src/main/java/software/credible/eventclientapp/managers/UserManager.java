package software.credible.eventclientapp.managers;

import java.util.UUID;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;
import software.credible.eventclientapp.GroupChatApplication;
import software.credible.eventclientapp.model.Team;
import software.credible.eventclientapp.model.UserReference;

public class UserManager {

    public static void logoutActiveUser() {
        SyncUser.currentUser().logout();
    }

    public static void setActiveUser(SyncUser user) {
        SyncConfiguration defaultConfig = new SyncConfiguration.Builder(user, GroupChatApplication.REALM_URL).build();
        Realm.setDefaultConfiguration(defaultConfig);
    }

    public static void addUserToTeam(final String userIdentity, final String teamName) {
        try (Realm r = Realm.getDefaultInstance()) {

            r.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    UserReference userReference = findUserByUserIdentity(r, userIdentity);

                    // Create user if doesn't already exist.
                    if (userReference == null) {
                        userReference = new UserReference();
                        userReference.setUserIdentity(userIdentity);
                        userReference = r.copyToRealm(userReference);
                    }

                    Team team = findTeamByName(r, teamName);

                    // Create team if doesn't already exist.
                    if (team == null) {
                        team = new Team();
                        team.setTeamId(UUID.randomUUID().toString());
                        team.setTeamName(teamName);
                        team = r.copyToRealm(team);
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

    private static void linkUserToTeam(UserReference managedUserReference, Team managedTeam) {
        managedTeam.getMembers().add(managedUserReference);
        managedUserReference.setTeam(managedTeam);
    }

    private static Team findTeamByName(Realm r, String teamName) {
        return r.where(Team.class).equalTo("teamName", teamName).findFirst();
    }


}
