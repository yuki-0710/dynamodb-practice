package practice.document;
import java.io.File;
import java.util.Iterator;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MoviesLoadData {

    public static void main(String[] args) throws Exception {

        EndpointConfiguration configuration =
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2");

        AmazonDynamoDB client =
                AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(configuration).build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Movies");

        JsonParser parser = new JsonFactory().createParser(new File("moviedata.json"));
        JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();
        while (iter.hasNext()) {
            JsonNode currentNode = iter.next();
            int year = currentNode.path("year").asInt();
            String title = currentNode.path("title").asText();

            try {

                table.putItem(new Item()
                        .withPrimaryKey("year", year, "title", title)
                        .withJSON("info", currentNode.path("info").toString()));

                System.out.println("PutItem succeeded: " + year + " " + title);

            } catch (Exception e) {
                System.err.println("Unable to add movie: " + year + " " + title);
                System.err.println(e.getMessage());
                break;
            }
        }
        parser.close();
    }
}