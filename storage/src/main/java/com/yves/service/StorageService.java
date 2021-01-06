package com.yves.service;


import com.yves.dao.StorageDao;
import com.yves.model.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageService {
    @Autowired
    private StorageDao storageDao;

    public void deduct(String commodityCode, int count) {
        Storage storage = storageDao.getByCommodityCode(commodityCode);
        if(storage == null){
            return;
        }
        storage.setCount(storage.getCount() - count);

        storageDao.update(storage);
    }

    public boolean insert(Storage storage) {
        return storageDao.insert(storage) > 0;
    }
}
