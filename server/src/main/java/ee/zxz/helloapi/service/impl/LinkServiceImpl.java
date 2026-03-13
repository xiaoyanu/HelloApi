package ee.zxz.helloapi.service.impl;

import ee.zxz.helloapi.domain.Link;
import ee.zxz.helloapi.mapper.LinkMapper;
import ee.zxz.helloapi.service.LinkService;
import ee.zxz.helloapi.utils.ResponseUtil;
import ee.zxz.helloapi.utils.Tools;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LinkServiceImpl implements LinkService {
    private final LinkMapper linkMapper;

    public LinkServiceImpl(LinkMapper linkMapper) {
        this.linkMapper = linkMapper;
    }

    @Override
    public Map<String, Object> getLinkList(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request) {
        int pageSize = Tools.strToInt(requestParam.get("pageSize"));
        int page = Tools.strToInt(requestParam.get("page"));
        boolean desc = Tools.strToInt(requestParam.get("desc")) >= 1;
        if (pageSize < 1) {
            pageSize = 30;
        }
        if (page < 1) {
            page = 1;
        }
        Map<String, Object> response = new LinkedHashMap<>();
        int total = linkMapper.getLinkListCount();
        List<Link> list = linkMapper.getLinkList(pageSize, Tools.getPageOffset(page, pageSize), desc);
        response.put("total", total);
        response.put("list", list);
        return ResponseUtil.response(200, "获取成功", response);
    }

    @Override
    public Map<String, Object> getLinkListSearch(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request) {
        String keyword = requestParam.get("keywords");
        int pageSize = Tools.strToInt(requestParam.get("pageSize"));
        int page = Tools.strToInt(requestParam.get("page"));
        if (pageSize < 1) {
            pageSize = 30;
        }
        if (page < 1) {
            page = 1;
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        // 获取总数量
        int total = linkMapper.getLinkListSearchCount(keyword);
        // 获取列表
        List<Link> linkList = linkMapper.getLinkListSearch(keyword, pageSize, Tools.getPageOffset(page, pageSize));
        resultMap.put("total", total);
        resultMap.put("list", linkList);
        return ResponseUtil.success(resultMap);
    }

    @Override
    public Map<String, Object> createLink(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request) {
        String name = requestBody.get("name");
        String url = requestBody.get("url");
        String desc = requestBody.get("desc");
        String avatar = requestBody.get("avatar");

        if (name == null || name.isEmpty()) {
            return ResponseUtil.response(400, "名称不能为空");
        }
        if (url == null || url.isEmpty()) {
            return ResponseUtil.response(400, "链接不能为空");
        }

        Link link = new Link();
        link.setName(name);
        link.setUrl(url);
        link.setDesc(desc);
        link.setAvatar(avatar);

        int result = linkMapper.insertLink(link);
        if (result > 0) {
            return ResponseUtil.response(200, "添加成功", link);
        } else {
            return ResponseUtil.response(500, "添加失败");
        }
    }

    @Override
    public Map<String, Object> updateLink(String finalLinkId, Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request) {
        int linkId = Tools.strToInt(finalLinkId);
        if (linkId <= 0) {
            return ResponseUtil.response(400, "无效的友链ID");
        }

        Link link = linkMapper.getLinkById(linkId);
        if (link == null) {
            return ResponseUtil.response(404, "友链不存在");
        }

        String name = requestBody.get("name");
        String url = requestBody.get("url");
        String desc = requestBody.get("desc");
        String avatar = requestBody.get("avatar");

        if (name != null) link.setName(name);
        if (url != null) link.setUrl(url);
        if (desc != null) link.setDesc(desc);
        if (avatar != null) link.setAvatar(avatar);

        int result = linkMapper.updateLink(link);
        if (result > 0) {
            return ResponseUtil.response(200, "修改成功", link);
        } else {
            return ResponseUtil.response(500, "修改失败");
        }
    }

    @Override
    public Map<String, Object> deleteLink(String finalLinkId, Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request) {
        int linkId = Tools.strToInt(finalLinkId);
        if (linkId <= 0) {
            return ResponseUtil.response(400, "无效的友链ID");
        }

        int result = linkMapper.deleteLink(linkId);
        if (result > 0) {
            return ResponseUtil.response(200, "删除成功");
        } else {
            return ResponseUtil.response(500, "删除失败或友链不存在");
        }
    }

}
