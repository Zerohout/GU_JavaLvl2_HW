package lesson1.participants;

import static lesson1.Helper.*;

public class Cat extends Participant implements ILeaping, IRunning, IFloating {
    private int jumpHeight;
    private int runDistance;
    private int swimDistance;

    public Cat(String name) {
        super(name);
        this.jumpHeight = setMaxValue(2, 4);
        this.runDistance = setMaxValue(300, 500);
        this.swimDistance = setMaxValue(200, 400);
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
