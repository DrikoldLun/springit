package com.lunz.springit.service;

import com.lunz.springit.domain.Comment;
import com.lunz.springit.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment){
        commentRepository.save(comment);
        return comment;
    }
}
