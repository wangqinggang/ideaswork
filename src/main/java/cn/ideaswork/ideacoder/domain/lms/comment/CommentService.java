package cn.ideaswork.ideacoder.domain.lms.comment;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Comment saveComment(Comment comment);

    List<Comment> getAllComments();

    Comment getCommentById(final String id);

    Comment updateCommentById(Comment comment, final String id);

    void deleteCommentById(final String id);

    Boolean isCommentExist(final String id);

    Page<Comment> getPageByCondition(CommentDTO commentDTO, Pageable pageable);

    List<Comment> getListByCondition(CommentDTO commentDTO);
}
