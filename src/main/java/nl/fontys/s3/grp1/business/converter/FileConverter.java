package nl.fontys.s3.grp1.business.converter;

import nl.fontys.s3.grp1.domain.File;
import nl.fontys.s3.grp1.persistence.entity.FileEntity;

import java.util.ArrayList;
import java.util.List;

public final class FileConverter {
    public static File convertFromFileEntity(FileEntity fileEntity) {
        return File.builder()
                .id(fileEntity.getId())
                .name(fileEntity.getFileName())
                .data(fileEntity.getData())
                .createdDate(fileEntity.getCreatedDate())
                .filetype(fileEntity.getFiletype())
                .build();
    }

    public static List<File> convertFromFileEntity(List<FileEntity> fileEntityList){
        List<File> newFiles = new ArrayList<>();

        fileEntityList.forEach(f-> newFiles.add(convertFromFileEntity(f)));

        return newFiles;
    }
}
