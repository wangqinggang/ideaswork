package cn.ideaswork.ideacoder.domain.pm.releaseplan;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReleaseplanService {
  Releaseplan saveReleaseplan(Releaseplan releaseplan);

  List<Releaseplan> getAllReleaseplans();

  Releaseplan getReleaseplanById(final String id);

  Releaseplan updateReleaseplanById(Releaseplan releaseplan, final String id);

  void deleteReleaseplanById(final String id);

  Boolean isReleaseplanExist(final String id);

  Page<Releaseplan> getPageByCondition(ReleaseplanDTO releaseplanDTO, Pageable pageable);

  List<Releaseplan> getListByCondition(ReleaseplanDTO releaseplanDTO);
}
