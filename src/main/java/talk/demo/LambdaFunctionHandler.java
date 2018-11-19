package talk.demo;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class LambdaFunctionHandler implements RequestHandler<S3Event, Void> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

    @Override
    public Void handleRequest(S3Event event, Context context) {
        String bucketName = event.getRecords().get(0).getS3().getBucket().getName();
        String objectKey = event.getRecords().get(0).getS3().getObject().getKey();
        
        S3ObjectInputStream objectContent = s3.getObject(new GetObjectRequest(bucketName, objectKey)).getObjectContent();
        
        FlatFileItemReader<ClientInfo> reader = new BatchFileMapper().reader(objectContent);
        reader.open(new ExecutionContext());
        
        ClientInfo clientInfo;
        
        try {
			while ((clientInfo = reader.read()) != null) {
				System.out.println(clientInfo.toString());
			}
        } catch (FlatFileParseException e) {
        	System.out.println("Linha " + e.getLineNumber() + " incorreta: " + e.getInput());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
        
        return null;
    }
    
}