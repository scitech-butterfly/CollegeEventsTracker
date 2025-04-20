# College Events Tracker - Community Module

This module is part of a **College Events Tracker System**, which helps in managing different types of college communities such as **Clubs** and **Departments**. It provides the base architecture for community-related features such as adding members and organizing events.


## 🧠 Class Overview

### 1. `Community` (Abstract)
The base class for all community types (e.g., Club, Department).

- **Fields:**
  - `communityId` (final): Unique identifier for the community.
  - `name`: Name of the community.
  - `events`: List of `Event` objects associated with the community.
  - `memberIds`: List of user IDs who are members of the community.

- **Methods:**
  - `getCommunityType()` (abstract): Implemented in subclasses to specify the type.
  - `addMember(String userId)`: Adds a user to the community.
  - `isMember(String userId)`: Checks if a user is part of the community.
  - `addEvent(Event event)`: Associates an event with the community.
  - Getters: `getEvents()`, `getCommunityId()`, `getName()`

---

### 2. `Club` (extends Community)
Represents a club in the college (e.g., Coding Club, Art Club).

- **Additional Field:**
  - `category`: The category of the club (e.g., Technical, Cultural).

- **Overrides:**
  - `getCommunityType()` → returns `"Club"`

- **Getter:**
  - `getCategory()`

---

### 3. `Department` (extends Community)
Represents an academic department in the college (e.g., Computer Science, Mechanical Engineering).

- **Additional Field:**
  - `facultyHead`: Name of the department's faculty head.

- **Overrides:**
  - `getCommunityType()` → returns `"Department"`

- **Getter:**
  - `getFacultyHead()`

---

### 4. `CommunityNotFoundException` (Custom Exception)
Custom checked exception to handle scenarios when a requested community is not found.

- **Constructor:**
  - `CommunityNotFoundException(String message)`

