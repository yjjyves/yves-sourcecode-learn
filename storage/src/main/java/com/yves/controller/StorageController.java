package com.yves.controller;

import com.yves.feign.StorageFeign;
import com.yves.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage/*")
public class StorageController implements StorageFeign {

    @Autowired
    private StorageService storageService;

    @RequestMapping("/deduct")
    @Override
    public void deduct(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count) {
        storageService.deduct(commodityCode, count);
    }
}
