package com.yves;

import com.yves.model.Storage;
import com.yves.service.StorageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StorageServiceTest extends BaseTest {
    @Autowired
    private StorageService storageService;

    @Test
    public void insert_test(){
        Storage storage = new Storage();
        storage.setCount(100);
        storage.setCommodityCode("1001");

        storageService.insert(storage);
    }
}
