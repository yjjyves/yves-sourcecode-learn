package com.yves.others;

import com.mongodb.client.result.UpdateResult;
import lombok.Data;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Map;

public class MongodbTest extends BaseTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 创建集合
     */
    @Test
    public void collTest() {
        //创建集合
        CollectionOptions collectionOptions = CollectionOptions.empty().size(102400L).maxDocuments(1000000L);
        mongoTemplate.createCollection("yvesTest", collectionOptions);
        //删除集合
        //mongoTemplate.dropCollection("collTest");
    }

    /**
     * 新增文档
     */
    @Test
    public void saveTest() {
        String jsonDoc = "{\n" +
                "      \"id\": 22,\n" +
                "      \"params\": {\n" +
                "        \"trans_type\": \"0\",\n" +
                "        \"file_type\": 0,\n" +
                "        \"file\": \"file248.txt\",\n" +
                "        \"md5\": \"ac4bbb8f8297b51d2013330dfa354e26\",\n" +
                "        \"size\": 47808\n" +
                "      },\n" +
                "      \"timestamp\": 1609120832882,\n" +
                "      \"version\": \"1.0.0\"\n" +
                "    }";
        mongoTemplate.save(jsonDoc, "yvesTest");
        //System.out.printf(result);
    }

    /**
     * 新增文档
     */
    @Test
    public void insertTest() {
        TestColl testColl = new TestColl();
        testColl.setAge(19);
        testColl.setName("yves");
        testColl.setSex("女");
        TestColl result = mongoTemplate.insert(testColl, "testColl");
        System.out.printf(result.toString());
    }

    /**
     * 更新文档
     */
    @Test
    public void updateTest() {
        Query query = new Query(Criteria.where("name").is("藏呜呜"));
        Update update = new Update().set("age", 99).set("sex", "男");
        //更新查询返回结果集的第一条
        UpdateResult result = mongoTemplate.updateFirst(query, update, "testColl");
        System.out.printf("匹配行数：" + result.getMatchedCount() + "修改行数：" + result.getModifiedCount());

        //更新查询返回结果集的所有
        mongoTemplate.updateMulti(query, update, "testColl");
    }

    /**
     * 删除文档
     */
    @Test
    public void deleteTestById() {
        Query query = new Query(Criteria.where("_id").is("5fea9b5e6e6dce42b2cf3171"));
        mongoTemplate.remove(query, MongodbTest.class);
    }

    /**
     * 查询文档
     */
    @Test
    public void findTestByName() {
        Query query = new Query(Criteria.where("name").is("yves"));
        TestColl mgt = mongoTemplate.findOne(query, TestColl.class, "testColl");
        System.out.printf(mgt.toString());
    }

    /**
     * count 查询
     */
    @Test
    public void countTest() {
        Query query = new Query();
        long result = mongoTemplate.count(query, TestColl.class, "testColl");
        System.out.printf(result + "");
    }

    /**
     * 分页查询
     */
    @Test
    public void pageTest() {
        int page = 0;
        int size = 10;
        page = (page - 1) < 0 ? 0 : page - 1;
        Query query = new Query().skip(page * size).limit(size);
        List<TestColl> list = mongoTemplate.find(query, TestColl.class, "testColl");
        long totalSize = mongoTemplate.count(new Query(), TestColl.class, "testColl");
        System.out.println("result:" + list.toString());
        System.out.println("count:" + totalSize);
    }


    /**
     * 排序查询
     */
    @Test
    public void sortTest() {
        Query query = new Query();
        query.with(Sort.by(new Sort.Order(Sort.Direction.DESC, "age")));
        List<TestColl> testColls = mongoTemplate.find(query, TestColl.class, "testColl");
        System.out.printf(testColls.toString());
    }

    /**
     * 聚合查询
     */
    @Test
    public void aggregationTest() {
        String alias = "nowNum";
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("name").is("yves")),
                Aggregation.group("age").count().as(alias)
        );
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "testColl", Map.class);
        List<Map> mappedResults = results.getMappedResults();
        if (mappedResults != null && mappedResults.size() > 0) {
            Integer num = (Integer) mappedResults.get(0).get(alias);
            System.out.printf("num:" + num);
        }
    }


    @Data
    public class TestColl {
        private String _id;
        private String name;
        private String sex;
        private int age;
    }

}
