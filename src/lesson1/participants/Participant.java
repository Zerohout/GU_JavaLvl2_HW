package lesson1.participants;

import static lesson1.Helper.*;

public abstract class Participant {
    protected String name;

    public Participant(String name) {
        this.name = name;
    }

    public void prepare() {
        System.out.println(String.format("%s preparing to overcome obstacles...", this.name));
    }

    public void finish(OvercomingStatus result) {
        //Если результат INCOMPETENT значит сущность прошла все препятствия,
        // а последнее просто напросто оказалось непреодолимое для данной сущности в связи с отсутствием необходимого интерфейса
        if (result == OvercomingStatus.INCOMPETENT) result = OvercomingStatus.SUCCESSFUL;
        System.out.println(String.format("%s has finished. Result of overcoming obstacles is %s.%s",
                name, result, SEPARATOR));
    }
}
