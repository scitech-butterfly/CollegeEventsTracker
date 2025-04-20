// Event.java
import java.sql.Date;
import java.sql.Time;

public class Event {
  private int id;
  private String name;
  private String description;
  private Date date;
  private Time time;
  private String venue;
  private int communityID;
  private boolean membersOnly;

  public Event(int id, String name, String description, String venue, Date date, Time time, int communityID,
      boolean membersOnly) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.venue = venue;
    this.date = date;
    this.time = time;
    this.communityID = communityID;
    this.membersOnly = membersOnly;
  }
}

