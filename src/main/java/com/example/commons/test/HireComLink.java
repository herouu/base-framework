package com.example.commons.test;

import com.example.commons.annoation.DBTableName;
import com.example.commons.annoation.EntityUseCache;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 实体
 * </p>
 *
 * @auther fuqiang
 * @since 2019-09-20
 */
@Data
@DBTableName("hire_com_link")
@EntityUseCache(value = true)
public class HireComLink {

    private Long id;
    @ApiModelProperty("联融账号")
    private String account;
    @ApiModelProperty("it账号")
    private String itAccount;
    @ApiModelProperty("应用app_key")
    private String appKey;
    @ApiModelProperty("版本号")
    private String version;
    @ApiModelProperty("创建时间")
    private Date createdAt;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("更新时间")
    private Date updatedAt;
    @ApiModelProperty("更新人")
    private String updatedBy;
}
