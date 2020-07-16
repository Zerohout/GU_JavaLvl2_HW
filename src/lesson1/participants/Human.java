package lesson1.participants;

import static lesson1.Helper.*;

public class Human extends Participant implements ILeaping, IRunning, IFloating {
    private int jumpHeight;
    private int runDistance;
    private int swimDistance;

    public Human(String name) {
        super(name);
        this.jumpHeight = setMaxValue(1, 3);
        this.runDistance = setMaxValue(200, 400);
        this.swimDistance = setMaxValue(300, 500);
    }

    @Override
    public int jump() {
        System.out.println(String.format("%s jumped %d meters.", name, jumpHeight));
        return jumpHeight;
    }

    @Override
    public int run() {
        System.out.println(String.format("%s ran %d meters.", name, runDistance));
        return runDistance;
    }

    @Override
    public int swim() {
        System.out.println(String.format("%s swam %d meters.", name, swimDistance));
        return swimDistance;
    }
}
