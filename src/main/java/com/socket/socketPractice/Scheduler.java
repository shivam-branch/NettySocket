package com.socket.socketPractice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private TcpServer server;

    @Scheduled(fixedRate = 5000)
    public void updateDataSchedule() {
        server.updateMap();
    }
}

