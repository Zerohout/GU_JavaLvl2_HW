package lesson7;

import lesson7.Helpers.ControlPanel;

public class Lesson7 {
    public void start(){
        new Thread(ControlPanel::new).start();
    }
}
