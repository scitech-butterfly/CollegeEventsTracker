import db.Database;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class EventManagement {
  // Method to search event by community name
  public List<Event> getEventsByCommunity(String communityName) throws SQLException {
        List<Event> events = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        Date sqlCurrentDate = Date.valueOf(currentDate);
        String sql = "SELECT * FROM events WHERE community_nid = (SELECT id FROM communities WHERE name = ?) AND event_date >= ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, communityName);
            stmt.setDate(2, sqlCurrentDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("venue"),
                        rs.getDate("event_date"),
                        rs.getTime("event_time"),
                        rs.getInt("community_nid"),
                        rs.getBoolean("members_only")
                );
                events.add(event);
            }
        }

        if (events.isEmpty()) {
            System.out.println("No upcoming events!");
        }

        return events;
    }

  // Method to search event by name
  public Event getEventByName(String eventName) throws SQLException {
        String sql = "SELECT * FROM events WHERE name = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("venue"),
                        rs.getDate("event_date"),
                        rs.getTime("event_time"),
                        rs.getInt("community_nid"),
                        rs.getBoolean("members_only"));
            }
        }
        return null;
    }
  

  // Method to RSVP for an event
  public void rsvpEvent(int eventId, int userId, boolean attending) throws SQLException {
        String sql = "INSERT INTO event_attendees (event_id, user_id, attending) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE attending = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            stmt.setBoolean(3, attending);
            stmt.setBoolean(4, attending);
            stmt.executeUpdate();
        }
    }

  // Method to view the events user registered for
  public List<Event> getRegisteredEventsForUser(int userId) throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT e.* FROM events e " +
                "INNER JOIN event_attendees ea ON e.id = ea.event_id " +
                "WHERE ea.user_id = ? AND e.event_date >= ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDate currentDate = LocalDate.now();
            Date sqlCurrentDate = Date.valueOf(currentDate);

            stmt.setInt(1, userId);
            stmt.setDate(2, sqlCurrentDate);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("venue"),
                        rs.getDate("event_date"),
                        rs.getTime("event_time"),
                        rs.getInt("community_nid"),
                        rs.getBoolean("members_only")
                );
                events.add(event);
            }
        }

        if (events.isEmpty()) {
            System.out.println("No upcoming events registered.");
        }

        return events;
    }

  // Method to add an event (only for event organizers)
  public void addEvent(Event event, int organizerId) throws SQLException {
        String sql = "INSERT INTO events (name, description, venue, event_date, event_time, community_nid, members_only, organizer_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getName());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getVenue());
            stmt.setDate(4, event.getDate());
            stmt.setTime(5, event.getTime());
            stmt.setInt(6, event.getCommunityId());
            stmt.setBoolean(7, event.isMembersOnly());
            stmt.setInt(8, organizerId);
            stmt.executeUpdate();
        }
    }

    // Method to update event time and venue
    public boolean updateEventTimeAndVenue(int eventId, int organizerId, Date newDate, Time newTime, String newVenue)
            throws SQLException {
        // Check if the user is an event organizer for this event
        String authCheckSql = "SELECT e.id FROM events e WHERE e.id = ? AND e.organizer_id = ?";

        try (Connection conn = Database.getConnection();
                PreparedStatement authStmt = conn.prepareStatement(authCheckSql)) {

            authStmt.setInt(1, eventId);
            authStmt.setInt(2, organizerId);
            ResultSet authRs = authStmt.executeQuery();

            if (!authRs.next()) {
                // User is not authorized to update this event
                System.out.println("You don't have permission to update this event.");
                return false;
            }

            // User is authorized, proceed with update
            String updateSql = "UPDATE events SET event_date = ?, event_time = ?, venue = ? WHERE id = ?";

            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setDate(1, newDate);
                updateStmt.setTime(2, newTime);
                updateStmt.setString(3, newVenue);
                updateStmt.setInt(4, eventId);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Event time and venue updated successfully!");

                    // Optional: Notify registered attendees about the change
                    notifyAttendeesAboutUpdate(eventId, conn);

                    return true;
                } else {
                    System.out.println("Failed to update event. Event may not exist.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating event time and venue: " + e.getMessage());
            throw e;
        }
    }
}

