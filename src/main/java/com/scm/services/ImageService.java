package com.scm.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {

    String uploadImage(MultipartFile picture, String filename);

    String getUrlFromPublicId(String id);

}
