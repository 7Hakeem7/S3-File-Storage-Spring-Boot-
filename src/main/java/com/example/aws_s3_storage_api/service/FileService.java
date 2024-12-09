package com.example.aws_s3_storage_api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public interface FileService {
    List<String> searchFiles(String userName, String searchTerm);
    byte[] downloadFile(String userName, String fileName);
}

@Service
class FileServiceImpl implements FileService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public List<String> searchFiles(String userName, String searchTerm) {
        String folderPath = userName + "/";
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(folderPath);
        ListObjectsV2Result result = amazonS3.listObjectsV2(request);

        return result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .filter(key -> key.contains(searchTerm))
                .collect(Collectors.toList());
    }

    @Override
    public byte[] downloadFile(String userName, String fileName) {
        try {
            // Construct the key
            String key = userName + "/" + fileName;
            System.out.println("Constructed key for S3: " + key);

            // Fetch the object from S3
            S3Object s3Object = amazonS3.getObject(bucketName, key);

            // Read and return file content
            return s3Object.getObjectContent().readAllBytes();

        } catch (AmazonS3Exception e) {
            if ("NoSuchKey".equals(e.getErrorCode())) {
                throw new RuntimeException("File not found in S3: " + userName + "/" + fileName, e);
            } else {
                throw new RuntimeException("S3 error while accessing file: " + userName + "/" + fileName, e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file content from S3 for key: " + userName + "/" + fileName, e);
        }
    }

}
