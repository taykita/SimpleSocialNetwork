package source.file.store;

import java.io.IOException;
import java.io.InputStream;

public interface FileStoreClient {
    void saveImage(String fullName, InputStream image);

    byte[] loadImage(String fullName) throws IOException;
}
