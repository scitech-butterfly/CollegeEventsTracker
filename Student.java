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
            if ("yes".equalsIgnoreCase(rsvpStatus)) {
                String query = "REPLACE INTO RSVPs (user_id, event_id, rsvp_status) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                stmt.setInt(2, eventId);
                stmt.setString(3, rsvpStatus.toLowerCase()); // yes
                stmt.executeUpdate();
                System.out.println("RSVP '" + rsvpStatus + "' submitted for event " + eventId);
            } else if ("no".equalsIgnoreCase(rsvpStatus)) {
                // Delete the RSVP entry instead of storing a "no"
                String deleteQuery = "DELETE FROM RSVPs WHERE user_id = ? AND event_id = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setInt(1, userId);
                deleteStmt.setInt(2, eventId);
                int rowsAffected = deleteStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("RSVP declined for event " + eventId);
                } else {
                    System.out.println("No RSVP found to delete for event " + eventId);
                }
            } else {
                System.out.println("Invalid RSVP status. Please use 'yes' or 'no'.");
            }
        } catch (SQLException e) {
            System.out.println("Error during RSVP: " + e.getMessage());
        }
    }
}

