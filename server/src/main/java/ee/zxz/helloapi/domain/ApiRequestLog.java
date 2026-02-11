package ee.zxz.helloapi.domain;

import java.time.LocalDateTime;

public class ApiRequestLog {
    private int app_id;
    private String ip;
    private LocalDateTime time;

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
