package com.groupn.demo;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class SwkomApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	private MinioClient minioClient;
	private JdbcTemplate jdbcTemplate;
	private RestClient restClient;
	@AfterEach
	public void tearDown() throws Exception {
		// Close the RestClient after the test
		if (restClient != null) {
			restClient.close();
		}
	}
	@Test // Test if the UI Works (Test the Homepage)
	public void testHomePageIsUp() throws Exception {
		mockMvc.perform(get("http://localhost"))
				.andExpect(status().isOk());  // Expect a 200 OK response for the homepage
	}

	@Test // Test if getAll Path of PaperlessRest Returns Documents
	public void testGetAllDocuments() throws Exception {
		mockMvc.perform(get("/api/documents"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").isNotEmpty());  // Expect non-empty JSON response
	}

	@Test // Test to Delete a Document
	public void testDeleteDocument() throws Exception {
		// Assume a document with ID "1002" exists
		mockMvc.perform(delete("/api/documents/{id}", "1002"))
				.andExpect(status().isOk());  // Expect a 200 OK on successful delete
	}

	@Test // Test if MinIO is Workings
	public void testMinIOIsWorking() throws Exception {
		String bucketName = "documents";
		boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

		assertTrue(bucketExists);  // Check if the MinIO bucket exists
	}

	@Test // Test if PostgreSQL is Working
	public void testPostgreSQLIsWorking() throws Exception {
		ResultSet resultSet = jdbcTemplate.queryForObject("SELECT 1", ResultSet.class);
		assertNotNull(resultSet);  // If we can query the database, it's working
	}

	@Test // Test if Adminer is Working
	public void testAdminerIsWorking() throws Exception {
		mockMvc.perform(get("http://localhost:9091"))
				.andExpect(status().isOk());  // Expect Adminer to be reachable on port 9091
	}

	@Test // Test if RabbitMQ is Working
	public void testRabbitMQIsWorking() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setPort(5672);
		Connection connection = factory.newConnection();

		assertNotNull(connection);  // RabbitMQ connection should be established
		connection.close();
	}

	@Test // Test if Grafana is Working
	public void testGrafanaIsWorking() throws Exception {
		mockMvc.perform(get("http://localhost:3000"))
				.andExpect(status().isOk());  // Expect Grafana to be reachable on port 3000
	}

}
