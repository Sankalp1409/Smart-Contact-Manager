package com.scm.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.scm.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile picture, String filename) {
        try {
            byte[] data = new byte[picture.getInputStream().available()];
            picture.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id", filename));
            return this.getUrlFromPublicId(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String getUrlFromPublicId(String id) {
        return cloudinary.url().generate(id);
    }

}
