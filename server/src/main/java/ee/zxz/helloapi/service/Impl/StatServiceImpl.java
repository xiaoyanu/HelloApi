package ee.zxz.helloapi.service.Impl;

import com.alibaba.fastjson.JSON;
import ee.zxz.helloapi.domain.ApiKey;
import ee.zxz.helloapi.domain.DTO.ApiTodayArray;
import ee.zxz.helloapi.mapper.ApiMapper;
import ee.zxz.helloapi.mapper.StatMapper;
import ee.zxz.helloapi.mapper.UserMapper;
import ee.zxz.helloapi.service.ApiLogManager;
import ee.zxz.helloapi.service.StatService;
import ee.zxz.helloapi.utils.Finals;
import ee.zxz.helloapi.utils.ResponseUtil;
import ee.zxz.helloapi.utils.Tools;
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
    public Map<String, Object> getStat(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request) {
        try {
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

    @Override
    public Map<String, Object> logApi(Map<String, String> requestBody, HttpServletRequest request) {
        try {
            // 检查用户密钥是否存在
            if (userMapper.checkUserKeyExists(requestBody.get("user_key")) < 1) {
                return ResponseUtil.error("user_key用户密钥不存在");
            }

            // 检查userID与Key是否匹配
            if (userMapper.getUserIdByUserKey(requestBody.get("user_key")) != Tools.strToInt(requestBody.get("user_id"))) {
                return ResponseUtil.error("user_id与user_key不匹配");
            }


            // 校验ApiId是否存在
            int api_id = Tools.strToInt(requestBody.get("api_id"));
            if (apiMapper.checkApiExist(api_id) < 1) {
                return ResponseUtil.error("api_id不存在");
            }

            String api_key = requestBody.get("api_key");
            if (api_key != null && !api_key.isEmpty()) {
                ApiKey apiKey = apiMapper.getApiKey(api_key);
                if (apiKey == null) {
                    return ResponseUtil.error("api_key不存在");
                }
                if (apiKey.getApi_id() != api_id) {
                    return ResponseUtil.error("api_id与api_key不匹配");
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
                    apiMapper.reduceApiKeyCount(api_key);
                }
            }


            String headerStr = requestBody.get("header");
            String bodyStr = requestBody.get("body");
            Object header = null;
            Object body = null;
            try {
                header = JSON.parse(headerStr);
            } catch (Exception ignored) {
            }
            try {
                body = JSON.parse(bodyStr);
            } catch (Exception ignored) {
            }

            // 记录日志
            apiLogManager.saveLogAsync(
                    api_id,
                    requestBody.get("app_id"),
                    header,
                    body,
                    api_key,
                    Tools.strToInt(requestBody.get("user_id"))
            );
            return ResponseUtil.success();
        } catch (Exception e) {
            return ResponseUtil.error(Finals.MESSAGES_ERROR_PARAM);
        }
    }

}
