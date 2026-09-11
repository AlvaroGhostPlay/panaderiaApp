package com.aevasquez.images.service.dto;

import java.util.List;

public record ImagesRequest(
        List<String> images
) {
}
