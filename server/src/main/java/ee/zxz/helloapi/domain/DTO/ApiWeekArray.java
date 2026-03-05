package ee.zxz.helloapi.domain.DTO;

import java.time.LocalDateTime;

public class ApiWeekArray {
    private LocalDateTime date;
    private Integer count;

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
}
