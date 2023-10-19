package cn.ideaswork.ideacoder.domain.coder.domain;

import cn.ideaswork.ideacoder.domain.coder.project.Project;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

import java.util.List;
import java.util.UUID;

@RestController
//@Tag(name = "领域实体", description = "领域实体")
@Api(tags = "领域实体 API")
@CrossOrigin()
@PreAuthorize("hasRole('ROLE_ADMIN')")
//@PreAuthorize("hasRole('ROLE_DOMAIN')")
@RequestMapping("/domains")
public class DomainController {
    @Autowired
    private DomainService domainService;

    @PostMapping("/getDomainBySql")
    @Operation(summary = "根据 SQL 获取  Domain", description = "根据 SQL 获取  Domain")
    public Domain getDomainBySql(@RequestBody String domainSql) throws Exception {
        return  domainService.getDomainBySqlMysql(domainSql); // TODO 原先为 getDomainBySql
    }

    @PostMapping("/getDomainBySqlMysql")
    @Operation(summary = "根据 SQL 获取  Domain", description = "根据 Mysql SQL 获取  Domain")
    public Domain getDomainBySqlMysql(@RequestBody String domainSql) throws Exception {
        return  domainService.getDomainBySqlMysql(domainSql);
    }

    @PostMapping
    @Operation(summary = "保存领域对象", description = "保存领域对象")
    public ResponseEntity<?> saveDomain(@RequestBody Domain domain) {
        String id = UUID.randomUUID().toString();
        domain.setId(id);
        int pkNum = 0;
        for (DomainField domainField : domain.getDomainFieldList()) {
            if (domainField.getIsKey()) {
                pkNum += 1;
            }
            domainField.setDomainId(id);
            if (StringUtils.isBlank(domainField.getId())) {
                domainField.setId(UUID.randomUUID().toString());
            }
        }
        if (pkNum<1){
            return ResponseEntity.badRequest().body("请至少选择一个主键");
        }

        return ResponseEntity.ok(domainService.saveDomain(domain));
    }

    @GetMapping
    @Operation(summary = "获取所有领域对象列表", description = "获取所有领域对象列表")
    public List<Domain> getDomains() {
        return domainService.getAllDomains();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取一个领域对象", description = "根据 id 获取一个领域对象")
    public Domain getDomain(@PathVariable("id") String id) {
        return domainService.getDomainById(id);
    }

    @GetMapping("/projectId/{projectId}")
    @Operation(summary = "获取项目下领域对象列表", description = "根据 projectId 获取领域对象列表")
    public List<Domain> getDomainListByProjectId(@PathVariable("projectId") String projectId) {
        return domainService.getDomainListByProjectId(projectId);
    }


    @PutMapping("/{id}")
    @Operation(summary = "更新一个领域对象", description = "根据 id 更新一个领域对象")
    public Domain updateDomain(@RequestBody Domain domain, @PathVariable("id") String id) {
        return domainService.updateDomainById(domain, id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除一个领域对象", description = "根据 id 删除一个领域对象")
    public void deleteDomain(@PathVariable("id") String id) {
        domainService.deleteDomainById(id);
    }

    @GetMapping("/isExist/{id}")
    @Operation(summary = "判断领域对象是否存在", description = "根据 id 判断领域对象是否存在")
    public Boolean isExistDomain(@PathVariable("id") String id) {
        return domainService.isDomainExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Domain> getPageByCondition(Domain domain,
                                            @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return domainService.getDomainPageListByCondition(domain ,pageable);
    }

}
