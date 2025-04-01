import java.util.*;
// Abstract Community class
abstract class Community {
    protected final String communityId;  // final variable
    protected String name;
    protected ArrayList<Event> events;
    protected ArrayList<String> memberIds;

    public Community(String communityId, String name) {
        this.communityId = communityId;
        this.name = name;
        this.events = new ArrayList<>();
        this.memberIds = new ArrayList<>();
    }
    // Abstract method
    public abstract String getCommunityType();
    
    public void addMember(String userId) {
        memberIds.add(userId);
    }
    public boolean isMember(String userId) {
        return memberIds.contains(userId);
    }
    public void addEvent(Event event) {
        events.add(event);
    }
  
