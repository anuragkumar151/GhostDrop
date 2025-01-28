package com.deka.owlsend.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deka.owlsend.ResponseData;
import com.deka.owlsend.entity.Attachment;
import com.deka.owlsend.service.AttachmentService;

@RestController
@CrossOrigin(origins = "http://localhost:3000") 
public class AttachmentController {

    private AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file")MultipartFile file) throws Exception {
        Attachment attachment = null;
        String downloadURl = "";
        attachment = attachmentService.saveAttachment(file);
        downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(attachment.getId())
                .toUriString();

        return new ResponseData(attachment.getFileName(),
                downloadURl,
                file.getContentType(),
                file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = null;
        attachment = attachmentService.getAttachment(fileId);
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }
    
    @GetMapping("/images")
    public ResponseEntity<List<ResponseData>> getAllImages() throws Exception {
        List<Attachment> attachments = attachmentService.getAllAttachments();
        List<ResponseData> responseDataList = attachments.stream()
                .map(attachment -> {
                    String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/download/")
                            .path(attachment.getId())
                            .toUriString();
                    return new ResponseData(attachment.getFileName(),
                            downloadURL,
                            attachment.getFileType(),
                            attachment.getData().length);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDataList);
    }
    
}
