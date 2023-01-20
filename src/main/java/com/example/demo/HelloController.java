package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Locale;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.StorageSharedKeyCredential;

@RestController
public class HelloController {
	
	@GetMapping("/")
	public String hello() {
        return "welcome";
	}
	
	@GetMapping("/test")
	public String test() {
        String accountName = "<account name>";
        String accountKey = "<access key>";
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);
        String endpoint = String.format(Locale.ROOT, "https://%s.blob.core.windows.net", accountName);
        BlobServiceClient storageClient = new BlobServiceClientBuilder().endpoint(endpoint).credential(credential)
                .buildClient();
        BlobContainerClient blobContainerClient = storageClient.getBlobContainerClient("<blob container name>");
        blobContainerClient.createIfNotExists();
        BlockBlobClient blockBlobClient = blobContainerClient.getBlobClient("UTC=2022-12-22/2022-12-22.csv")
                .getBlockBlobClient();
        String data = "Hello ,world3, world4, world5";
        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(data.getBytes())) {
            blockBlobClient.upload(dataStream, data.length(), true);
            System.out.println("Completed");
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        
        return "Completed";
	}
	
}
