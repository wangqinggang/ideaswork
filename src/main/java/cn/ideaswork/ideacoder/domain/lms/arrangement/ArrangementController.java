package cn.ideaswork.ideacoder.domain.lms.arrangement;

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
        tags = "排课 API"
)
@CrossOrigin()
@RequestMapping("/arrangements")
public class ArrangementController {
    @Autowired
    ArrangementService arrangementService;

    @PostMapping
    @ApiOperation("添加排课")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Arrangement saveArrangement(@RequestBody Arrangement arrangement) {
        arrangement.setId(UUID.randomUUID().toString());
        return arrangementService.saveArrangement(arrangement);
    }

    @GetMapping
    @ApiOperation("获取 排课列表")
    public List<Arrangement> getArrangements() {
        return arrangementService.getAllArrangements();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 排课")
    public Arrangement getArrangement(@PathVariable("id") final String id) {
        return arrangementService.getArrangementById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新排课")
    public Arrangement updateArrangement(@RequestBody Arrangement arrangement,
                                         @PathVariable("id") final String id) {
        return arrangementService.updateArrangementById(arrangement, id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除排课")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteArrangementById(@PathVariable("id") final String id) {
        arrangementService.deleteArrangementById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看排课是否存在")
    public Boolean isExistArrangement(@PathVariable("id") final String id) {
        return arrangementService.isArrangementExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Arrangement> getPageByCondition(ArrangementDTO arrangementDTO,
                                                @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return arrangementService.getPageByCondition(arrangementDTO, pageable);
    }
}