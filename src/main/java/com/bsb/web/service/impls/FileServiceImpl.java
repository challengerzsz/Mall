package com.bsb.web.service.impls;

import com.bsb.util.FtpUtil;
import com.bsb.web.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author zeng
 */
@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private FtpUtil ftpUtil;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *
     * @param file
     * @param path 获取servlet上下文得出的路径 webapp下目录
     * @return
     */
    public String upload(MultipartFile file, String path) {

        String fileName = file.getOriginalFilename();
        //扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件，上传文件名：{}, 上传的路径:{}, 新文件名:{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            // 文件上传成功 已经上传至webapp下
            List<File> fileList = new ArrayList<>();
            fileList.add(targetFile);

            ftpUtil.uploadFile(fileList);
            //已经上传至ftp

            // 删除webapp下文件
            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }

        return targetFile.getName();
    }
}
