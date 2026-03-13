package ee.zxz.helloapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
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

    public boolean isValidJson(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            JSON.parse(str);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    @Resource
    private ApiLogManager apiLogManager;

    @Override
    public Map<String, Object> getStat(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request, String authorizationHeader) {
        try {
            String type = requestBody.get("type");
            if (type == null || type.isEmpty()) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
            }

            boolean isLogin = false;
            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                if (Tools.tokenToUserId(authorizationHeader) > 0) {
                    isLogin = true;
                }
            }

            int count, change = 0;
            switch (type) {
                // 用户总数
                case "userCount":
                    if (!isLogin) {
                        return ResponseUtil.response(401, Finals.MESSAGES_TOKEN_TIME_OUT);
                    }
                    count = statMapper.getUserCount();
                    break;
                // 本月注册用户数
                case "userMonthRegisterCount":
                    if (!isLogin) {
                        return ResponseUtil.response(401, Finals.MESSAGES_TOKEN_TIME_OUT);
                    }
                    count = statMapper.getUserMonthCount();
                    break;
                // API接口数量
                case "getApiAppCount":
                    if (!isLogin) {
                        return ResponseUtil.response(401, Finals.MESSAGES_TOKEN_TIME_OUT);
                    }
                    count = statMapper.getApiAppCount();
                    break;
                // 近30天API发布数量
                case "getApiAppMonthCount":
                    if (!isLogin) {
                        return ResponseUtil.response(401, Finals.MESSAGES_TOKEN_TIME_OUT);
                    }
                    count = statMapper.getApiAppMonthCount();
                    break;
                // API调用总次数
                case "apiAllCount":
                    count = statMapper.getApiAllCount();
                    break;
                // 今天调用总次数
                case "apiTodayCount":
                    if (!isLogin) {
                        return ResponseUtil.response(401, Finals.MESSAGES_TOKEN_TIME_OUT);
                    }
                    count = statMapper.getApiTodayCount();
                    change = statMapper.getApiTodayCountChange();
                    break;
                // 本周调用总次数
                case "apiWeekCount":
                    if (!isLogin) {
                        return ResponseUtil.response(401, Finals.MESSAGES_TOKEN_TIME_OUT);
                    }
                    count = statMapper.getApiWeekCount();
                    change = statMapper.getApiWeekCountChange();
                    break;
                // 本月调用总次数
                case "apiMonthCount":
                    if (!isLogin) {
                        return ResponseUtil.response(401, Finals.MESSAGES_TOKEN_TIME_OUT);
                    }
                    count = statMapper.getApiMonthCount();
                    change = statMapper.getApiMonthCountChange();
                    break;
                // 近7天API调用统计（数组）
                case "apiWeekCountArray":
                    if (!isLogin) {
                        return ResponseUtil.response(401, Finals.MESSAGES_TOKEN_TIME_OUT);
                    }
                    return ResponseUtil.success(statMapper.getApiWeekCountArray());
                // 今天API调用数量前7的接口（包含今天调用次数为0的接口）
                case "apiTodayCountArray":
                    if (!isLogin) {
                        return ResponseUtil.response(401, Finals.MESSAGES_TOKEN_TIME_OUT);
                    }
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
                return ResponseUtil.response(400, "调用失败，请联系站长/开发者 代码：ERROR_1", "user_key用户密钥不存在");
            }

            // 检查userID与Key是否匹配
            if (userMapper.getUserIdByUserKey(requestBody.get("user_key")) != Tools.strToInt(requestBody.get("user_id"))) {
                return ResponseUtil.response(400, "调用失败，请联系站长/开发者 代码：ERROR_2", "user_id与user_key不匹配");
            }


            // 校验ApiId是否存在
            int api_id = Tools.strToInt(requestBody.get("api_id"));
            if (apiMapper.checkApiExist(api_id) < 1) {
                return ResponseUtil.response(400, "调用失败，请联系站长/开发者 代码：ERROR_3", "api_id不存在");
            }

            // 校验过审没有
            if (apiMapper.checkApiViewStatus(api_id) < 1) {
                return ResponseUtil.response(400, "调用失败，请联系站长/开发者 代码：ERROR_4", "api_id未过审");
            }

            String api_key = requestBody.get("api_key");
            if (api_key != null && !api_key.isEmpty()) {
                ApiKey apiKey = apiMapper.getApiKey(api_key);
                if (apiKey == null) {
                    return ResponseUtil.response(400, "API密钥 / APIKey不存在", "api_key不存在");
                }
                if (apiKey.getApi_id() != api_id) {
                    return ResponseUtil.response(400, "API密钥 / APIKey错误", "api_id与api_key不匹配");
                }
                // 判断密钥类型
                if (apiKey.getType() == 0) {
                    // 0 时间类型
                    // 检查是否过期
                    if (LocalDateTime.now().isAfter(apiKey.getExpired())) {
                        return ResponseUtil.response(400, "API密钥 / APIKey过期", Finals.MESSAGES_ERROR_KEY_EXPIRED);
                    }
                } else {
                    // 1 计数类型
                    // 检查是否用完
                    if (apiKey.getCount() <= 0) {
                        return ResponseUtil.response(400, "API密钥 / APIKey耗尽", Finals.MESSAGES_ERROR_KEY_COUNT_EXPIRED);
                    }
                    // 减少次数
                    apiMapper.reduceApiKeyCount(api_key);
                }
            }


            String header = requestBody.get("header");
            String body = requestBody.get("body");
            header = isValidJson(header) ? header : null;
            body = isValidJson(body) ? body : null;

            // 记录日志
            apiLogManager.saveLogAsync(
                    api_id,
                    requestBody.get("ip"),
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


