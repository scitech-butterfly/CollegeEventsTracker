import db.Database;
import java.sql.*;

public class Authentication {
    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                int id = rs.getInt("id");
                if ("student".equals(role)) {
                    return new Student(id, username, password);
                } else if ("club member".equals(role)) {
                    return new ClubMember(id, username, password, rs.getInt("club_id"));
                } else if ("event organizer".equals(role)) {
                    return new EventOrganizer(id, username, password, rs.getInt("club_id"));
                }
            }
        }
        return null;
    }
 public void registerUser(String username, String password, String role, int clubId) throws SQLException {
        // Check if username already exists
        if (userExists(username)) {
            throw new SQLException("Username already exists");
        }

        String sql = "INSERT INTO users (username, password, role, club_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            if (clubId > 0 && ("club member".equals(role) || "event organizer".equals(role))) {
                stmt.setInt(4, clubId);
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();
        }
    }
