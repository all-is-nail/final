package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.Test;

import java.io.IOException;

public class ESOpsTest {

    public static final String HOST_NAME = "hk.k11p.cn";
    public static final int PORT = 19201;

    @Test
    public void testConnect() throws IOException {
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost(HOST_NAME, PORT)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        // Use the client...

        // Close the client, also closing the underlying transport object and network connections.
        esClient.close();
    }

    @Test
    public void testConnectWithAuth() throws IOException {
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "Elastic_DpG4Nc"));

        RestClientBuilder builder = RestClient.builder(
                        new HttpHost(HOST_NAME, PORT))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider));

        ElasticsearchTransport transport = new RestClientTransport(
                builder.build(), new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        // SearchResponse<Product> search = esClient.search(s -> s
        //                 .index("products")
        //                 .query(q -> q
        //                         .term(t -> t
        //                                 .field("name")
        //                                 .value(v -> v.stringValue("bicycle"))
        //                         )),
        //         Product.class);

        esClient.close();
    }
}
