package com.bsb.web.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author zeng
 */
public interface IFileService {

    String upload(MultipartFile file, String path);

}
