package source.file.store;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;

@Repository
public class S3Client implements FileStoreClient {
    @Autowired
    public S3Client(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private final AmazonS3 amazonS3;

    @Value("${s3Client.bucket.name}")
    private String bucket;

    @Override
    public void saveImage(String fullName, InputStream image) {
        amazonS3.putObject(
                bucket,
                fullName,
                image,
                new ObjectMetadata()
        );
    }

    @Override
    public byte[] loadImage(String fullName) throws IOException {
        return IOUtils.toByteArray(
                amazonS3.getObject(
                bucket,
                fullName)
                .getObjectContent()
                .getDelegateStream());
    }
}
