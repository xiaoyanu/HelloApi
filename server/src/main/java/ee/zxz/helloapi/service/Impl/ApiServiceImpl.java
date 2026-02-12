package ee.zxz.helloapi.service.Impl;

import com.alibaba.fastjson.JSON;
import ee.zxz.helloapi.domain.ApiApp;
import ee.zxz.helloapi.domain.ApiParam;
import ee.zxz.helloapi.mapper.ApiMapper;
import ee.zxz.helloapi.mapper.UserMapper;
import ee.zxz.helloapi.service.ApiService;
import ee.zxz.helloapi.utils.Finals;
import ee.zxz.helloapi.utils.ResponseUtil;
import ee.zxz.helloapi.utils.Tools;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApiServiceImpl implements ApiService {
    private final ApiMapper apiMapper;
    private final UserMapper userMapper;

    public ApiServiceImpl(ApiMapper apiMapper, UserMapper userMapper) {
        this.apiMapper = apiMapper;
        this.userMapper = userMapper;
    }

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
        List<Map<String, Object>> list = new ArrayList<>();
        for (ApiApp apiApp : apiMapper.getApiList(pageSize, Tools.getPageOffset(page, pageSize))) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", apiApp.getId());
            map.put("title", apiApp.getTitle());
            map.put("smallTitle", apiApp.getSmallTitle());
            map.put("status", apiApp.getStatus());
            map.put("type", apiApp.getType());
            list.add(map);
        }
        return ResponseUtil.response(200, "获取成功", list);
    }

    @Override
    public Map<String, Object> createApi(Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            ApiApp apiApp = new ApiApp();
            int user_id = (int) request.getAttribute("userId");
            if (userMapper.checkUserIdExists(user_id) < 1) {
                return ResponseUtil.response(401, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
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
            if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
                int userMode = (int) request.getAttribute("userMode");
                if (userMode < 1) {
                    if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
                        return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
                    }
                }
            }

            ApiApp apiApp = new ApiApp();
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
                if (userMode < 1) {
                    if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
                        return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
                    }
                }
            }


            apiMapper.deleteApiApp(intApiId);
            apiMapper.deleteApiParamAll(intApiId);
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

        List<ApiApp> apiAppList = apiMapper.searchApiList(keyword, pageSize, Tools.getPageOffset(page, pageSize));
        return ResponseUtil.success(apiAppList);
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
            if (userMode < 1) {
                if (apiMapper.checkApiCreator(intApiId, userId) < 1) {
                    return ResponseUtil.response(400, Finals.MESSAGES_ERROR_NOT_CREATOR);
                }
            }
        }



        if (apiMapper.checkApiParamExist(intApiId, name) < 1) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM_NOT_FOUND);
        }
        apiMapper.deleteApiParam(intApiId, name);

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
