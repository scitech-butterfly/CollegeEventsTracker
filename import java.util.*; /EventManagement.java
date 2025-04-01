import java.util.*;
// Event interface
interface EventManagement {
    void addEvent(Event event);
    void removeEvent(String eventId);
    ArrayList<Event> searchEventsByName(String name);
    ArrayList<Event> getEventsForMonth(int month, int year);
}
