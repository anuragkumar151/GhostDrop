package com.anurag.owlsend.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.anurag.owlsend.entity.Attachment;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;
    
    List<Attachment> getAllAttachments() throws Exception;
    
}
