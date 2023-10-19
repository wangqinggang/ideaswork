package cn.ideaswork.ideacoder.domain.vms.copy;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CopyService {
    Copy saveCopy(Copy copy);

    List<Copy> getAllCopys();

    Copy getCopyById(final String id);

    Copy updateCopyById(Copy copy, final String id) throws Exception;

    void deleteCopyById(final String id) throws Exception;

    Boolean isCopyExist(final String id);

    Page<Copy> getPageByCondition(CopyDTO copyDTO, Pageable pageable);

    List<Copy> getListByCondition(CopyDTO copyDTO);

    Integer getCopyNumByUserId(String id);

    Integer getActrualCopyCountByTopicId(String topicId);
}
