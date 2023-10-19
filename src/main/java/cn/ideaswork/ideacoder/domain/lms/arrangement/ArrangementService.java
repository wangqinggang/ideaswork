package cn.ideaswork.ideacoder.domain.lms.arrangement;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArrangementService {
    Arrangement saveArrangement(Arrangement arrangement);

    List<Arrangement> getAllArrangements();

    Arrangement getArrangementById(final String id);

    Arrangement updateArrangementById(Arrangement arrangement, final String id);

    void deleteArrangementById(final String id);

    Boolean isArrangementExist(final String id);

    Page<Arrangement> getPageByCondition(ArrangementDTO arrangementDTO, Pageable pageable);

    List<Arrangement> getListByCondition(ArrangementDTO arrangementDTO);
}
