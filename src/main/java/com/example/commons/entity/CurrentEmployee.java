package com.example.commons.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PanYe on 2016/3/3.
 */
public class CurrentEmployee {
    private Integer id;
    private Integer orgId;
    private Integer postId;
    private String empAccount;
    private String empName;
    private String empPwd;
    private String empPwdSalt;
    private String empPhone;
    private String empEmail;
    private Boolean isEnabled;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer version;
    private String orgName;
    private String orgCode;
    private String roleName;
    private Map<String, String> permissions;
    private Boolean isManagement;
    private Integer documentaryNum;
    private Map<String, String> positions;
    private Map<String, TodoInfo> todoMap;
    private String appKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getEmpAccount() {
        return empAccount;
    }

    public void setEmpAccount(String empAccount) {
        this.empAccount = empAccount;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpPwd() {
        return empPwd;
    }

    public void setEmpPwd(String empPwd) {
        this.empPwd = empPwd;
    }

    public String getEmpPwdSalt() {
        return empPwdSalt;
    }

    public void setEmpPwdSalt(String empPwdSalt) {
        this.empPwdSalt = empPwdSalt;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Map<String, String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, String> permissions) {
        this.permissions = permissions;
    }

    public Boolean getManagement() {
        return isManagement;
    }

    public void setManagement(Boolean management) {
        isManagement = management;
    }

    public Integer getDocumentaryNum() {
        return documentaryNum;
    }

    public void setDocumentaryNum(Integer documentaryNum) {
        this.documentaryNum = documentaryNum;
    }

    public Map<String, String> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, String> positions) {
        this.positions = positions;
    }

    public Map<String, TodoInfo> getTodoMap() {
        return todoMap == null ? new HashMap<String, TodoInfo>() : todoMap;
    }

    public void setTodoMap(Map<String, TodoInfo> todoMap) {
        this.todoMap = todoMap;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
