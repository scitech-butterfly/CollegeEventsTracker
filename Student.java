import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Student extends User {

    public Student(int userId, String name, String email) {
        super(userId, name, email, "student");
    }

    @Override
    public boolean canAddEvents() {
        return false;
    }

    @Override
    public void displayProfile() {
        super.displayProfile();
    }

    public void registerForEvent(int eventId, String rsvpStatus) {
        try (Connection conn = db.Database.getConnection()) {
            String query = "REPLACE INTO RSVPs (user_id, event_id, rsvp_status) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            stmt.setString(3, rsvpStatus.toLowerCase()); // yes or no
            stmt.executeUpdate();
            System.out.println("RSVP '" + rsvpStatus + "' submitted for event " + eventId);
        } catch (SQLException e) {
            System.out.println("Error during RSVP: " + e.getMessage());
        }
    }
}

