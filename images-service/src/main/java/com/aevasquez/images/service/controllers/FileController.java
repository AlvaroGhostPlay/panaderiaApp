package com.aevasquez.images.service.controllers;

import com.aevasquez.images.service.ImagesRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class FileController {
    private final String PUBLIC_DIR = "image/uploads/public/";
    private final String PRIVATE_DIR = "image/uploads/private/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("isPublic") boolean isPublic) throws IOException {

        String directory = isPublic ? PUBLIC_DIR : PRIVATE_DIR;
        Path filePath = Paths.get(directory + file.getOriginalFilename());

        // Crea los directorios si no existen
        Files.createDirectories(filePath.getParent());

        // Guarda el archivo en el disco
        Files.write(filePath, file.getBytes());

        return ResponseEntity.ok("Archivo subido con éxito: " + file.getOriginalFilename());
    }

    @GetMapping("/private/images")
    public ResponseEntity<?> getPrivateImage(
            @RequestParam String filename,
            @RequestHeader("Authorization") String token) {

        // 2. Si tiene permisos, le devuelves la imagen
        try {
            Path filePath = Paths.get("uploads/private/" + filename);
            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // O detectarlo dinámicamente
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/images")
    public ResponseEntity<?> getpubliImage(
            @RequestParam String filename) {

        try {

            Path filePath = Paths.get(PUBLIC_DIR)
                    .resolve(filename)
                    .normalize();

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/public/images/map")
    public ResponseEntity<?> getPublicImages(
            @RequestBody Map<String, String> images) {

        Map<String, String> imagesBase64 = images.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,

                        entry -> {
                            try {

                                String imageName = entry.getValue();

                                Path filePath = Paths.get(PUBLIC_DIR)
                                        .resolve(imageName)
                                        .normalize();

                                if (!Files.exists(filePath)) {
                                    return "";
                                }

                                byte[] fileContent =
                                        Files.readAllBytes(filePath);

                                return Base64.getEncoder()
                                        .encodeToString(fileContent);

                            } catch (IOException e) {
                                return "";
                            }
                        }
                ));

        return ResponseEntity.ok(imagesBase64);
    }
}
