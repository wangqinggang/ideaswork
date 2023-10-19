package cn.ideaswork.ideacoder.domain.pm.releaseplan;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(
    tags = "发布计划 API"
)
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/releaseplans")
@CrossOrigin()
public class ReleaseplanController {
  @Autowired
  ReleaseplanService releaseplanService;

  @PostMapping
  @ApiOperation("添加发布计划")
  public Releaseplan saveReleaseplan(@RequestBody Releaseplan releaseplan) {
    releaseplan.setId(UUID.randomUUID().toString());
    return releaseplanService.saveReleaseplan(releaseplan);
  }

  @GetMapping
  @ApiOperation("获取 发布计划列表")
  public List<Releaseplan> getReleaseplans() {
    return releaseplanService.getAllReleaseplans();
  }

  @GetMapping("/{id}")
  @ApiOperation("根据主键获取一条 发布计划")
  public Releaseplan getReleaseplan(@PathVariable("id") final String id) {
    return releaseplanService.getReleaseplanById(id);
  }

  @PutMapping("/{id}")
  @ApiOperation("根据主键更新发布计划")
  public Releaseplan updateReleaseplan(@RequestBody Releaseplan releaseplan,
      @PathVariable("id") final String id) {
    return releaseplanService.updateReleaseplanById(releaseplan,id);
  }

  @DeleteMapping("/{id}")
  @ApiOperation("根据主键 id 删除发布计划")
  public void deleteReleaseplanById(@PathVariable("id") final String id) {
    releaseplanService.deleteReleaseplanById(id);
  }

  @GetMapping("/isExist/{id}")
  @ApiOperation("根据主键 id 查看发布计划是否存在")
  public Boolean isExistReleaseplan(@PathVariable("id") final String id) {
    return releaseplanService.isReleaseplanExist(id);
  }

  @GetMapping("/getPageList")
  @ApiOperation("分页条件查询")
  public Page<Releaseplan> getPageByCondition(ReleaseplanDTO releaseplanDTO,
      @PageableDefault(value = 1, size = 20) Pageable pageable) {
    return releaseplanService.getPageByCondition(releaseplanDTO ,pageable);
  }
}