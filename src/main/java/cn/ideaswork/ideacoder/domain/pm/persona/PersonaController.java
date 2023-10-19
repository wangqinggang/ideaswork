package cn.ideaswork.ideacoder.domain.pm.persona;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
    tags = "用户画像 API"
)
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/personas")
@CrossOrigin()
public class PersonaController {
  @Autowired
  PersonaService personaService;

  @PostMapping
  @ApiOperation("添加用户画像")
  public Persona savePersona(@RequestBody Persona persona) {
    persona.setId(UUID.randomUUID().toString());
    return personaService.savePersona(persona);
  }

  @GetMapping
  @ApiOperation("获取 用户画像列表")
  public List<Persona> getPersonas() {
    return personaService.getAllPersonas();
  }

  @GetMapping("/{id}")
  @ApiOperation("根据主键获取一条 用户画像")
  public Persona getPersona(@PathVariable("id") final String id) {
    return personaService.getPersonaById(id);
  }

  @PutMapping("/{id}")
  @ApiOperation("根据主键更新用户画像")
  public Persona updatePersona(@RequestBody Persona persona, @PathVariable("id") final String id) {
    return personaService.updatePersonaById(persona,id);
  }

  @DeleteMapping("/{id}")
  @ApiOperation("根据主键 id 删除用户画像")
  public void deletePersonaById(@PathVariable("id") final String id) {
    personaService.deletePersonaById(id);
  }

  @GetMapping("/isExist/{id}")
  @ApiOperation("根据主键 id 查看用户画像是否存在")
  public Boolean isExistPersona(@PathVariable("id") final String id) {
    return personaService.isPersonaExist(id);
  }

  @GetMapping("/getPageList")
  @ApiOperation("分页条件查询")
  public Page<Persona> getPageByCondition(PersonaDTO personaDTO,
      @PageableDefault(value = 1, size = 20) Pageable pageable) {
    return personaService.getPageByCondition(personaDTO ,pageable);
  }

  @GetMapping("/getList")
  @ApiOperation("分页条件查询")
  public List<Persona> getListByCondition(PersonaDTO personaDTO) {
    return personaService.getListByCondition(personaDTO );
  }
}