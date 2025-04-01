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
  
