package com.example.todayshouse.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Slf4j(topic = "AwsS3Util")
@Component
@RequiredArgsConstructor
public class AwsS3Util {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.maxsize}")
    private Long maxsize;

    public String uploadImgFile(MultipartFile multipartFile, String dirName) {
        validateFileSize(multipartFile);

        String imgFileName = dirName + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        ObjectMetadata objectMeta = new ObjectMetadata();

        try {
            objectMeta.setContentLength(multipartFile.getInputStream().available());
            amazonS3.putObject(bucket, imgFileName, multipartFile.getInputStream(), objectMeta);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }

        URL url = amazonS3.getUrl(bucket, imgFileName);
        return url.toString();
    }

    private void validateFileSize(MultipartFile multipartFile) {
        if(multipartFile.getSize() > maxsize) {
            throw new MultipartException("용량 초과입니다.(최대 3MB까지 가능합니다.)");
        }
    }

}
