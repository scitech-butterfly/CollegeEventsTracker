// Club class (hierarchical inheritance)
class Club extends Community {
    private String category;

    public Club(String communityId, String name, String category) {
        super(communityId, name);
        this.category = category;
    }
    @Override
    public String getCommunityType() {
        return "Club";
    }
