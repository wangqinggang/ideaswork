package cn.ideaswork.ideacoder.domain.lms.comment;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Api(tags = "评论 API")
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping
    @ApiOperation("添加评论")
    public Comment saveComment(@RequestBody Comment comment) {
        comment.setPlsj(LocalDate.now());
        comment.setId(UUID.randomUUID().toString());
        return commentService.saveComment(comment);
    }

    @GetMapping
    @ApiOperation("获取 评论列表")
    public List<Comment> getComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 评论")
    public Comment getComment(@PathVariable("id") final String id) {
        return commentService.getCommentById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新评论")
    public Comment updateComment(@RequestBody Comment comment, @PathVariable("id") final String id) {
        return commentService.updateCommentById(comment, id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除评论")
    public void deleteCommentById(@PathVariable("id") final String id) {
        commentService.deleteCommentById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看评论是否存在")
    public Boolean isExistComment(@PathVariable("id") final String id) {
        return commentService.isCommentExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Comment> getPageByCondition(CommentDTO commentDTO,
                                            @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return commentService.getPageByCondition(commentDTO, pageable);
    }
}