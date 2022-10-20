package com.socket.socketPractice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SocketDataResource implements Serializable {

    private String roomName;
    private Double latitude;
    private Double longitude;
    private Long timestamp;
    private Double speed;

    public SocketDataResource(String roomName, Long timestamp) {
        this.roomName = roomName;
        this.latitude = 26.7664;
        this.longitude = 32.3242;
        this.speed = 8.33;
        this.timestamp = timestamp;
    }

}



