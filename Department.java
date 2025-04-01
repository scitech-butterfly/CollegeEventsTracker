// Department class (hierarchical inheritance)
class Department extends Community {
    private String facultyHead;

    public Department(String communityId, String name, String facultyHead) {
        super(communityId, name);
        this.facultyHead = facultyHead;
    }
    @Override
    public String getCommunityType() {
        return "Department";
    }
    public String getFacultyHead() {
        return facultyHead;
    }
}
