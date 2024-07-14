package nl.fontys.s3.grp1.business;

import nl.fontys.s3.grp1.domain.File;
import nl.fontys.s3.grp1.domain.dto.file.CreateFileRequest;
import nl.fontys.s3.grp1.domain.dto.file.CreateFileResponse;
import nl.fontys.s3.grp1.domain.dto.file.GetAllFilesByStoryResponse;
import nl.fontys.s3.grp1.domain.dto.file.GetAllFilesResponse;

import java.io.IOException;
import java.util.Optional;

public interface FileService {
    CreateFileResponse createFile(CreateFileRequest request) throws IOException;
    GetAllFilesResponse getAllFiles();
    GetAllFilesByStoryResponse getAllFilesByStory(Long storyId);
    Optional<File> getFileById(Long id);
    void deleteFileById(Long id);
}
