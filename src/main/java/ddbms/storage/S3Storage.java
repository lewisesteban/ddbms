package ddbms.storage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3Storage {
	private static String bucketName = "oha.you";
	private static String key        = "georges.jpg";
  private static String uploadFileName = "georges.jpg";

	public static void getObject(String[] args) throws IOException {
        BasicAWSCredentials creds = new BasicAWSCredentials("access_key", "secret_key");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).build();
        try {
            if (args.length > 0) {
              key = args[0];
            }
            System.out.println("Downloading an object");
            S3Object s3object = s3Client.getObject(new GetObjectRequest(
            		bucketName, key));
            System.out.println("Content-Type: "  +
            		s3object.getObjectMetadata().getContentType());
            displayTextInputStream(s3object.getObjectContent());

           // Get a range of bytes from an object.

            GetObjectRequest rangeObjectRequest = new GetObjectRequest(
            		bucketName, key);
            rangeObjectRequest.setRange(0, 10);
            S3Object objectPortion = s3Client.getObject(rangeObjectRequest);

            System.out.println("Printing bytes retrieved.");
            displayTextInputStream(objectPortion.getObjectContent());

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
            		" means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
            		" the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private static void displayTextInputStream(InputStream input)
    throws IOException {
    	// Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new
        		InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }

	public static String UploadObject(File file, String extension) throws IOException {
    BasicAWSCredentials creds = new BasicAWSCredentials("a;f", "wq+sdf");
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion("sdkl").withCredentials(new AWSStaticCredentialsProvider(creds)).build();
        try {
            key = String.format("%s.%s", new SimpleDateFormat("ddMMyyyy").format(new Date()),
                              new Random().nextInt(9));
            key += extension;
            System.out.println("Uploading a new object to S3 from a file\n");

            s3Client.putObject(new PutObjectRequest(
            		                 bucketName, key, file));
            return key;
         } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            throw ase;
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            throw ace;
        }
    }
}
