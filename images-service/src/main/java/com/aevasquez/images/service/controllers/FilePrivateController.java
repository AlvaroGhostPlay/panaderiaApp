package com.aevasquez.images.service.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/private")
public class FilePrivateController {
    private final String PRIVATE_DIR = "image/uploads/private/";
    private final String PUBLIC_DIR = "image/uploads/public/";


    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/images")
    public ResponseEntity<?> getpubliImage(
            @RequestParam String filename) {

        try {

            Path filePath = Paths.get(PRIVATE_DIR)
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

    @PostMapping("/products-image-map")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> getImagesByUrlImage(
            @RequestBody Map<Object, String> images) {

        Map<Object, String> imagesBase64 = images.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,

                        entry -> {
                            try {

                                String imageName = entry.getValue();

                                Path filePath = Paths.get(PRIVATE_DIR)
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
