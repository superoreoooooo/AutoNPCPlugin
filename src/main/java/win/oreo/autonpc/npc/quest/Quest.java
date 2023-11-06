package win.oreo.autonpc.npc.quest;

public class Quest {
    private String name;
    private String goal;
    private String reward;
    private String story;

    public Quest(String name, String goal, String reward, String story) {
        this.name = name;
        this.goal = goal;
        this.reward = reward;
        this.story = story;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
