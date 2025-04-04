package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import junit.framework.Assert;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ESOpsTest {

    public static final String HOST_NAME = "localhost";
    public static final int PORT = 9200;

    /**
     * 索引名称
     */
    public static final String INDEX = "books";

    /**
     * 测试连接
     *
     * @throws IOException
     */
    @Test
    public void testConnect() throws IOException {

        // URL and API key
        String serverUrl = "http://localhost:9200";
        String apiKey = "VnVhQ2ZHY0JDZGJrU...";

        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        // Use the client...

        // Close the client, also closing the underlying transport object and network
        // connections.
        esClient.close();
    }


    /**
     * 获取客户端
     *
     * @return
     */
    private ElasticsearchClient getClient() {
        // URL and API key
        String serverUrl = "http://localhost:9200";
        String apiKey = "VnVhQ2ZHY0JDZGJrU...";

        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        return esClient;
    }

    /**
     * 创建索引
     *
     * @throws IOException
     */
    @Test
    public void createIndex() throws IOException {
        ElasticsearchClient client = getClient();
        client.indices().create(c -> c
                .index(INDEX));
        client.close();
    }

    /**
     * 检查索引是否存在
     *
     * @throws IOException
     */
    @Test
    public void checkIndexExists() throws IOException {
        ElasticsearchClient client = getClient();
        boolean exists = client.indices().exists(c -> c.index(INDEX)).value();
        Assert.assertTrue(exists);
        client.close();
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    @Test
    public void deleteIndex() throws IOException {
        ElasticsearchClient client = getClient();
        client.indices().delete(c -> c.index(INDEX));
        client.close();
    }

    /**
     * 查询索引内容
     *
     * @throws IOException
     */
    @Test
    public void searchIndex() throws IOException {
        ElasticsearchClient client = getClient();
        // 查询索引内容
        SearchResponse<Book> response = client.search(s -> s
                .index(INDEX)
                .query(q -> q.matchAll(m -> m)), Book.class);
        response.hits().hits().forEach(h -> System.out.println(h.source()));
        client.close();
    }

    /**
     * 获取文档
     *
     * @throws IOException
     */
    @Test
    public void getSingleDocument() throws IOException {
        ElasticsearchClient client = getClient();
        GetResponse<Book> response = client.get(g -> g.index(INDEX).id(String.valueOf(1)), Book.class);
        if (response.found()) {
            Book book = response.source();
            Assert.assertNotNull("fail to get the target doc", book);
        } else {
            System.out.println("the doc is not found");
        }
        client.close();
    }

    /**
     * 查询所有文档
     *
     * @throws IOException
     */
    @Test
    public void getDocumentsBySize() throws IOException {
        ElasticsearchClient client = getClient();
        
        // 设置查询参数，获取所有文档
        SearchResponse<Book> response = client.search(s -> s
                .index(INDEX)
                .query(q -> q.matchAll(m -> m))
                .size(1000), // 设置较大的size以获取更多文档
                Book.class);
        
        // 输出查询结果
        System.out.println("Total hits: " + response.hits().total().value());
        System.out.println("Documents found:");
        
        List<Book> books = new ArrayList<>();
        response.hits().hits().forEach(hit -> {
            Book book = hit.source();
            books.add(book);
            System.out.println("ID: " + hit.id() + ", Book: " + book);
        });
        
        Assert.assertFalse("No documents found in index", books.isEmpty());
        client.close();
    }

    /**
     * 添加文档
     *
     * @throws IOException
     */
    @Test
    public void addDocument() throws IOException {
        ElasticsearchClient client = getClient();
        // 判断索引是否存在
        boolean exists = client.indices().exists(c -> c.index(INDEX)).value();
        if (!exists) {
            createIndex();
        }
        // 添加文档
        Book book = new Book(1,
                "Java编程思想",
                "Bruce Eckel",
                100,
                new Date(),
                "计算机语言",
                "计算机",
                "人民邮电出版社");
        client.index(i -> i
                .index(INDEX)
                .id(String.valueOf(book.getId()))
                .document(book));
        client.close();
    }

    /**
     * 更新文档
     *
     * @throws IOException
     */
    @Test
    public void updateDocument() throws IOException {
        ElasticsearchClient client = getClient();
        // 更新文档
        Book book = new Book(1,
                "Java编程思想",
                "Bruce Eckel",
                100,
                new Date(),
                "计算机语言",
                "计算机",
                "人民邮电出版社");
        UpdateResponse<Book> response = client.update(u -> u
                        .index(INDEX)
                        .id(String.valueOf(book.getId()))
                        .doc(book),
                Book.class);

        System.out.println(response);
        client.close();
    }

    /**
     * 删除文档
     *
     * @throws IOException
     */
    @Test
    public void deleteDocument() throws IOException {
        ElasticsearchClient client = getClient();
        // 删除文档
        DeleteResponse response = client.delete(d -> d
                .index(INDEX)
                .id(String.valueOf(1)));
        System.out.println(response);
        client.close();
    }

    /**
     * 删除所有文档
     *
     * @throws IOException
     */
    @Test
    public void deleteAllDocuments() throws IOException {
        ElasticsearchClient client = getClient();
        
        // 使用DeleteByQuery删除所有文档
        client.deleteByQuery(d -> d
                .index(INDEX)
                .query(q -> q
                        .matchAll(m -> m)
                )
        );
        
        // 验证删除结果
        SearchResponse<Book> response = client.search(s -> s    
                .index(INDEX)
                .query(q -> q.matchAll(m -> m))
                .size(1000),
                Book.class);
        
        System.out.println("Documents remaining after deletion: " + response.hits().total().value());
        Assert.assertEquals("All documents should be deleted", 0, response.hits().total().value());
        
        client.close();
    }

    /**
     * 批量添加文档
     *
     * @throws IOException
     */
    @Test
    public void bulkAddDocuments() throws IOException {
        ElasticsearchClient client = getClient();

        // 准备批量文档
        Book book1 = new Book(1, "Spring实战", "Craig Walls", 90, new Date(), "框架", "计算机", "人民邮电出版社");
        Book book2 = new Book(2, "MySQL技术内幕", "姜承尧", 80, new Date(), "数据库", "计算机", "机械工业出版社");
        Book book3 = new Book(3, "深入理解Java虚拟机", "周志明", 85, new Date(), "Java", "计算机", "机械工业出版社");
        Book book4 = new Book(4, "Spring Boot实战", "Spring Boot实战", 95, new Date(), "框架", "计算机", "机械工业出版社");
        Book book5 = new Book(5, "Spring Cloud实战", "Spring Cloud实战", 100, new Date(), "框架", "计算机", "机械工业出版社");
        Book book6 = new Book(6, "Spring Security实战", "Spring Security实战", 105, new Date(), "框架", "计算机", "机械工业出版社");
        Book book7 = new Book(7, "Spring Data实战", "Spring Data实战", 110, new Date(), "框架", "计算机", "机械工业出版社");
        Book book8 = new Book(8, "Spring Batch实战", "Spring Batch实战", 115, new Date(), "框架", "计算机", "机械工业出版社");
        Book book9 = new Book(9, "Redis实战", "Redis实战", 120, new Date(), "框架", "计算机", "机械工业出版社");
        Book book10 = new Book(10, "MongoDB实战", "MongoDB实战", 125, new Date(), "框架", "计算机", "机械工业出版社");

        // Create a list to store the books
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        books.add(book5);
        books.add(book6);
        books.add(book7);
        books.add(book8);
        books.add(book9);
        books.add(book10);

        // 批量添加
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (Book book : books) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(INDEX)
                            .id(String.valueOf(book.getId()))
                            .document(book)
                    )
            );
        }

        client.bulk(br.build());

        client.close();
    }


    /**
     * 批量删除文档
     *
     * @throws IOException
     */
    @Test
    public void bulkDeleteDocuments() throws IOException {
        ElasticsearchClient client = getClient();
        // 创建要删除的ID列表
        List<Integer> idsToDelete = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 创建批量请求构建器
        BulkRequest.Builder br = new BulkRequest.Builder();

        // 遍历ID列表，为每个ID添加删除操作
        for (Integer id : idsToDelete) {
            br.operations(op -> op
                    .delete(d -> d
                            .index(INDEX)
                            .id(String.valueOf(id))
                    )
            );
        }

        // 执行批量删除
        client.bulk(br.build());
        client.close();
    }

}