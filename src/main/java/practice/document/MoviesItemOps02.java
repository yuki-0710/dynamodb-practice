package practice.document;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

public class MoviesItemOps02 {

    public static void main(String[] args) throws Exception {

        EndpointConfiguration configuration =
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2");

        AmazonDynamoDB client =
                AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(configuration).build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Movies");

        int year = 2015;
        String title = "The Big New Movie";
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("year", year, "title", title);

        try {

            System.out.println("Attempting to read the item...");

            Item outcome = table.getItem(spec);

            System.out.println("GetItem succeeded: " + outcome);

        } catch (Exception e) {
            System.err.println("Unable to read item: " + year + " " + title);
            System.err.println(e.getMessage());
        }

    }
}