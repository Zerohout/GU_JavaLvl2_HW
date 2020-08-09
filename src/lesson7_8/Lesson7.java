package lesson7_8;

import lesson7_8.Helpers.ControlPanel;

public class Lesson7 {
    public void start(){
        new Thread(ControlPanel::new).start();
    }
}
