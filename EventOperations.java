import java.util.ArrayList;

// Interface for event operations
interface EventOperations {
 ArrayList<Event> searchEventsByCommunity(String communityName);

 Event searchEventByName(String eventName);

 boolean rsvpForEvent(String eventId, String userId, boolean attending);
}