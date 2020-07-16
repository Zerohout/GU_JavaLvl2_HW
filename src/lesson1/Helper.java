package lesson1;

import lesson1.obstacles.Obstacle;
import lesson1.obstacles.River;
import lesson1.obstacles.Treadmill;
import lesson1.obstacles.Wall;
import lesson1.participants.*;

import java.util.Random;

public class Helper {
    public static final String SEPARATOR = "\n***********************************************\n";

    public enum OvercomingStatus {
        SUCCESSFUL,
        FAILING,
        INCOMPETENT
    }

    public static int setMaxValue(int min, int max) {
        var rnd = new Random();
        return rnd.nextInt(max - min) + min;
    }

    static Team createTeam() {
        var participants = new Participant[]{
                new Cat("Cat"),
                new Fish("Fish"),
                new Human("Human"),
                new Robot("Robot")
        };
        return new Team(participants);
    }

    static Obstacle[] createObstacles() {
        return new Obstacle[]{
                new Wall("Wall"),
                new Treadmill("Treadmill"),
                new River("River")
        };
    }
}
