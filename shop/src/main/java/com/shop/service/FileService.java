package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        // 개체를 구별하기 위한 UUID
        UUID id = UUID.randomUUID();

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // UUID와 확장자를 조합해서 저장될 파일 이름 생성
        String savedFileName = id.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        // 파일이 저장될 위치와 파일명을 이용해 파일에 쓸 파일 출력 스트림을 생성한다.
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        // 파일을 쓴다.
        fos.write(fileData);

        // 객체 반환
        fos.close();

        return savedFileName;
    }


    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){ // 만약 파일이 있으면 해당 파일을 삭제한다.
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else{ // 파일이 없으면 로그만 띄우고 끝낸다.
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
