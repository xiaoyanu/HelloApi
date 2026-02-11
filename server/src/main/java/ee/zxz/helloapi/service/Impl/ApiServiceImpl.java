package ee.zxz.helloapi.service.Impl;

import ee.zxz.helloapi.service.ApiService;
import ee.zxz.helloapi.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ApiServiceImpl implements ApiService {

    @Override
    public Map<String, Object> getApiList(Map<String, String> requestParam, HttpServletRequest request) {

        return ResponseUtil.response(200, "获取成功");
    }
}
