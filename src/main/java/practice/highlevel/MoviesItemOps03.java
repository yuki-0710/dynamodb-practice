package practice.highlevel;

import java.util.Arrays;
import java.util.List;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

public class MoviesItemOps03 {

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

            List<MovieItem> itemList = mapper.query(MovieItem.class, queryExpression);
            for (MovieItem item : itemList) {

                MovieItem.Info info = item.getInfo();
                info.setActors(Arrays.asList("Larry", "Moe", "Curly"));
                info.setPlot("Everything happens all at once.");
                info.setRating(5.5D);

                System.out.println("Updating the item...");

                mapper.save(item);

                System.out.println("UpdateItem succeeded:\n" + item);
            }

        } catch (Exception e) {
            System.err.println("Unable to read item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}
