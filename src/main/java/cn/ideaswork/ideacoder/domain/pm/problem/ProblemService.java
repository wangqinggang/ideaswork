package cn.ideaswork.ideacoder.domain.pm.problem;


import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemService {
  Problem saveProblem(Problem problem);

  List<Problem> getAllProblems();

  Problem getProblemById(final String id);

  Problem updateProblemById(Problem problem, final String id);

  void deleteProblemById(final String id);

  Boolean isProblemExist(final String id);

  Page<Problem> getPageByCondition(ProblemDTO problemDTO, Pageable pageable);

  List<Problem> getListByCondition(ProblemDTO problemDTO);
}
