package com.allinfinance.dev.ccs.dto;

/**
 * @author huanghf
 * @date 2021/12/24 10:07
 */
public class RolesQueryResponseDTO {
    private String roleId;

    private String roleName;

    private String isAvailable;

    private String weight;

    private String org;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    @Override
    public String toString() {
        return "RolesQueryResponseDTO{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", weight='" + weight + '\'' +
                ", org='" + org + '\'' +
                '}';
    }
}
