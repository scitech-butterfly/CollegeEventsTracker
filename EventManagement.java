//EventManagement.java
import java.util.*;
// EventManagement interface
interface EventManagement {
    void addEvent(Event event);
    void removeEvent(String eventId);
    ArrayList<Event> searchEventsByName(String name);
    ArrayList<Event> getEventsForMonth(int month, int year);
}
// Event class
class Event {
    private final String eventId;  // final variable
    private String name;
    private String description;
    private String venue;
    private String date;
    private String time;
    private String communityId;
    private boolean membersOnly;
    private ArrayList<String> attendees;

    public Event(String eventId, String name, String description, String venue,
                 String date, String time, String communityId, boolean membersOnly) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.communityId = communityId;
        this.membersOnly = membersOnly;
        this.attendees = new ArrayList<>();
    }
// Getters 
    public String getEventId() { return eventId; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getVenue() { return venue; }
    public String getCommunityId() { return communityId; }
    public boolean isMembersOnly() { return membersOnly; }

    public void addAttendee(String userId) {
        attendees.add(userId);
    }

    public ArrayList<String> getAttendees() {
        return attendees;
    }

