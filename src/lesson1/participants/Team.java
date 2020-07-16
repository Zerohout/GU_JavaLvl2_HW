package lesson1.participants;

import lesson1.obstacles.Obstacle;

import static lesson1.Helper.*;

public class Team {
    private Participant[] participants;

    public Team(Participant[] participants) {
        this.participants = participants;
    }


    public Participant[] getParticipants(){
        return participants;
    }

    public void startCompetition(Obstacle[] obstacles){
        OvercomingStatus result = OvercomingStatus.SUCCESSFUL;

        for (var participant : participants) {
            participant.prepare();
            for (var obstacle : obstacles) {
                result = obstacle.overcomingObstacle(participant);
                if (result == OvercomingStatus.INCOMPETENT) continue;
                if (result == OvercomingStatus.FAILING) break;
            }
            participant.finish(result);
        }
    }
}
