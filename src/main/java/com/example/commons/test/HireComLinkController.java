package com.example.commons.test;

import com.example.commons.controller.BaseController;
import com.example.commons.entity.JsonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统管理-用户管理
 * </p>
 *
 * @auther fuqiang
 * @since 2019-09-20
 */
@Api(tags = "系统管理-用户管理")
@RequestMapping("/b/system")
@RestController
public class HireComLinkController extends BaseController {

    @Autowired
    private HireComLinkService hireComLinkService;

    @ApiOperation(value = "系统管理 - 获取用户管理分页列表", response = HireComLink.class)
    @ApiImplicitParam(name = "token", value = "Token", dataType = "string", paramType = "query")
    @RequestMapping(path = "/get/hireComLink/page/list", method = RequestMethod.POST)
    public JsonResponse getHireComLinkPageList(@RequestBody HireComLinkVo hireComLinkVo) {
        return jsonData(hireComLinkService.getHireComLinkPageList(hireComLinkVo));
    }

    @ApiOperation(value = "系统管理 - 获取用户管理列表", response = HireComLink.class)
    @ApiImplicitParam(name = "token", value = "Token", dataType = "string", paramType = "query")
    @RequestMapping(path = "/get/hireComLink/list", method = RequestMethod.GET)
    public JsonResponse getHireComLinkList(@RequestParam Long hireComLinkId) {
        return jsonData(hireComLinkService.getHireComLinkList(hireComLinkId));
    }

    @ApiOperation(value = "系统管理 - 获取用户管理", response = HireComLink.class)
    @ApiImplicitParam(name = "token", value = "Token", dataType = "string", paramType = "query")
    @RequestMapping(path = "/get/hireComLink", method = RequestMethod.GET)
    public JsonResponse getHireComLinkById(@RequestParam Long hireComLinkId) {
        return jsonData(hireComLinkService.getHireComLinkById(hireComLinkId));
    }


}