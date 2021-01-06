package com.yves.others.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class MenuAccess {
    @Excel(name = "功能权限", orderNum = "0", width = 30)
    private String buttonName;

    @Excel(name = "操作描述", orderNum = "2", width = 30)
    private String remark;

    @Excel(name = "功能权限标识符", orderNum = "1", width = 30)
    private String firstIdentifier;

    @Excel(name = "操作标识符", orderNum = "3", width = 30)
    private String twoIdentifier;

    @Excel(name = "权限接口", orderNum = "4", width = 30)
    private String interfaceUrl;

    @Excel(name = "操作类型", orderNum = "5", width = 30)
    private String method;

    @Excel(name = "接口名称(可以不填)", orderNum = "6", width = 30)
    private String interfaceName;

    @Excel(name = "菜单", orderNum = "7", width = 30)
    private String menuName;

    @Excel(name = "权限项", orderNum = "0", width = 30)
    private String accessKeyValue;
}
