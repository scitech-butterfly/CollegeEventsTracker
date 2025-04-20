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
}

