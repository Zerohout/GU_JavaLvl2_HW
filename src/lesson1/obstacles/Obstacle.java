package lesson1.obstacles;

import static lesson1.Helper.*;
import lesson1.participants.Participant;


public abstract class Obstacle {
    protected String name;

    public Obstacle(String name) {
        this.name = name;
    }

    public abstract OvercomingStatus overcomingObstacle(Participant participant);
}
