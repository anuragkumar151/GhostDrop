package com.anurag.owlsend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anurag.owlsend.entity.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, String> {
}
