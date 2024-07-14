package nl.fontys.s3.grp1.business.impl;

import nl.fontys.s3.grp1.business.exception.FileTooLargeException;
import nl.fontys.s3.grp1.business.exception.InvalidFileException;
import nl.fontys.s3.grp1.business.exception.UnsupportedMediaTypeException;
import nl.fontys.s3.grp1.domain.dto.file.CreateFileRequest;
import nl.fontys.s3.grp1.domain.dto.file.CreateFileResponse;
import nl.fontys.s3.grp1.domain.dto.file.GetAllFilesByStoryResponse;
import nl.fontys.s3.grp1.domain.dto.file.GetAllFilesResponse;
import nl.fontys.s3.grp1.persistence.FileRepository;
import nl.fontys.s3.grp1.persistence.StoryRepository;
import nl.fontys.s3.grp1.persistence.entity.FileEntity;
import nl.fontys.s3.grp1.persistence.entity.StoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


class FileServiceTest {
    @Mock
    private FileRepository fileRepository;

    @Mock
    private StoryRepository storyRepository;

    @InjectMocks
    private FileServiceImpl fileServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFile_Success() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        CreateFileRequest request = new CreateFileRequest(1L, mockFile);

        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(1024L);
        when(storyRepository.findById(anyLong())).thenReturn(Optional.of(new StoryEntity()));
        when(fileRepository.save(any(FileEntity.class))).thenReturn(new FileEntity());

        CreateFileResponse response = fileServiceImpl.createFile(request);

        assertNotNull(response);
        verify(fileRepository, times(1)).save(any(FileEntity.class));
    }

    @Test
    void testCreateFile_Failure_EmptyFile() {
        MultipartFile mockFile = mock(MultipartFile.class);
        CreateFileRequest request = new CreateFileRequest(1L, mockFile);

        when(mockFile.isEmpty()).thenReturn(true);

        assertThrows(InvalidFileException.class, () -> {
            fileServiceImpl.createFile(request);
        });
        verify(fileRepository, never()).save(any(FileEntity.class));
    }

    @Test
    void testCreateFile_Failure_UnsupportedMediaType() {
        MultipartFile mockFile = mock(MultipartFile.class);
        CreateFileRequest request = new CreateFileRequest(1L, mockFile);

        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getContentType()).thenReturn("text/plain");

        assertThrows(UnsupportedMediaTypeException.class, () -> {
            fileServiceImpl.createFile(request);
        });
        verify(fileRepository, never()).save(any(FileEntity.class));
    }

    @Test
    void testCreateFile_Failure_FileTooLarge() {
        MultipartFile mockFile = mock(MultipartFile.class);
        CreateFileRequest request = new CreateFileRequest(1L, mockFile);

        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(5L * 1024 * 1024 * 1024); // 5 GB

        assertThrows(FileTooLargeException.class, () -> {
            fileServiceImpl.createFile(request);
        });
        verify(fileRepository, never()).save(any(FileEntity.class));
    }

    @Test
    void testGetAllFiles() {
        when(fileRepository.findAll()).thenReturn(List.of(new FileEntity()));

        GetAllFilesResponse response = fileServiceImpl.getAllFiles();

        assertNotNull(response);
        assertEquals(1, response.getFileList().size());
    }

    @Test
    void testGetFilesByStoryId_Success() {
        when(storyRepository.findById(1L)).thenReturn(Optional.of(new StoryEntity()));
        when(fileRepository.findAllByStoryEntity_Id(1L)).thenReturn(List.of(new FileEntity()));

        GetAllFilesByStoryResponse response = fileServiceImpl.getAllFilesByStory(1L);

        assertNotNull(response);
        assertEquals(1, response.getFileList().size());
    }

    @Test
    void testDeleteFile_Success() {
        doNothing().when(fileRepository).deleteById(anyLong());

        fileServiceImpl.deleteFileById(1L);

        verify(fileRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteFile_Failure_FileNotFound() {
        doThrow(new IllegalArgumentException("File not found")).when(fileRepository).deleteById(anyLong());

        assertThrows(IllegalArgumentException.class, () -> {
            fileServiceImpl.deleteFileById(1L);
        });
    }
}

