package org.example;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

public class Version6ESOpsTest {

    public static final String HOST_NAME = "us.k11p.cn";
    public static final int PORT = 19200;
    public static final String INDEX = "books";

    /**
     * 测试连接
     *
     * @throws IOException
     */
    @Test
    public void testConnect() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(HOST_NAME, PORT, "http")));

        client.close();
    }


    /**
     * 获取客户端
     *
     * @return
     */
    private RestHighLevelClient getClient() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(HOST_NAME, PORT, "http")));
    }

    /**
     * 创建索引
     *
     * @throws IOException
     */
    @Test
    public void createIndex() throws IOException {
        RestHighLevelClient client = getClient();
        // 创建索引
        CreateIndexRequest request = new CreateIndexRequest(INDEX);
        client.indices().create(request, RequestOptions.DEFAULT);
        client.close();
    }

    /**
     * 检查索引是否存在
     *
     * @throws IOException
     */
    @Test
    public void checkIndexExists() throws IOException {
        RestHighLevelClient client = getClient();
        GetIndexRequest request = new GetIndexRequest();
        request.indices(INDEX);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("Index exists: " + exists);
        client.close();
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    @Test
    public void deleteIndex() throws IOException {
        RestHighLevelClient client = getClient();
        client.indices().delete(new DeleteIndexRequest("books"), RequestOptions.DEFAULT);
        client.close();
    }

    /**
     * 查询索引内容
     *
     * @throws IOException
     */
    @Test
    public void searchIndex() throws IOException {
        RestHighLevelClient client = getClient();
        // 查询索引内容
        SearchRequest searchRequest = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse);
        client.close();
    }

    /**
     * 添加文档
     *
     * @throws IOException
     */
    @Test
    public void addDocument() throws IOException {
        RestHighLevelClient client = getClient();
        // 判断索引是否存在
        GetIndexRequest request = new GetIndexRequest();
        request.indices("books");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        if (!exists) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("books");
            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        }
        // 添加文档
        client.index(new IndexRequest("books").type("_doc").id("1").source("name", "Java编程思想"), RequestOptions.DEFAULT);
        client.close();
    }

}