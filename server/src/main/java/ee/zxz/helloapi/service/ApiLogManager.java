package ee.zxz.helloapi.service;

import ee.zxz.helloapi.mapper.ApiMapper;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ApiLogManager {

    @Resource
    private ApiMapper apiMapper;

    /**
     * 异步写入日志
     */
    @Async("logExecutor")
    public void saveLogAsync(int appId, String ip, String time, Object header, Object body) {
        try {
            // 增加API调用次数
            apiMapper.addApiCount(appId);
            // 写入日志
            apiMapper.insertApiLog(appId, ip, time, header, body);
        } catch (Exception e) {
            System.err.println("异步日志写入失败: " + e.getMessage());
        }
    }
}
