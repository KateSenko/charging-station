package com.example.chargestation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class ChargeSession {

    private String id;
    private String stationId;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private StatusEnum status;
    @JsonIgnore
    private Long updateNanoTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getStoppedAt() {
        return stoppedAt;
    }

    public void setStoppedAt(LocalDateTime stoppedAt) {
        this.stoppedAt = stoppedAt;
    }

    public Long getUpdateNanoTime() {
        return updateNanoTime;
    }

    public void setUpdateNanoTime(Long updateNanoTime) {
        this.updateNanoTime = updateNanoTime;
    }
}
