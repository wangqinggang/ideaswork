package cn.ideaswork.ideacoder.domain.lms.feedback;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {
  Feedback saveFeedback(Feedback feedback);

  List<Feedback> getAllFeedbacks();

  Feedback getFeedbackById(final String id);

  Feedback updateFeedbackById(Feedback feedback, final String id);

  void deleteFeedbackById(final String id);

  Boolean isFeedbackExist(final String id);

  Page<Feedback> getPageByCondition(FeedbackDTO feedbackDTO, Pageable pageable);

  List<Feedback> getListByCondition(FeedbackDTO feedbackDTO);
}
