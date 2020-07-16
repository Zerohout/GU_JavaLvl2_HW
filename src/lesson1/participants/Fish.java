package lesson1.participants;

import static lesson1.Helper.*;

public class Fish extends Participant implements ILeaping, IFloating {
    private int jumpHeight;
    private int swimDistance;

    public Fish(String name) {
        super(name);
        this.jumpHeight = setMaxValue(1, 3);
        this.swimDistance = setMaxValue(400, 600);
    }

    @Override
    public int jump() {
        System.out.println(String.format("%s jumped %d meters.", name, jumpHeight));
        return jumpHeight;
    }

    @Override
    public int swim() {
        System.out.println(String.format("%s swam %d meters.", name, swimDistance));
        return swimDistance;
    }
}
