package ee.zxz.helloapi.service;

import ee.zxz.helloapi.mapper.StatMapper;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ApiLogManager {

    @Resource
    private StatMapper statMapper;

    /**
     * 异步写入日志
     */
    @Async("logExecutor")
    public void saveLogAsync(int appId, String ip, Object header, Object body, String apiKey, int userId) {
        try {
            // 增加API调用次数
            statMapper.addApiCount(appId);
            // 写入日志
            statMapper.insertApiLog(appId, ip, header, body, apiKey, userId);
        } catch (Exception e) {
            System.err.println("异步日志写入失败: " + e.getMessage());
        }
    }
}
