package ee.zxz.helloapi.service.Impl;

import ee.zxz.helloapi.domain.ApiApp;
import ee.zxz.helloapi.domain.ApiKey;
import ee.zxz.helloapi.domain.ApiRequestLog;
import ee.zxz.helloapi.domain.DTO.ApiTodayArray;
import ee.zxz.helloapi.mapper.ApiMapper;
import ee.zxz.helloapi.mapper.StatMapper;
import ee.zxz.helloapi.mapper.UserMapper;
import ee.zxz.helloapi.service.ApiLogManager;
import ee.zxz.helloapi.service.StatService;
import ee.zxz.helloapi.utils.Finals;
import ee.zxz.helloapi.utils.ResponseUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatServiceImpl implements StatService {
    private final ApiMapper apiMapper;
    private final UserMapper userMapper;
    private final StatMapper statMapper;

    public StatServiceImpl(ApiMapper apiMapper, UserMapper userMapper, StatMapper statMapper) {
        this.apiMapper = apiMapper;
        this.userMapper = userMapper;
        this.statMapper = statMapper;
    }

    @Resource
    private ApiLogManager apiLogManager;

    @Override
    public Map<String, Object> logApi(String userKey, String key, ApiRequestLog apiRequestLog, HttpServletRequest request) {
        // 检查用户密钥是否存在
        if (userMapper.checkUserKeyExists(userKey) < 1) {
            return ResponseUtil.error(Finals.MESSAGES_ERROR_KEY_NOT_FOUND);
        }

        ApiApp apiApp = apiMapper.getApiApp(apiRequestLog.getApp_id());

        // 校验ApiId是否存在
        if (apiApp == null) {
            return ResponseUtil.error(Finals.MESSAGES_ERROR_API_NOT_FOUND);
        }

        if (key != null) {
            ApiKey apiKey = apiMapper.getApiKey(key);
            if (apiKey == null) {
                return ResponseUtil.error(Finals.MESSAGES_ERROR_KEY_NOT_FOUND);
            }
            // 判断密钥类型
            if (apiKey.getType() == 0) {
                // 0 时间类型
                // 检查是否过期
                if (LocalDateTime.now().isAfter(apiKey.getExpired())) {
                    return ResponseUtil.error(Finals.MESSAGES_ERROR_KEY_EXPIRED);
                }
            } else {
                // 1 计数类型
                // 检查是否用完
                if (apiKey.getCount() <= 0) {
                    return ResponseUtil.error(Finals.MESSAGES_ERROR_KEY_COUNT_EXPIRED);
                }
                // 减少次数
                apiMapper.reduceApiKeyCount(key);
            }
        }

        // 记录日志
        if (apiRequestLog.getHeader().equals("")) {
            apiRequestLog.setHeader(null);
        }
        if (apiRequestLog.getBody().equals("")) {
            apiRequestLog.setBody(null);
        }
        apiLogManager.saveLogAsync(
                apiRequestLog.getApp_id(),
                apiRequestLog.getIp(),
                apiRequestLog.getHeader(),
                apiRequestLog.getBody(),
                apiRequestLog.getApi_key(),
                apiRequestLog.getUser_id()
        );
        return ResponseUtil.success();
    }

    @Override
    public Map<String, Object> getStat(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request) {
        try {
//            int tokenMode = (int) request.getAttribute("userMode");
//            int tokenUserId = (int) request.getAttribute("userId");
            String type = requestBody.get("type");
            if (type == null || type.isEmpty()) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
            }
            int count, change = 0;
            switch (type) {
                case "userCount":
                    count = statMapper.getUserCount();
                    break;
                case "apiAllCount":
                    count = statMapper.getApiAllCount();
                    break;
                case "apiTodayCount":
                    count = statMapper.getApiTodayCount();
                    change = statMapper.getApiTodayCountChange();
                    break;
                case "apiWeekCount":
                    count = statMapper.getApiWeekCount();
                    change = statMapper.getApiWeekCountChange();
                    break;
                case "apiMonthCount":
                    count = statMapper.getApiMonthCount();
                    change = statMapper.getApiMonthCountChange();
                    break;
                case "apiWeekCountArray":
                    return ResponseUtil.success(statMapper.getApiWeekCountArray());
                case "apiTodayCountArray":
                    List<ApiTodayArray> list = statMapper.getApiTodayCountArray();
                    while (list.size() < 7) {
                        ApiTodayArray emptyItem = new ApiTodayArray();
                        emptyItem.setName("");
                        emptyItem.setCount(0);
                        list.add(emptyItem);
                    }
                    return ResponseUtil.success(list);
                default:
                    return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
            }
            Map<String, Integer> map = new HashMap<>();
            map.put("count", count);
            map.put("change", change);
            return ResponseUtil.success(map);
        } catch (Exception e) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }
    }
}
