package com.yves.others.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class MenuDO {
    @Excel(name = "菜单ID", orderNum = "0", width = 30)
    private Integer menuId;

    @Excel(name = "菜单列表", orderNum = "1", width = 30)
    private String menuName;


}
