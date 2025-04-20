
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClubMember extends Student {
    private int communityId;
    private String communityName;

    public ClubMember(int userId, String name, String email, int communityId) {
        super(userId, name, email);
        this.communityId = communityId;
        fetchCommunityName(); // fetch name from DB based on communityId
    }

    private void fetchCommunityName() {
        try (Connection conn = db.Database.getConnection()) {
            String query = "SELECT name FROM Communities WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, this.communityId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.communityName = rs.getString("name");
            } else {
                this.communityName = "Unknown";
            }
        } catch (SQLException e) {
            System.out.println("Error fetching community name: " + e.getMessage());
            this.communityName = "Unavailable";
        }
    }

    @Override
    public void displayProfile() {
        super.displayProfile();
        System.out.println("Member of club: " + communityName + " (ID: " + communityId + ")");
    }

    public int getCommunityId() {
        return this.communityId;
    }

    public String getCommunityName() {
        return this.communityName;
    }
}
