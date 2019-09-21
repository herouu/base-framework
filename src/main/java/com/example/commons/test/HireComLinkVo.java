package com.example.commons.test;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *   实体Vo
 * </p>
 *
 * @auther fuqiang
 * @since  2019-09-20
 */
@Data
public class HireComLinkVo extends HireComLink {

    @ApiModelProperty("分页参数")
    private Integer pageNum = 1;
    @ApiModelProperty("分页参数")
    private Integer pageSize = 10;

    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("结束时间")
    private Date endTime;

}
