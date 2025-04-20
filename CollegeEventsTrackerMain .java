import java.util.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.SQLException;

public class CollegeEventsTrackerMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Authentication auth = new Authentication();
        try {
            while (true) {
                System.out.println("1. Sign Up");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 1) {
                    // Sign-up logic
                    try {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        System.out.print("Enter role (student/club_member/event_organizer): ");
                        String role = scanner.nextLine();

                        // For club_member and event_organizer, ask for club ID
                        int clubId = 0;
                        if (role.equalsIgnoreCase("club member") || role.equalsIgnoreCase("event organizer")) {
                            System.out.print("Enter club ID: ");
                            clubId = Integer.parseInt(scanner.nextLine());
                        }

                        auth.registerUser(username, password, role, clubId);
                        System.out.println("Registration successful!");
                    } catch (SQLException e) {
                        System.out.println("Registration failed: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for Club ID. Please enter a number.");
                    }
                } else if (choice == 2) {
                    // Login logic
                    try {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        User user = auth.authenticate(username, password);

                        if (user != null) {
                            System.out.println("Login successful!");
                            mainMenu(scanner, user); // Pass user to mainMenu
                        } else {
                            System.out.println("Invalid credentials.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Login failed: " + e.getMessage());
                    }
                } else if (choice == 0) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number for the menu choice.");
        } finally {
            scanner.close();
        }
    }

    // Main menu after login
    private static void mainMenu(Scanner scanner, User user) {
        EventManagement eventMgmt = new EventManagement();

        while (true) {
            System.out.println("\n1. Search Events by Community");
            System.out.println("2. Search Event by Name");
            System.out.println("3. RSVP to Event");
            System.out.println("4. Your Upcoming Events");
            if (user.canAddEvents()) {
                System.out.println("5. Add Event");
            }
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            try {
                if (choice == 1) {
                    System.out.print("Enter community name: ");
                    String community = scanner.nextLine();
                    Calendar cal = Calendar.getInstance();
                    int month = cal.get(Calendar.MONTH) + 1;
                    List<Event> events = eventMgmt.getEventsByCommunity(community);
                    for (Event event : events) {
                        System.out.println(event.getName() + " on " + event.getDate() + " at " + event.getTime()
                                + ", Venue: " + event.getVenue());
                        System.out.println("Event description: " + event.getDescription());
                    }
                } else if (choice == 2) {
                    System.out.print("Enter event name: ");
                    String eventName = scanner.nextLine();
                    Event event = eventMgmt.getEventByName(eventName);
                    if (event != null) {
                        System.out.println(event.getName() + " on " + event.getDate() + " at " + event.getTime()
                                + ", Venue: " + event.getVenue());
                        System.out.println("Event description: " + event.getDescription());
                    } else {
                        System.out.println("Event not found.");
                    }
                } else if (choice == 3) {
                    System.out.print("Enter event name to RSVP: ");
                    String eventName = scanner.nextLine();
                    Event event = eventMgmt.getEventByName(eventName);
                    if (event != null) {
                        System.out.print("RSVP (yes/no): ");
                        String rsvp = scanner.nextLine();
                        eventMgmt.rsvpEvent(event.getEventId(), user.getUserId(), "yes".equalsIgnoreCase(rsvp));
                        System.out.println("RSVP updated.");
                    } else {
                        System.out.println("Event not found.");
                    }
                } else if (choice == 4) {
                    try {
                        List<Event> registeredEvents = eventMgmt.getRegisteredEventsForUser(user.getUserId());
                        if (registeredEvents.isEmpty()) {
                            System.out.println("No upcoming events registered.");
                        } else {
                            System.out.println("Your Upcoming Registered Events:");
                            for (Event event : registeredEvents) {
                                System.out.println(event.getName() + " on " + event.getDate() + " at " + event.getTime()
                                        + ", Venue: " + event.getVenue());
                                System.out.println("Event description: " + event.getDescription());
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error fetching registered events: " + e.getMessage());
                    }
                } else if (choice == 5 && user.canAddEvents()) {
                    // Add event logic
                    System.out.print("Event name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    System.out.print("Event date (YYYY-MM-DD): ");
                    Date date = Date.valueOf(scanner.nextLine());
                    System.out.print("Event time (HH:MM:SS): ");
                    Time time = Time.valueOf(scanner.nextLine());
                    System.out.print("Venue: ");
                    String venue = scanner.nextLine();
                    System.out.print("Community ID: ");
                    int communityID = Integer.parseInt(scanner.nextLine());
                    System.out.print("Members only? (true/false): ");
                    boolean membersOnly = Boolean.parseBoolean(scanner.nextLine());
                    Event newEvent = new Event(0, name, description, venue, date, time, communityID, membersOnly);
                    eventMgmt.addEvent(newEvent, user.getUserId());
                    System.out.println("Event added.");
                } else if (choice == 0) {
                    System.out.println("Logging out...");
                    break;
                } else {
                    System.out.println("Invalid option.");
                }
            } catch (SQLException e) {
                System.out.println("SQL Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date or time format. Please use YYYY-MM-DD and HH:MM:SS.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
