package lesson1.obstacles;

import static lesson1.Helper.*;

import lesson1.participants.Participant;
import lesson1.participants.IFloating;

import java.util.Random;

public class River extends Obstacle {
    private int length;

    public River(String name) {
        super(name);
        this.length = new Random().nextInt(500);
    }

    @Override
    public OvercomingStatus overcomingObstacle(Participant participant) {
        if (participant instanceof IFloating) {
            System.out.println(String.format("Current obstacle is %s. Length - %d", name, length));
            return ((IFloating) participant).swim() >= length ?
                    OvercomingStatus.SUCCESSFUL :
                    OvercomingStatus.FAILING;
        }
        return OvercomingStatus.INCOMPETENT;
    }

}
