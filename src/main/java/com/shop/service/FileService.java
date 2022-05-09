package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * 파일 처리
 */
@Service
@Log
public class FileService {

    //파일 업로드
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID(); //Universally Unique Identifier 서로 다른 개체 구별하기위해 이름을 부여할 때 사용(파일명 중복 해결)
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension; // + 확장자 =저장될 파일 이름
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); //FileOutputStream클래스는 바이트 단위의 출력을 내보내는 클래스, 생성자로 파일이 저장될 위치와 파일의 이름을 넘겨 파일 출력 스트림 만든다.
        fos.write(fileData); //fileData를 파일 출력 스트림에 입력
        fos.close();
        return savedFileName; //업로드된 파일 이름 반환
    }

    //삭제
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath); //파일이 저장된 경로를 이용해 파일 객체 생성
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}