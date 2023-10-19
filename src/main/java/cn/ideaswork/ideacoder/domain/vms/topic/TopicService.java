package cn.ideaswork.ideacoder.domain.vms.topic;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicService {
    Topic saveTopic(Topic topic);

    List<Topic> getAllTopics();

    Topic getTopicById(final String id);

    Topic updateTopicById(Topic topic, final String id);

    void deleteTopicById(final String id);

    Boolean isTopicExist(final String id);

    Page<Topic> getPageByCondition(TopicDTO topicDTO, Pageable pageable);

    List<Topic> getListByCondition(TopicDTO topicDTO);

    Integer getTopicNumByUserId(String id);

    Integer checktopicCount(String userId);
}
