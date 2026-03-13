package ee.zxz.helloapi.controller;

import ee.zxz.helloapi.annotation.RequiresLogin;
import ee.zxz.helloapi.service.LinkService;
import ee.zxz.helloapi.utils.Finals;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(Finals.linkUrl)
public class LinkController {
    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    // GetLinkList - 获取友链列表 - GET
    @GetMapping("/")
    public Map<String, Object> GetLinkList(@RequestParam(required = false) Map<String, String> requestParam, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return linkService.getLinkList(requestParam, requestBody, request);
    }

    // CreateLink - 新增友链 - POST
    @PostMapping("/")
    @RequiresLogin(mode = Finals.Admin)
    public Map<String, Object> CreateLink(@RequestParam(required = false) Map<String, String> requestParam, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return linkService.createLink(requestParam, requestBody, request);
    }

    // UpdateLink - 更新友链 - PUT
    @PutMapping("/{link_id}")
    @RequiresLogin(mode = Finals.Admin)
    public Map<String, Object> UpdateLink(@PathVariable(required = false) String link_id, @RequestParam(required = false) Map<String, String> requestParam, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        String finalLinkId = (link_id == null) ? "0" : link_id;
        return linkService.updateLink(finalLinkId, requestParam, requestBody, request);
    }

    // DeleteLink - 删除友链 - DELETE
    @DeleteMapping("/{link_id}")
    @RequiresLogin(mode = Finals.Admin)
    public Map<String, Object> DeleteLink(@PathVariable(required = false) String link_id, @RequestParam(required = false) Map<String, String> requestParam, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        String finalLinkId = (link_id == null) ? "0" : link_id;
        return linkService.deleteLink(finalLinkId, requestParam, requestBody, request);
    }

    // GetLinkListSearch - 搜索友链列表 - GET
    @GetMapping("/Search")
    @RequiresLogin(mode = Finals.Admin)
    public Map<String, Object> GetLinkListSearch(@RequestParam(required = false) Map<String, String> requestParam, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return linkService.getLinkListSearch(requestParam, requestBody, request);
    }
}
