package com.aevasquez.msvc.nadbar.controllers;

import com.aevasquez.msvc.nadbar.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/getByContentType")
    public ResponseEntity<?> getContentsByContentType(@RequestParam("contentType") String contentType){
        return ResponseEntity.ok(contentService.getContentsByTypeContent(contentType));
    }
}
