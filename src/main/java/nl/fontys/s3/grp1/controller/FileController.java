package nl.fontys.s3.grp1.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.grp1.business.FileService;
import nl.fontys.s3.grp1.domain.File;
import nl.fontys.s3.grp1.domain.dto.file.CreateFileRequest;
import nl.fontys.s3.grp1.domain.dto.file.CreateFileResponse;
import nl.fontys.s3.grp1.domain.dto.file.GetAllFilesByStoryResponse;
import nl.fontys.s3.grp1.domain.dto.file.GetAllFilesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload/{storyId}")
    public ResponseEntity<CreateFileResponse> uploadFile(@PathVariable(value = "storyId") final Long storyId, @RequestParam("file") MultipartFile file) {
        try {
            CreateFileRequest createFileRequest = new CreateFileRequest(storyId, file);
            CreateFileResponse response = fileService.createFile(createFileRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/upload/multiple/{storyId}")
    public ResponseEntity<List<CreateFileResponse>> uploadMultipleFiles(@PathVariable(value = "storyId") final Long storyId, @RequestParam("files") List<MultipartFile> files) {
        List<CreateFileResponse> responses = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                CreateFileRequest createFileRequest = new CreateFileRequest(storyId, file);
                CreateFileResponse response = fileService.createFile(createFileRequest);
                responses.add(response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @GetMapping("{id}")
    public ResponseEntity<?> getFileById(@PathVariable(value = "id") Long id) {
        final File file = fileService.getFileById(id).get();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(file.getFiletype()))
                .body(file.getData());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFileById(@PathVariable(value = "id") Long id) {
        fileService.deleteFileById(id);
        return ResponseEntity.noContent().build();
    }
}
