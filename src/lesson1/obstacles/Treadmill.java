package lesson1.obstacles;

import static lesson1.Helper.*;
import lesson1.participants.Participant;
import lesson1.participants.IRunning;

import java.util.Random;

public class Treadmill extends Obstacle {
    private int length;

    public Treadmill(String name) {
        super(name);
        this.length = new Random().nextInt(500);
    }

    @Override
    public OvercomingStatus overcomingObstacle(Participant participant) {
        if (participant instanceof IRunning) {
            System.out.println(String.format("Current obstacle is %s. Length - %d", name, length));
            return ((IRunning) participant).run() >= length ?
                    OvercomingStatus.SUCCESSFUL :
                    OvercomingStatus.FAILING;
        }
        return OvercomingStatus.INCOMPETENT;
    }
}
