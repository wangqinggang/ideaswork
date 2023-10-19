package cn.ideaswork.ideacoder.domain.pm.storymap;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StorymapService {
  Storymap saveStorymap(Storymap storymap);

  List<Storymap> getAllStorymaps();

  Storymap getStorymapById(final String id);

  Storymap updateStorymapById(Storymap storymap, final String id);

  void deleteStorymapById(final String id);

  Boolean isStorymapExist(final String id);

  Page<Storymap> getPageByCondition(StorymapDTO storymapDTO, Pageable pageable);

  List<Storymap> getListByCondition(StorymapDTO storymapDTO);
}
