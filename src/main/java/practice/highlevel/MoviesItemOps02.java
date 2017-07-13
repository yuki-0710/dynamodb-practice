package practice.highlevel;

import java.util.List;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

public class MoviesItemOps02 {

    public static void main(String[] args) throws Exception {

        EndpointConfiguration configuration =
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2");

        AmazonDynamoDB client =
                AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(configuration).build();

        DynamoDBMapper mapper = new DynamoDBMapper(client);

        int year = 2015;
        String title = "The Big New Movie";

        MovieItem key = new MovieItem();
        key.setYear(year);
        key.setTitle(title);

        DynamoDBQueryExpression<MovieItem> queryExpression =
                new DynamoDBQueryExpression<MovieItem>().withHashKeyValues(key);

        try {

            System.out.println("Attempting to read the item...");

            List<MovieItem> itemList = mapper.query(MovieItem.class, queryExpression);
            for (MovieItem item : itemList) {
                System.out.println("GetItem succeeded: " + item);
            }

        } catch (Exception e) {
            System.err.println("Unable to read item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}
