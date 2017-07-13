package practice.highlevel;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

public class MoviesQuery {

    public static void main(String[] args) throws Exception {

        EndpointConfiguration configuration =
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2");

        AmazonDynamoDB client =
                AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(configuration).build();

        DynamoDBMapper mapper = new DynamoDBMapper(client);

        MovieItem hashKey1 = new MovieItem();
        hashKey1.setYear(1985);

        DynamoDBQueryExpression<MovieItem> queryExpression1 = new DynamoDBQueryExpression<MovieItem>()
                .withHashKeyValues(hashKey1);

        try {
            System.out.println("Movies from 1985");

            PaginatedQueryList<MovieItem> queryList = mapper.query(MovieItem.class, queryExpression1);
            for (MovieItem item : queryList) {
                System.out.println(item);
            }

        } catch (Exception e) {
            System.err.println("Unable to query movies from 1985");
            System.err.println(e.getMessage());
        }

        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.BETWEEN)
                .withAttributeValueList(new AttributeValue("A"), new AttributeValue("L"));

        MovieItem hashKey2 = new MovieItem();
        hashKey2.setYear(1992);

        DynamoDBQueryExpression<MovieItem> queryExpression2 = new DynamoDBQueryExpression<MovieItem>()
                .withHashKeyValues(hashKey2)
                .withRangeKeyCondition("title", rangeKeyCondition);

        try {

            System.out.println("Movies from 1992 - titles A-L, with genres and lead actor");

            PaginatedQueryList<MovieItem> queryList = mapper.query(MovieItem.class, queryExpression2);
            for (MovieItem item : queryList) {
                System.out.println(item);
            }

        } catch (Exception e) {
            System.err.println("Unable to query movies from 1992:");
            System.err.println(e.getMessage());
        }
    }
}
