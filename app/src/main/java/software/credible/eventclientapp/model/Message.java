package software.credible.eventclientapp.model;

import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Message extends RealmObject {

    @PrimaryKey
    @Required
    private String messageId;

    @Required
    private Date sent;

    @Required
    private String contents;
    private Team team;
    private UserReference userReference;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Team getTeam() {
        return team;
    }

    public UserReference getUserReference() {
        return userReference;
    }

    public void setUserReference(UserReference userReference) {
        this.userReference = userReference;
        this.team = userReference.getTeam();
    }
}
