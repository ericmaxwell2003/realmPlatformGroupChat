package software.credible.eventclientapp.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Team extends RealmObject {

//    @Required
//    private String teamId;

    @PrimaryKey
    @Required
    private  String teamName;

    private RealmList<UserReference> members;

//    public String getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(String teamId) {
//        this.teamId = teamId;
//    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<UserReference> getMembers() {
        return members;
    }

}
