package lesson5;

import lesson5.joinVersion.JoinThreadsVersion;
import lesson5.syncVersion.SynchronizedVersion;

public class Lesson5 {
   public void start(){
       var syncVersion = new SynchronizedVersion();
       var joinThreadVersion = new JoinThreadsVersion();


       syncVersion.start();
       joinThreadVersion.start();
   }
}
