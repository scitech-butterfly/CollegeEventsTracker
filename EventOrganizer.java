
// EventOrganizer class - inherits from ClubMember (multi-level inheritance)
class EventOrganizer extends ClubMember {
    public EventOrganizer(int userId, String name, String email, int clubId) {
        super(userId, name, email, clubId);
    }

    @Override
    public boolean canAddEvents() {
        return true;
    }

    @Override
    public void displayProfile() {
        super.displayProfile();
        System.out.println("Role: Event Organizer");
    }
}
