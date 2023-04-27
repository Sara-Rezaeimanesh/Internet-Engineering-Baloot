package com.baloot.IE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BalootApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(BalootApplication.class, args);
    }
}

//@SpringBootApplication
//public class Bolbolestan05Application {
//
//    public static void main(String[] args) {
//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        try {
//            EducationSystem.getInstance().importDataFromWeb();
//            // Start scheduler for wait list
//            new MinJob().run();
//            scheduler.scheduleAtFixedRate(new MinJob(), 0, 1, TimeUnit.MINUTES);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("run mere");
//        SpringApplication.run(Bolbolestan05Application.class, args);
//    }
//}

