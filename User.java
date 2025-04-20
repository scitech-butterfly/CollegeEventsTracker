public abstract class User {
    protected final int userId; // now int (matches SQL schema)
    protected String name;
    protected String email;
    protected String role;

    // Constructor
    public User(int userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }
