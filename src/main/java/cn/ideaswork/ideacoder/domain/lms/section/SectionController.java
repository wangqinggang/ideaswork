package cn.ideaswork.ideacoder.domain.lms.section;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.util.Date;
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
        tags = "课时 API"
)
@CrossOrigin
@RequestMapping("/sections")
public class SectionController {
    @Autowired
    SectionService sectionService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("添加课时")
    public Section saveSection(@RequestBody Section section) {
        section.setId(UUID.randomUUID().toString());
        section.setCzsj(new Date());
        if(section.getPxh()==null){
            section.setPxh(1);
        }
        return sectionService.saveSection(section);
    }

    @GetMapping
    @ApiOperation("获取 课时列表")
    public List<Section> getSections() {
        return sectionService.getAllSections();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 课时")
    public Section getSection(@PathVariable("id") final String id) {
        return sectionService.getSectionById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("根据主键更新课时")
    public Section updateSection(@RequestBody Section section, @PathVariable("id") final String id) {
        return sectionService.updateSectionById(section, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("根据主键 id 删除课时")
    public void deleteSectionById(@PathVariable("id") final String id) throws TencentCloudSDKException {
        sectionService.deleteSectionById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看课时是否存在")
    public Boolean isExistSection(@PathVariable("id") final String id) {
        return sectionService.isSectionExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Section> getPageByCondition(SectionDTO sectionDTO,
                                            @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return sectionService.getPageByCondition(sectionDTO, pageable);
    }
}