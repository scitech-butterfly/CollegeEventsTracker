import java.util.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

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
                        System.out.print("Enter role (student/club member/event organizer): ");
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
                System.out.println("6. Update Event Time and Venue"); 
                System.out.println("7. Delete Event");
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
                        eventMgmt.rsvpEvent(event.getEventId(), user.getUserId(), rsvp); // assuming rsvp is already "yes"/"no"
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
                } else if (choice == 6 && user.canAddEvents()) {
                    // Update event time and venue
                    updateEventTimeAndVenue(scanner, user, eventMgmt);
                } else if (choice == 7 && user.canAddEvents()){
                    System.out.println("Event Name: ");
                    String name = scanner.nextLine();
                    Event event = eventMgmt.getEventByName(name);
                    if (event != null) {
                        eventMgmt.deleteEvent(name);
                        System.out.println("Event Deleted!");
                    } else {
                        System.out.println("Event not found.");
                    }
                }else if (choice == 0) {
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

    // Helper method to handle event time and venue updates
    private static void updateEventTimeAndVenue(Scanner scanner, User user, EventManagement eventMgmt)
            throws SQLException {
        // First check if this user is actually an event organizer
        if (!eventMgmt.isEventOrganizer(user.getUserId())) {
            System.out.println("Only event organizers can update events.");
            return;
        }

        // Get the event to update
        System.out.println("Enter event name to update: ");
        String eventName = scanner.nextLine();
        Event event = eventMgmt.getEventByName(eventName);

        if (event == null) {
            System.out.println("Event not found.");
            return;
        }

        // Show current event details
        System.out.println("Current event details:");
        System.out.println("Name: " + event.getName());
        System.out.println("Date: " + event.getDate());
        System.out.println("Time: " + event.getTime());
        System.out.println("Venue: " + event.getVenue());

        // Get new details
        try {
            System.out.print("New event date (YYYY-MM-DD) or press Enter to keep current: ");
            String dateInput = scanner.nextLine();
            Date newDate = dateInput.isEmpty() ? event.getDate() : Date.valueOf(dateInput);

            System.out.print("New event time (HH:MM:SS) or press Enter to keep current: ");
            String timeInput = scanner.nextLine();
            Time newTime = timeInput.isEmpty() ? event.getTime() : Time.valueOf(timeInput);

            System.out.print("New venue or press Enter to keep current: ");
            String venueInput = scanner.nextLine();
            String newVenue = venueInput.isEmpty() ? event.getVenue() : venueInput;

            // Attempt to update the event
            boolean updated = eventMgmt.updateEventTimeAndVenue(event.getEventId(), user.getUserId(), newDate, newTime,
                    newVenue);

            if (updated) {
                System.out.println("Event updated successfully!");
            } else {
                System.out.println("Failed to update event. You may not have permission to update this event.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date or time format. Please use YYYY-MM-DD and HH:MM:SS.");
        }
    }
}
