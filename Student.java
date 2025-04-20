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
