package ee.zxz.helloapi.service.Impl;

import com.alibaba.fastjson.JSON;
import ee.zxz.helloapi.domain.ApiApp;
import ee.zxz.helloapi.domain.ApiKey;
import ee.zxz.helloapi.domain.ApiParam;
import ee.zxz.helloapi.domain.ApiRequestLog;
import ee.zxz.helloapi.mapper.ApiMapper;
import ee.zxz.helloapi.mapper.UserMapper;
import ee.zxz.helloapi.service.ApiLogManager;
import ee.zxz.helloapi.service.ApiService;
import ee.zxz.helloapi.utils.Finals;
import ee.zxz.helloapi.utils.ResponseUtil;
import ee.zxz.helloapi.utils.Tools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ApiServiceImpl implements ApiService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ApiMapper apiMapper;
    private final UserMapper userMapper;

    public ApiServiceImpl(ApiMapper apiMapper, UserMapper userMapper) {
        this.apiMapper = apiMapper;
        this.userMapper = userMapper;
    }

    /**
     * 格式化API应用列表，适用于首页获取列表、搜索列表
     *
     * @param list   用来存放结果的变量
     * @param apiApp API应用对象
     */
    private void formatApiAppList(List<Map<String, Object>> list, ApiApp apiApp) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", apiApp.getId());
        map.put("title", apiApp.getTitle());
        map.put("smallTitle", apiApp.getSmallTitle());
        map.put("status", apiApp.getStatus());
        map.put("type", apiApp.getType());
        list.add(map);
    }

    @Resource
    private ApiLogManager apiLogManager;

    @Override
    public Map<String, Object> getApiList(Map<String, String> requestParam, HttpServletRequest request) {
        int pageSize = Tools.strToInt(requestParam.get("pageSize"));
        int page = Tools.strToInt(requestParam.get("page"));
        if (pageSize < 1) {
            pageSize = 30;
        }
        if (page < 1) {
            page = 1;
        }
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        int total = apiMapper.getApiListAllCount();
        for (ApiApp apiApp : apiMapper.getApiList(pageSize, Tools.getPageOffset(page, pageSize))) {
            formatApiAppList(list, apiApp);
        }
        response.put("total", total);
        response.put("list", list);
        return ResponseUtil.response(200, "获取成功", response);
    }

    @Override
    public Map<String, Object> createApi(Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            ApiApp apiApp = new ApiApp();
            int user_id = (int) request.getAttribute("userId");
            if (userMapper.checkUserIdExists(user_id) < 1) {
                return ResponseUtil.response(401, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
            }
            if (apiApp.getView_status() != 2) {
                // 检测是不是管理员
                int userMode = (int) request.getAttribute("userMode");
                if (userMode != Finals.Admin) {
                    return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_ADMIN);
                }
            }
            getRequestApiApp(requestBody, apiApp, user_id);
            Object param = requestBody.get("params");
            List<ApiParam> params = JSON.parseArray(JSON.toJSONString(param), ApiParam.class);
            List<ApiParam> uniqueParams = Tools.deleteDuplicatesList(params, ApiParam::getName);
            // 创建应用
            if (apiMapper.createApiApp(apiApp) > 0) {
                for (ApiParam apiParam : uniqueParams) {
                    apiParam.setApi_id(apiApp.getId());
                    if (apiMapper.createApiParam(apiParam) < 1) {
                        return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
                    }
                }
            }
            return ResponseUtil.success();
        } catch (Exception e) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM, e.getMessage());
        }
    }

    @Override
    public Map<String, Object> updateApi(String apiId, Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            int intApiId = Tools.strToInt(apiId);
            // 检查API是否存在
            if (apiMapper.checkApiExist(intApiId) < 1) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_API_NOT_FOUND);
            }

            // 检查是否是创建人或管理员
            int userId = (int) request.getAttribute("userId");
            if (userMapper.checkUserIdExists(userId) < 1) {
                return ResponseUtil.response(401, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
            }
            int userMode = (int) request.getAttribute("userMode");
            if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
                if (userMode != Finals.Admin) {
                    return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
                }
            }

            ApiApp apiApp = new ApiApp();
            // 校验审核状态
            if (apiApp.getView_status() != 2) {
                // 检测是不是管理员
                if (userMode != Finals.Admin) {
                    return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_ADMIN);
                }
            }
            apiApp.setId(intApiId);
            getRequestApiApp(requestBody, apiApp, userId);
            Object param = requestBody.get("params");
            List<ApiParam> params = JSON.parseArray(JSON.toJSONString(param), ApiParam.class);
            List<ApiParam> uniqueParams = Tools.deleteDuplicatesList(params, ApiParam::getName);
            // 更新应用
            if (apiMapper.updateApiApp(apiApp) > 0) {
                for (ApiParam apiParam : uniqueParams) {
                    apiParam.setApi_id(apiApp.getId());
                    // 检查参数是否存在
                    if (apiMapper.checkApiParamExistObj(apiParam) > 0) {
                        // 更新参数
                        if (apiMapper.updateApiParam(apiParam) < 1) {
                            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
                        }
                    } else {
                        // 创建参数
                        if (apiMapper.createApiParam(apiParam) < 1) {
                            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
                        }
                    }

                }
            }
            return ResponseUtil.success();
        } catch (Exception e) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);

        }
    }

    @Override
    public Map<String, Object> deleteApi(String apiId, HttpServletRequest request) {
        try {
            int intApiId = Tools.strToInt(apiId);
            // 检查API是否存在
            if (apiMapper.checkApiExist(intApiId) < 1) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_API_NOT_FOUND);
            }

            // 检查是否是创建人或管理员
            int userId = (int) request.getAttribute("userId");
            if (userMapper.checkUserIdExists(userId) < 1) {
                return ResponseUtil.response(401, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
            }
            if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
                int userMode = (int) request.getAttribute("userMode");
                if (userMode != Finals.Admin) {
                    return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
                }
            }


            apiMapper.deleteApiApp(intApiId);
            apiMapper.deleteApiParamAll(intApiId);
            apiMapper.deleteApiCount(intApiId);
            apiMapper.deleteApiLog(intApiId);
            return ResponseUtil.success();

        } catch (Exception e) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);

        }
    }

    @Override
    public Map<String, Object> searchApiList(Map<String, String> requestParam) {
        String keyword = requestParam.get("keyword");
        if (keyword == null || keyword.isEmpty()) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }
        int page = Tools.strToInt(requestParam.get("page"));
        int pageSize = Tools.strToInt(requestParam.get("pageSize"));
        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1) {
            pageSize = 30;
        }

        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        int total = apiMapper.searchApiListCount(keyword);
        for (ApiApp apiApp : apiMapper.searchApiList(keyword, pageSize, Tools.getPageOffset(page, pageSize))) {
            formatApiAppList(list, apiApp);
        }
        response.put("total", total);
        response.put("list", list);
        return ResponseUtil.success(response);
    }


    @Override
    public Map<String, Object> getApiApp(String apiId) {
        int intApiId = Tools.strToInt(apiId);
        ApiApp apiApp = apiMapper.getApiApp(intApiId);
        if (apiApp == null) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_API_NOT_FOUND);
        }
        List<ApiParam> apiParams = apiMapper.getApiParam(intApiId);
        Map<String, Object> map = new LinkedHashMap<>();
        List<Object> paramList = new ArrayList<>();
        map.put("id", apiApp.getId());
        map.put("title", apiApp.getTitle());
        map.put("smallTitle", apiApp.getSmallTitle());
        map.put("status", apiApp.getStatus());
        map.put("type", apiApp.getType());
        map.put("url", apiApp.getUrl());
        map.put("sendType", apiApp.getSendType());
        map.put("returnType", apiApp.getReturnType());
        map.put("returnContent", apiApp.getReturnContent());
        for (ApiParam apiParam : apiParams) {
            Map<String, Object> paramMap = new LinkedHashMap<>();
            paramMap.put("name", apiParam.getName());
            paramMap.put("required", apiParam.getRequired());
            paramMap.put("type", apiParam.getType());
            paramMap.put("msg", apiParam.getMsg());
            paramList.add(paramMap);
        }
        map.put("params", paramList);
        return ResponseUtil.success(map);
    }

    @Override
    public Map<String, Object> deleteApiParam(String apiId, Map<String, String> requestBody, HttpServletRequest request) {
        int intApiId = Tools.strToInt(apiId);
        String name = requestBody.get("name").trim();
        if (name.isEmpty()) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }
        // 检查API是否存在
        if (apiMapper.checkApiExist(intApiId) < 1) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_API_NOT_FOUND);
        }
        // 检查是否是创建人或管理员
        int userId = (int) request.getAttribute("userId");
        if (userMapper.checkUserIdExists(userId) < 1) {
            return ResponseUtil.response(401, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
        }
        if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
            int userMode = (int) request.getAttribute("userMode");
            if (userMode != Finals.Admin) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
            }
        }


        if (apiMapper.checkApiParamExist(intApiId, name) < 1) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM_NOT_FOUND);
        }
        apiMapper.deleteApiParam(intApiId, name);

        return ResponseUtil.success();
    }

    @Override
    public Map<String, Object> createApiKey(String finalApiID, Map<String, Object> requestBody, HttpServletRequest request) {
        int intApiId = Tools.strToInt(finalApiID);
        // 检查API是否存在
        if (apiMapper.checkApiExist(intApiId) < 1) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_API_NOT_FOUND);
        }
        // 检查是否是创建人或管理员
        int userId = (int) request.getAttribute("userId");
        if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
            if ((int) request.getAttribute("userMode") != Finals.Admin) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
            }
        }

        int type = (int) requestBody.get("type");
        long created = System.currentTimeMillis();
        long started, expired;
        try {
            started = Tools.timeStampEx(((Number) requestBody.getOrDefault("started", 0L)).longValue(), 0);
            expired = Tools.timeStampEx(((Number) requestBody.getOrDefault("expired", 0L)).longValue(), 0);
        } catch (Exception e) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }
        int count = (int) requestBody.get("count");


        // 校验数字长度
        if (String.valueOf(started).length() < 10 || String.valueOf(expired).length() < 10) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }

        if (started > expired || type < 0 || type > 1 || count < 0) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }

        // 生成密钥
        String key = Tools.getTextMd5(String.valueOf(intApiId) + System.currentTimeMillis());
        while (apiMapper.checkApiKeyExist(key) > 0) {
            key = Tools.getTextMd5(String.valueOf(intApiId) + System.currentTimeMillis());
        }

        apiMapper.createApiKey(intApiId, key, created, type, started, expired, count);

        return ResponseUtil.success(key);
    }

    @Override
    public Map<String, Object> getUserApiKeyList(String userId, Map<String, String> requestParam, HttpServletRequest request) {
        int intUserId = Tools.strToInt(userId);
        int tokenUserId = Tools.strToInt((String) request.getAttribute("tokenUserId"));
        if (tokenUserId != intUserId) {
            int userMode = (int) request.getAttribute("userMode");
            if (userMode != Finals.Admin) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
            }
        }

        // 检查用户是否存在
        if (userMapper.checkUserIdExists(intUserId) < 1) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
        }

        int pageSize = Tools.strToInt(requestParam.get("pageSize"));
        int page = Tools.strToInt(requestParam.get("page"));
        if (pageSize < 1) {
            pageSize = 30;
        }
        if (page < 1) {
            page = 1;
        }
        int total = apiMapper.getApiKeyListAllCount(intUserId);
        List<ApiKey> apiKeyList = apiMapper.getApiKeyList(intUserId, pageSize, Tools.getPageOffset(page, pageSize));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", total);
        result.put("list", apiKeyList);
        return ResponseUtil.success(result);
    }

    @Override
    public Map<String, Object> updateApiKey(String finalKey, Map<String, Object> requestBody, HttpServletRequest request) {

        // 检查密钥是否存在
        if (apiMapper.checkApiKeyExist(finalKey) < 1) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_KEY_NOT_FOUND);
        }

        // 检查是否是创建人或管理员
        int userId = (int) request.getAttribute("userId");
        int intApiId = apiMapper.getApiIdByKey(finalKey);
        if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
            if ((int) request.getAttribute("userMode") != Finals.Admin) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
            }
        }

        int type = (int) requestBody.get("type");
        long started, expired;
        try {
            started = Tools.timeStampEx(((Number) requestBody.getOrDefault("started", 0L)).longValue(), 0);
            expired = Tools.timeStampEx(((Number) requestBody.getOrDefault("expired", 0L)).longValue(), 0);
        } catch (Exception e) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }
        int count = (int) requestBody.get("count");


        // 校验数字长度
        if (String.valueOf(started).length() < 10 || String.valueOf(expired).length() < 10) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }

        if (started > expired || type < 0 || type > 1 || count < 0) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }

        apiMapper.updateApiKey(finalKey, type, started, expired, count);

        return ResponseUtil.success();
    }

    @Override
    public Map<String, Object> getApiKey(String finalKey, HttpServletRequest request) {
        // 检查密钥是否存在
        if (apiMapper.checkApiKeyExist(finalKey) < 1) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_KEY_NOT_FOUND);
        }

        // 检查是否是创建人或管理员
        int userId = (int) request.getAttribute("userId");
        int intApiId = apiMapper.getApiIdByKey(finalKey);
        if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
            if ((int) request.getAttribute("userMode") != Finals.Admin) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
            }
        }

        // 获取密钥详情
        ApiKey apiKey = apiMapper.getApiKey(finalKey);
        return ResponseUtil.success(apiKey);
    }

    @Override
    public Map<String, Object> deleteApiKey(String finalKey, HttpServletRequest request) {
        // 检查密钥是否存在
        if (apiMapper.checkApiKeyExist(finalKey) < 1) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_KEY_NOT_FOUND);
        }

        // 检查是否是创建人或管理员
        int userId = (int) request.getAttribute("userId");
        int intApiId = apiMapper.getApiIdByKey(finalKey);
        if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
            if ((int) request.getAttribute("userMode") != Finals.Admin) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
            }
        }

        // 删除密钥
        apiMapper.deleteApiKey(finalKey);

        return ResponseUtil.success();
    }

    @Override
    public Map<String, Object> resetApiKey(String finalKey, HttpServletRequest request) {
        // 检查密钥是否存在
        if (apiMapper.checkApiKeyExist(finalKey) < 1) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_KEY_NOT_FOUND);
        }

        // 检查是否是创建人或管理员
        int userId = (int) request.getAttribute("userId");
        int intApiId = apiMapper.getApiIdByKey(finalKey);
        if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
            if ((int) request.getAttribute("userMode") != Finals.Admin) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
            }
        }

        // 生成密钥
        String key = Tools.getTextMd5(String.valueOf(intApiId) + System.currentTimeMillis());
        while (apiMapper.checkApiKeyExist(key) > 0) {
            key = Tools.getTextMd5(String.valueOf(intApiId) + System.currentTimeMillis());
        }

        // 重置密钥
        apiMapper.resetApiKey(finalKey, key);

        return ResponseUtil.success(key);
    }

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
                if (System.currentTimeMillis() > apiKey.getExpired()) {
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

        String time = LocalDateTime.now().format(FORMATTER);
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
                time,
                apiRequestLog.getHeader(),
                apiRequestLog.getBody()
        );
        return ResponseUtil.success();
    }

    /**
     * 从请求体中获取API应用信息
     *
     * @param requestBody 请求体
     * @param apiApp      ApiApp对象
     */
    private void getRequestApiApp(Map<String, Object> requestBody, ApiApp apiApp, int userId) {
        apiApp.setTitle((String) requestBody.get("title"));
        apiApp.setSmallTitle((String) requestBody.get("smallTitle"));
        apiApp.setType((Integer) requestBody.get("type"));
        apiApp.setStatus((Integer) requestBody.get("status"));
        apiApp.setUrl((String) requestBody.get("url"));
        apiApp.setSendType((Integer) requestBody.get("sendType"));
        apiApp.setReturnType((String) requestBody.get("returnType"));
        apiApp.setReturnContent((String) requestBody.get("returnContent"));
        apiApp.setUser_id(userId);
        apiApp.setCreated(System.currentTimeMillis());
    }
}
