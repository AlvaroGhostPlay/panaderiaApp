package com.aevasquez.images.service;

import java.util.List;

public record ImagesRequest(
        List<String> images
) {
}
