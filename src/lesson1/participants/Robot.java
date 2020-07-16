package lesson1.participants;

public class Robot extends Participant implements ILeaping, IRunning {
    private int jumpHeight;
    private int runDistance;

    public Robot(String name) {
        super(name);
        this.jumpHeight = 5;
        this.runDistance = 600;
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
}
