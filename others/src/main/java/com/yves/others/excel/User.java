package com.yves.others.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class User {

    /**
     * 用户名
     */
    @Excel(name = "用户名", orderNum = "0", width = 30)
    private String username;

    /**
     * 姓名
     */
    @Excel(name = "姓名", orderNum = "1", width = 30)
    private String name;

    /**
     * 年龄
     */
    @Excel(name = "年龄", orderNum = "2", width = 30)
    private Integer age;

    /**
     * 性别,0表示男，1表示女
     */
    @Excel(name = "性别", orderNum = "3", width = 30,replace = {"男_0", "女_1"})
    private String sex;

    /**
     * 籍贯
     */
    @Excel(name = "籍贯", orderNum = "4", width = 30)
    private String address;
    //对于时间，需要指定导出的格式：exportFormat="yyyy-MM-dd HH:mm:ss"
}