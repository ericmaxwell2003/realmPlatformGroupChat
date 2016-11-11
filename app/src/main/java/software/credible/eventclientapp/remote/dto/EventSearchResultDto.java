package software.credible.eventclientapp.remote.dto;

import java.util.List;

public class EventSearchResultDto {

    private String syncToken;
    private List<EventDto> events;

    public String getSyncToken() {
        return syncToken;
    }

    public void setSyncToken(String syncToken) {
        this.syncToken = syncToken;
    }

    public List<EventDto> getEvents() {
        return events;
    }

    public void setEvents(List<EventDto> events) {
        this.events = events;
    }
}
