package com.example.commons.test;

import com.example.commons.entity.WhereCondition;
import com.example.commons.enums.DbOperatorType;
import com.example.commons.service.CommonsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @auther fuqiang
 * @since 2019-09-20
 */
@Service
public class HireComLinkService {

    @Autowired
    CommonsService commonsService;

    public List<HireComLink> getHireComLinkList(Long hireComLinkId) {
        WhereCondition condition = new WhereCondition();
        condition.addCondition("", DbOperatorType.EQ, hireComLinkId);
        condition.addOrderBy("id desc");
        List<HireComLink> hireComLinkList = commonsService.selectByCondition(HireComLink.class, condition);
        return hireComLinkList;
    }

    public PageInfo<HireComLink> getHireComLinkPageList(HireComLinkVo hireComLinkVo) {
        WhereCondition condition = new WhereCondition();
        condition.addOrderBy("id desc");
        PageHelper.startPage(hireComLinkVo.getPageNum(), hireComLinkVo.getPageSize());
        List<HireComLink> hireComLinkList = commonsService.selectByCondition(HireComLink.class, condition);
        return new PageInfo<>(hireComLinkList);
    }

    public HireComLink getHireComLinkById(Long hireComLinkId) {
        return commonsService.selectById(HireComLink.class, hireComLinkId);
    }

    public HireComLink addHireComLink(HireComLink hireComLink) {
        return commonsService.insertData(hireComLink);
    }

    public void deleteHireComLinkById(Long hireComLinkId) {
        commonsService.deleteById(HireComLink.class, hireComLinkId);
    }

    public void updateHireComLink(HireComLink hireComLink) {
        commonsService.updateExcludeNull(hireComLink);
    }

}