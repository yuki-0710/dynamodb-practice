package practice.highlevel;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class MoviesItemOps01 {

    public static void main(String[] args) throws Exception {

        EndpointConfiguration configuration =
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2");

        AmazonDynamoDB client =
                AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(configuration).build();

        DynamoDBMapper mapper = new DynamoDBMapper(client);

        int year = 2015;
        String title = "The Big New Movie";

        MovieItem item = new MovieItem();
        item.setYear(year);
        item.setTitle(title);

        MovieItem.Info info = new MovieItem.Info();
        info.setPlot("Nothing happens at all.");
        info.setRating(0D);
        item.setInfo(info);

        try {

            System.out.println("Adding a new item...");

            mapper.save(item);

            System.out.println("PutItem succeeded:\n" + item);

        } catch (Exception e) {
            System.err.println("Unable to add item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}
