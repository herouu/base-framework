package com.example.commons.entity;

import com.example.commons.annoation.DBColumnInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {
    @ApiModelProperty("主键ID")
    @DBColumnInfo(length = 11)
    private Long id;
    @ApiModelProperty("创建人")
    @DBColumnInfo(length = 64)
    private String createdBy;
    @ApiModelProperty("创建时间")
    @DBColumnInfo
    private Date createdAt;
    @ApiModelProperty("更新人")
    @DBColumnInfo(length = 64)
    private String updatedBy;
    @ApiModelProperty("更新时间")
    @DBColumnInfo
    private Date updatedAt;
    @ApiModelProperty("版本号")
    @DBColumnInfo(length = 11, DEFAULT = "1")
    private Integer version;
}
