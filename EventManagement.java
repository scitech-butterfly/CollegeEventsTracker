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
}

