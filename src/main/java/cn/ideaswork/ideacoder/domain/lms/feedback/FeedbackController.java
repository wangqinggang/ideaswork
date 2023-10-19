package cn.ideaswork.ideacoder.domain.lms.feedback;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(tags = "反馈 API")
@CrossOrigin
@RequestMapping("/feedbacks")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @PostMapping
    @ApiOperation("添加反馈")
    public Feedback saveFeedback(@RequestBody Feedback feedback) {
        feedback.setId(UUID.randomUUID().toString());
        feedback.setCjsj(new Date());
        return feedbackService.saveFeedback(feedback);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("获取 反馈列表")
    public List<Feedback> getFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("根据主键获取一条 反馈")
    public Feedback getFeedback(@PathVariable("id") final String id) {
        return feedbackService.getFeedbackById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("根据主键更新反馈")
    public Feedback updateFeedback(@RequestBody Feedback feedback,
                                   @PathVariable("id") final String id) {
        return feedbackService.updateFeedbackById(feedback, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("根据主键 id 删除反馈")
    public void deleteFeedbackById(@PathVariable("id") final String id) {
        feedbackService.deleteFeedbackById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看反馈是否存在")
    public Boolean isExistFeedback(@PathVariable("id") final String id) {
        return feedbackService.isFeedbackExist(id);
    }

    @GetMapping("/getPageList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("分页条件查询")
    public Page<Feedback> getPageByCondition(FeedbackDTO feedbackDTO,
                                             @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return feedbackService.getPageByCondition(feedbackDTO, pageable);
    }
}