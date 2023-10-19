package cn.ideaswork.ideacoder.domain.lms.section;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SectionService {
  Section saveSection(Section section);

  List<Section> getAllSections();

  Section getSectionById(final String id);

  Section updateSectionById(Section section, final String id);

  void deleteSectionById(final String id) throws TencentCloudSDKException;

  Boolean isSectionExist(final String id);

  Page<Section> getPageByCondition(SectionDTO sectionDTO, Pageable pageable);

  List<Section> getListByCondition(SectionDTO sectionDTO);
}
