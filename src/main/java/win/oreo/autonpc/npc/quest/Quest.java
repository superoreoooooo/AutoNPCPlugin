package win.oreo.autonpc.npc.quest;

import java.util.UUID;

public class Quest {
    private String name;
    private String goal;
    private String reward;
    private String story;
    private UUID id;

    public Quest(String name, String goal, String reward, String story) {
        this.name = name;
        this.goal = goal;
        this.reward = reward;
        this.story = story;
        this.id = UUID.randomUUID();
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
