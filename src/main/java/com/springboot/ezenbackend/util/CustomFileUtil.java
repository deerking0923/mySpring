package com.springboot.ezenbackend.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {
    
    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init(){
        File temFolder = new File(uploadPath);

        if(temFolder.exists() == false){
            temFolder.mkdir();
        }
        
        uploadPath = temFolder.getAbsolutePath();

        log.info("--------------------------------");
        log.info(uploadPath);
    }
    

    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException{

        if(files == null || files.size() == 0){
            return List.of();
        }

        List<String> uploadNames = new ArrayList<>();

        for(MultipartFile multipartFile : files){
            //다른 값과 중복되지 않는 유니크한 값을 생성할 때 사용하는 난수 생성기. (중복 확률이 매우 희박함)
            String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

            //MultipartFile 인터페이스
            //getName() 넘어온 파라미터명
            //getOriginalFileName 업로드 파일명
            //getContentType 파일의 ContentType
            //isEmpty() 비어있냐
            //getSize() byte 사이즈
            //transferTo() 파일 저장
            //getInputStream() 파일의 내용을 읽기 위한 InputStream변환 


            //저장할 파일의 경로 uploadPath 내가 저장할 파일의 이름 savedName
            Path savePath = Paths.get(uploadPath, savedName);

            try {
                //muitipartFile 입력 스트림으로 파일의 내용을 읽음. savePath에 복사함
                //업로드한 파일을 저장 경로에 복사해서 저장
                Files.copy(multipartFile.getInputStream(), savePath);

                String contentType = multipartFile.getContentType();

                //썸네일 생성하는 코드
                if(contentType != null && contentType.startsWith("image")){
                    Path thumnailPath = Paths.get(uploadPath,"s_"+savedName);
                    Thumbnails.of(savePath.toFile())
                    .size(200,200).toFile(thumnailPath.toFile());
                }
                uploadNames.add(savedName);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return uploadNames;
     }



    //파일 조회
    //Resource 스프링 프레임워크에서 제공하는 추상화된 리소스 인터페이스, 다양한 유형의 리소스를 표현할 수 있다.
    public ResponseEntity<Resource> getFile(String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        
        /*isReadable() : 리소스를 읽을 수 있는지를 확인
         * isFile() : 리소스가 파일인지 확인
         * isOpen() : 리소스가 열려있는지 확인
         * getDescription() : 전체 경로 포함한 파일 이름 또는 실제 URL
         */
        
        if(!resource.isReadable()) {
           //uploadPath + File.separator + "123.jpg" 이 경로의 파일을 가져오는 역할. File.separator는 /를 의미
           resource = new FileSystemResource(uploadPath + File.separator + "123.jpg");
        }
        
        //HttpHeaders 를 이욯해서 해당 파일의 MIME 유형을 추가
        //heders 를 통해서 MIME 유형을 추가해 주지 않으면 클라이언트가 서버로 전송하는 데이터의 형식을 정학히 인식할 수 X
        //-> 파일이나 이미지가 깨짐
        HttpHeaders headers = new HttpHeaders();
        
        try {
           headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        }catch (Exception e) {
           return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
     }
     
     public void deleteFiles(List<String> fileNames) {
        if(fileNames == null || fileNames.size() == 0) {
           return;
        }
        
        fileNames.forEach(fileName -> {
           
           String thumbnailFileName = "s_" + fileNames;
           Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
           Path filePath = Paths.get(uploadPath, fileName);
           
           try {
              Files.deleteIfExists(filePath);
              Files.deleteIfExists(thumbnailPath);
           }catch(IOException e) {
              throw new RuntimeException(e.getMessage());
           }
           
           
        });
     }
 

}
