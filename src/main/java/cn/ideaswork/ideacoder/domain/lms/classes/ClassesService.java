package cn.ideaswork.ideacoder.domain.lms.classes;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClassesService {
  Classes saveClasses(Classes classes);

  List<Classes> getAllClassess();

  Classes getClassesById(final String id);

  Classes updateClassesById(Classes classes, final String id);

  void deleteClassesById(final String id);

  Boolean isClassesExist(final String id);

  Page<Classes> getPageByCondition(ClassesDTO classesDTO, Pageable pageable);

  List<Classes> getListByCondition(ClassesDTO classesDTO);
}
