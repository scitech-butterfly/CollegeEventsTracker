import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class EventUpdater {
    private final EventManagement eventManagement;
    private final Authentication authentication;

    public EventUpdater() {
        this.eventManagement = new EventManagement();
        this.authentication = new Authentication();
    }

    /**
     * Updates the time and venue of an event
     *
     * @param eventId     The ID of the event to update
     * @param userId      The ID of the user attempting to update the event
     * @param newDate     The new date for the event
     * @param newTime     The new time for the event
     * @param newVenue    The new venue for the event
     * @return            True if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateEventTimeAndVenue(int eventId, int userId, Date newDate, Time newTime, String newVenue)
            throws SQLException {

        // First check if user is an event organizer
        if (!eventManagement.isEventOrganizer(userId)) {
            System.out.println("Access denied: Only event organizers can update events.");
            return false;
        }

        // Attempt to update the event
        boolean updated = eventManagement.updateEventTimeAndVenue(eventId, userId, newDate, newTime, newVenue);

        if (updated) {
            // Get updated event details to display confirmation
            Event updatedEvent = eventManagement.getEventById(eventId);
            if (updatedEvent != null) {
                System.out.println("Event successfully updated:");
                System.out.println("Name: " + updatedEvent.getName());
                System.out.println("New Date: " + updatedEvent.getDate());
                System.out.println("New Time: " + updatedEvent.getTime());
                System.out.println("New Venue: " + updatedEvent.getVenue());
            }
        }

        return updated;
    }

    /**
     * Example usage method showing how to update an event
     */
    public static void exampleUsage() {
        try {
            EventUpdater updater = new EventUpdater();

            // Example values
            int eventId = 1;              // The event to update
            int organizerId = 5;          // The ID of an event organizer
            Date newDate = Date.valueOf("2025-05-15");  // New date
            Time newTime = Time.valueOf("18:30:00");    // New time (6:30 PM)
            String newVenue = "Main Auditorium";        // New venue

            boolean success = updater.updateEventTimeAndVenue(eventId, organizerId, newDate, newTime, newVenue);

            if (!success) {
                System.out.println("Event update failed. Please check your permissions and event details.");
            }

        } catch (SQLException e) {
            System.out.println("Database error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}