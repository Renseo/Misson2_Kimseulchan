package com.example.misson2.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

// 파일 저장과 관련된 업무 처리
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return "%s%s".formatted(fileDir, filename);
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    // 서버 내부에서 관리하는 파일명을 유일한 이름을 생성하는 UUID를 사용해서 충돌하지 않도록 한다.
    private String createStoreFileName(String originalFilename) {
        String ext = extraExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return "%s.%s".formatted(uuid, ext);
    }

    // 확장자를 별로도 추출해서 서버 내부에서 관리하는 파일명에도 붙여준다.
    // 예) a.png -> 51041c62-86e4-4274-801d-614a7d994edb.png 와 같이 저장한다.
    private String extraExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
