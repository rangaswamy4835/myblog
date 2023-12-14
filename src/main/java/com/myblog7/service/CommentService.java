package com.myblog7.service;

import com.myblog7.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);//once i supply the Dto to it and the postId two things will go postId and the comment for that post save the comment.
    List<CommentDto> getCommentByPostId(long postId);


    CommentDto getCommentsById(Long postId, Long commentId);

    List<CommentDto> getAllCommentsById();

    void deleteCommentById(long postId, long commentId);
}

