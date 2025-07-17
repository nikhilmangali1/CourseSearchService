package com.nikhilmangali1.CourseSearchService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // 1. Create and configure the ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // 2. Create JacksonJsonpMapper using configured ObjectMapper
        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(objectMapper);

        // 3. Configure the transport and client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();
        RestClientTransport transport = new RestClientTransport(restClient, jacksonJsonpMapper);
        return new ElasticsearchClient(transport);
    }
}