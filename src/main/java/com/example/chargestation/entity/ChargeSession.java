package com.example.chargestation.entity;

public class ChargeSession {

    private String id;
    private String stationId;
    private Long startedAt;
    private Long stoppedAt;
    private StatusEnum status;

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

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Long getStoppedAt() {
        return stoppedAt;
    }

    public void setStoppedAt(Long stoppedAt) {
        this.stoppedAt = stoppedAt;
    }
}
