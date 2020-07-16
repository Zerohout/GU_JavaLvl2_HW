package lesson1;

import static lesson1.Helper.*;

public class Lesson1 {
    public void start() {
        var team = createTeam();
        var obstacles = createObstacles();
        team.startCompetition(obstacles);
    }
}
