package com.example.misson2.file;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 업로드 파일 정보 보관
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UploadFile {

    // 고객이 업로드한 파일명
    private String uploadFileName;

    // 서버 내부에서 관리하는 파일명
    private String storeFileName;
}
