package ee.zxz.helloapi.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface LinkService {
    Map<String, Object> getLinkList(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    Map<String, Object> getLinkListSearch(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    Map<String, Object> createLink(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    Map<String, Object> updateLink(String finalLinkId, Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    Map<String, Object> deleteLink(String finalLinkId, Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);
}
