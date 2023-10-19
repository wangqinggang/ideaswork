package cn.ideaswork.ideacoder.domain.pm.idea;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IdeaService {
  Idea saveIdea(Idea idea);

  List<Idea> getAllIdeas();

  Idea getIdeaById(final String id);

  Idea updateIdeaById(Idea idea, final String id);

  void deleteIdeaById(final String id);

  Boolean isIdeaExist(final String id);

  Page<Idea> getPageByCondition(IdeaDTO ideaDTO, Pageable pageable);

  List<Idea> getListByCondition(IdeaDTO ideaDTO);
}
