package nl.fontys.s3.grp1.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.grp1.business.FileService;
import nl.fontys.s3.grp1.business.converter.FileConverter;
import nl.fontys.s3.grp1.business.exception.FileTooLargeException;
import nl.fontys.s3.grp1.business.exception.InvalidFileException;
import nl.fontys.s3.grp1.business.exception.UnsupportedMediaTypeException;
import nl.fontys.s3.grp1.domain.File;
import nl.fontys.s3.grp1.domain.dto.file.CreateFileRequest;
import nl.fontys.s3.grp1.domain.dto.file.CreateFileResponse;
import nl.fontys.s3.grp1.domain.dto.file.GetAllFilesByStoryResponse;
import nl.fontys.s3.grp1.domain.dto.file.GetAllFilesResponse;
import nl.fontys.s3.grp1.persistence.FileRepository;
import nl.fontys.s3.grp1.persistence.StoryRepository;
import nl.fontys.s3.grp1.persistence.entity.FileEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    private FileRepository fileRepository;
    private StoryRepository storyRepository;
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "application/pdf");

    @Override
    public CreateFileResponse createFile(CreateFileRequest request) throws IOException {
        MultipartFile file = request.getFile();

        if (file.isEmpty()) {
            throw new InvalidFileException("EMPTY_FILE");
        }
        if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
            throw new UnsupportedMediaTypeException();
        }
        if (file.getSize() > 4L * 1024 * 1024 * 1024) {
            throw new FileTooLargeException();
        }

        FileEntity fileEntity = FileEntity.builder()
                .fileName(file.getOriginalFilename())
                .data(file.getBytes())
                .createdDate(LocalDateTime.now())
                .filetype(file.getContentType())
                .storyEntity(storyRepository.findById(request.getStoryId()).get())
                .build();

        FileEntity savedFileEntity = fileRepository.save(fileEntity);

        return CreateFileResponse.builder()
                .file(FileConverter.convertFromFileEntity(savedFileEntity))
                .build();
    }

    @Override
    public GetAllFilesResponse getAllFiles() {
        List<FileEntity> results = fileRepository.findAll();
        List<File> fileList = convertResults(results);

        return GetAllFilesResponse.builder().fileList(fileList).build();
    }

    @Override
    public GetAllFilesByStoryResponse getAllFilesByStory(Long storyId) {
        List<FileEntity> results = fileRepository.findAllByStoryEntity_Id(storyId);
        List<File> fileList = convertResults(results);

        return GetAllFilesByStoryResponse.builder().fileList(fileList).build();
    }

    @Override
    public Optional<File> getFileById(Long id) {
        return fileRepository.findById(id).map(FileConverter::convertFromFileEntity);
    }

    @Override
    public void deleteFileById(Long id) {
        fileRepository.deleteById(id);
    }

    private List<File> convertResults(List<FileEntity> results) {
        return results
                .stream()
                .map(FileConverter::convertFromFileEntity)
                .toList();
    }
}

