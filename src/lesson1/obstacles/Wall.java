package lesson1.obstacles;

import static lesson1.Helper.*;
import lesson1.participants.Participant;
import lesson1.participants.ILeaping;

import java.util.Random;

public class Wall extends Obstacle {
    private int height;

    public Wall(String name) {
        super(name);
        this.height = new Random().nextInt(4);
    }

    @Override
    public OvercomingStatus overcomingObstacle(Participant participant) {
        if (participant instanceof ILeaping) {
            System.out.println(String.format("Current obstacle is %s. Height - %d", name, height));
            return ((ILeaping) participant).jump() >= height ?
                    OvercomingStatus.SUCCESSFUL :
                    OvercomingStatus.FAILING;
        }
        return OvercomingStatus.INCOMPETENT;
    }
}
