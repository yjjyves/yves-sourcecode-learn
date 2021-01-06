package com.yves.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.yves.model.Storage;

@Mapper
public interface StorageDao {
    int insert(@Param("storage") Storage storage);

    int insertSelective(@Param("storage") Storage storage);

    int insertList(@Param("storages") List<Storage> storages);

    int update(@Param("storage") Storage storage);

    Storage getByCommodityCode(String commodityCode);
}
