package com.yves.others.excel;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExcelImport {

    @Test
    public void importExcel() {
        String filePath = "D://paas30.xlsx";

        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        params.setStartSheetIndex(0);

        int accessMaxId = 500;
        int buttonMaxId = 500;

        try {
            List<MenuAccess> objects = ExcelUtils.importExcel(filePath, params, MenuAccess.class);

            Map<String, Integer> buttonMap = new HashMap<>();
            Map<String, Integer> accessMap = new HashMap<>();
            Map<String, Boolean> buttonAccessMap = new HashMap<>();
            Map<String, Boolean> menuButtonMap = new HashMap<>();
            System.err.println("" + JSONObject.toJSONString(objects));

            ImportParams params1 = new ImportParams();
            params1.setTitleRows(0);
            params1.setHeadRows(1);
            params1.setStartSheetIndex(1);
            List<MenuDO> menuDOS = ExcelUtils.importExcel(filePath, params1, MenuDO.class);
            System.err.println("" + JSONObject.toJSONString(objects));
            Map<String, MenuDO> menuIdMap = menuDOS.stream().collect(Collectors.toMap(MenuDO::getMenuName, a -> a, (a, b) -> b));

            if (objects != null && !objects.isEmpty()) {
                for (MenuAccess menuAccess : objects) {
                    if (menuAccess == null || menuAccess.getFirstIdentifier() == null) {
                        continue;
                    }
                    //按钮
                    String buttonName = menuAccess.getButtonName() + "-" + (menuAccess.getInterfaceName() == null ? menuAccess.getRemark() : menuAccess.getInterfaceName());
                    String buttonValue = menuAccess.getFirstIdentifier() + ":" + menuAccess.getTwoIdentifier();
                    if (!buttonMap.containsKey(buttonValue)) {
                        StringBuilder stringBuilder2 = new StringBuilder("insert into sys_button(button_id,button_name, button_value,create_user) values(" + buttonMaxId + ",\"" +
                                buttonName + "\",\"" +
                                buttonValue + "\",\"1\");");
                        //System.err.println(stringBuilder2.toString());
                        buttonMap.put(buttonValue, buttonMaxId);
                    }

                    //权限项
                    String accessName = buttonName;
                    String method = menuAccess.getMethod().toUpperCase();
                    String accessValue = getAccessValue(menuAccess.getInterfaceUrl()) + ":" + (menuAccess.getAccessKeyValue() == null ? getAccessValueKey(menuAccess.getInterfaceUrl()) : menuAccess.getAccessKeyValue());
                    if (!accessMap.containsKey(accessValue)) {
                        StringBuilder stringBuilder1 = new StringBuilder("insert into sys_right_access(access_id,access_name, access_url, access_value,request_method,create_user) values(" + accessMaxId + ",\"" +
                                accessName + "\",\"" +
                                "/paas-business" + menuAccess.getInterfaceUrl() + "\",\"" +
                                accessValue + "\",\"" + method + "\",\"1\");");
                        System.err.println(stringBuilder1.toString());
                        accessMap.put(accessValue, accessMaxId);
                    }

                    //按钮-权限项,1-> n
                    if (!buttonAccessMap.containsKey(buttonValue + accessValue)) {
                        StringBuilder stringBuilder3 = new StringBuilder("insert into sys_button_menu_access(button_id,access_id, create_user) values(" + buttonMap.get(buttonValue) + "," +
                                accessMap.get(accessValue) + ",1);");
                        //System.err.println(stringBuilder3.toString());
                        buttonMap.put(buttonValue + accessValue, accessMaxId);
                    }

                    //菜单-按钮
                    Integer menuId = Optional.ofNullable(menuIdMap.get(menuAccess.getMenuName())).map(i -> i.getMenuId()).orElse(null);
                    if (menuId != null && !menuButtonMap.containsKey(menuId + buttonValue) && menuId != null) {
                        StringBuilder stringBuilder4 = new StringBuilder("insert into sys_menu_button(button_id,menu_id, create_user) values(" + buttonMap.get(buttonValue) + "," +
                                menuId + ",1);");
                        //System.err.println(stringBuilder4.toString());
                        menuButtonMap.put(menuId + buttonValue, true);
                    }

                    accessMaxId++;
                    buttonMaxId++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String url = "/dataAnalysis/timer/timerLogList";
        System.err.println("getAccessValue " + getAccessValue(url));

        String url2 = "v1/productManage/product/add";
        System.err.println("getAccessValue2 " + getAccessValueKey(url2));
    }

    private static String getAccessValue(String interfaceUrl) {
        String url = interfaceUrl.replace("/v1/", "").replace("v1/", "");
        if (url.startsWith("/")) {
            url = url.replaceFirst("/", "");
        }
        int index = url.indexOf("/");

        return url.substring(0, (index == -1) ? url.length() - 1 : index);
    }


    private static String getAccessValueKey(String interfaceUrl) {
        String url = interfaceUrl.replace("/v1/", "").replace("v1/", "");
        int index = url.lastIndexOf("/");

        return index < 0 ? url : url.substring(index + 1);
    }
}
