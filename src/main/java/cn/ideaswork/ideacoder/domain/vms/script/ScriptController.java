package cn.ideaswork.ideacoder.domain.vms.script;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.vms.copy.Copy;
import cn.ideaswork.ideacoder.domain.vms.copy.CopyService;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Api(tags = "拍摄脚本 API")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/scripts")
public class ScriptController {
    @Autowired
    ScriptService scriptService;

    @Autowired
    CopyService copyService;

    @PostMapping
    @ApiOperation("添加拍摄脚本")
    @Transactional
    public ResponseEntity saveScript(@RequestBody Script script) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请先登录");
        }
        Integer scriptCount = loginUser.getScriptCount();
        Integer actrualScriptCount = scriptService.getScriptCountByCopyId(script.getCopyId());
        if (ObjectUtil.isNotEmpty(actrualScriptCount) && actrualScriptCount >= scriptCount) {
            return ResponseEntity.badRequest().body("您的当前文案脚本数量已达上限");
        }

        script.setId(UUID.randomUUID().toString());
        script.setUserId(loginUser.getId());
        Integer maxPxh = scriptService.getMaxPxh(loginUser.getId(), script.getCopyId());
        maxPxh++;
        script.setPxh(maxPxh);
        script.setPsh(maxPxh);
        Script scriptDB = scriptService.saveScript(script);
        updateCopyScriptNum(script.getCopyId());

        return ResponseEntity.ok(scriptDB);
    }


//  @GetMapping
//  @ApiOperation("获取 拍摄脚本列表")
//  public List<Script> getScripts() {
//    return scriptService.getAllScripts();
//  }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 拍摄脚本")
    public Script getScript(@PathVariable("id") final String id) {
        return scriptService.getScriptById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新拍摄脚本")
    @Transactional
    public ResponseEntity updateScript(@RequestBody Script script, @PathVariable("id") final String id) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请先登录");
        }
        String userId = script.getUserId();
        // 如果是脚本用户与登录用户不一致，不允许修改
        if (!loginUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("非法操作");
        }
        script.setUserId(loginUser.getId());
        String copyId = script.getCopyId();
        Script scriptLast = scriptService.updateScriptById(script, id);
        updateCopyScriptNum(copyId);
        return ResponseEntity.ok(scriptLast);
    }

    /**
     * 更新文案的脚本数量
     *
     * @param copyId 文案 id
     * @throws Exception 异常
     */
    private void updateCopyScriptNum(String copyId) throws Exception {
        ScriptDTO scriptDTO = new ScriptDTO();
        scriptDTO.setCopyId(copyId);
        List<Script> scriptList = scriptService.getListByCondition(scriptDTO);
        Copy copyDB = copyService.getCopyById(copyId);
        if (scriptList.size() > 0) {
            copyDB.setScriptNum(scriptList.size());
            List<Script> finishedList = scriptList.stream().filter(one -> one.getFinished() != null ? one.getFinished() == true : false).collect(Collectors.toList());
            copyDB.setFinishedScriptNum(finishedList.size());
        } else {
            copyDB.setFinishedScriptNum(0);
            copyDB.setScriptNum(0);
        }
        copyService.updateCopyById(copyDB, copyDB.getId());

    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除拍摄脚本")
    @Transactional
    public ResponseEntity deleteScriptById(@PathVariable("id") final String id) throws Exception {

        // 获取当前登录用户
        User loginUser = SysTools.getLoginUser();
        if (ObjectUtil.isEmpty(loginUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请先登录");
        } else {
            if (ObjectUtil.isEmpty(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("请返回重试");
            }
            Script scriptDB = scriptService.getScriptById(id);
            if (ObjectUtil.isEmpty(scriptDB)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("该拍摄脚本不存在");
            }
            if (!scriptDB.getUserId().equals(loginUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("非法操作");
            }
            if (ObjectUtil.isNotEmpty(scriptDB)) {
                scriptService.deleteScriptById(id);
            }
            updateCopyScriptNum(scriptDB.getCopyId());
        }

        return ResponseEntity.ok("删除成功");
    }



    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看拍摄脚本是否存在")
    public Boolean isExistScript(@PathVariable("id") final String id) {
        return scriptService.isScriptExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Script> getPageByCondition(ScriptDTO scriptDTO,
                                           @PageableDefault(value = 1, size = 20) Pageable pageable) {
        User loginUser = SysTools.getLoginUser();
        scriptDTO.setUserId(loginUser.getId());
        return scriptService.getPageByCondition(scriptDTO, pageable);
    }

    @GetMapping("/getList")
    @ApiOperation("分页条件查询")
    public List<Script> getListByCondition(ScriptDTO scriptDTO) {
        User loginUser = SysTools.getLoginUser();
        scriptDTO.setUserId(loginUser.getId());
        return scriptService.getListByCondition(scriptDTO);
    }

    @GetMapping("/getListOrderByPxh")
    @ApiOperation("分页条件查询按照拍摄号排序")
    public List<Script> getListOrderByPxh(@RequestParam(value = "copyId") String copyId) {
        ScriptDTO scriptDTO = new ScriptDTO();
        scriptDTO.setCopyId(copyId);
//        User loginUser = SysTools.getLoginUser();
//        scriptDTO.setUserId(loginUser.getId());
        scriptDTO.setOrderByPxh(true);
        return scriptService.getListByCondition(scriptDTO);
    }

    @GetMapping("/getListOrderByPsh")
    @ApiOperation("分页条件查询按照拍摄号排序")
    public List<Script> getListOrderByPsh(@RequestParam(value = "copyId") String copyId) {
        ScriptDTO scriptDTO = new ScriptDTO();
        scriptDTO.setCopyId(copyId);
//        User loginUser = SysTools.getLoginUser();
//        scriptDTO.setUserId(loginUser.getId());
        scriptDTO.setOrderByPxh(false);
        return scriptService.getListByCondition(scriptDTO);
    }

    @GetMapping("/moveByPxh/{id}/{isUp}")
    @ApiOperation("根据pxh移动一位")
    public ResponseEntity moveByPxh(@PathVariable("id") String id, @PathVariable("isUp") boolean isUp) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请先登录");
        }

        if (isUp) {
            return ResponseEntity.ok(scriptService.moveUpByPxh(loginUser.getId(), id));
        } else {
            return ResponseEntity.ok(scriptService.moveDownByPxh(loginUser.getId(), id));
        }
    }

    @GetMapping("/moveByPsh/{id}/{isUp}")
    @ApiOperation("根据psh移动一位")
    public ResponseEntity moveByPsh(@PathVariable("id") String id, @PathVariable("isUp") boolean isUp) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请先登录");
        if (isUp) {
            return ResponseEntity.ok(scriptService.moveUpByPsh(loginUser.getId(), id));
        } else {
            return ResponseEntity.ok(scriptService.moveDownByPsh(loginUser.getId(), id));
        }
    }

    @PostMapping(value = "/importScriptList/{copyId}")
    @ApiOperation("根据文案导入 excel 脚本（全量更新）")
    @Transactional(rollbackFor = Exception.class)
//    @PreAuthorize("hasAuthority('VMS_SCRIPT_EXPORT')")
    public ResponseEntity importPlantSurfaceFile(
            @ApiParam(value = "文件上传", required = true) @RequestPart("file") MultipartFile file,
            @ApiParam(value = "文案id", required = true) @PathVariable("copyId") String copyId) throws Exception {
        // 检查登录状态
        User loginUser = SysTools.getLoginUser();
        if (ObjectUtil.isEmpty(loginUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请先登录");
        }
        // 检查文案是否存在
        Copy copyDB = copyService.getCopyById(copyId);
        if (ObjectUtil.isEmpty(copyDB)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("该文案不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", new ArrayList<>());
        result.put("error", "");
        String userId = loginUser.getId();
        ScriptDTO scriptDTO = new ScriptDTO();
        scriptDTO.setUserId(userId);
        scriptDTO.setCopyId(copyId);
        List<Script> scriptListDB = scriptService.getListByCondition(scriptDTO);

        if (ObjectUtil.isNotEmpty(copyDB)) {
            if (copyDB.getUserId().equals(userId)) {
                Map<String, Object> importScriptResult = scriptService.getImportScriptResult(file);
                List<Script> scriptList = (List<Script>) importScriptResult.get("list");
                String error = (String) importScriptResult.get("error");
                if (StringUtils.isEmpty(error)) {
                    for (int i = 0; i < scriptList.size(); i++) {
                        Script script = scriptList.get(i);
                        script.setUserId(userId);
                        script.setCopyId(copyId);
                        script.setCopyName(copyDB.getTopicName());
                        script.setId(UUID.randomUUID().toString());
                        scriptService.saveScript(script);
                    }
                    for (int i = 0; i < scriptListDB.size(); i++) {
                        Script scriptOfDB = scriptListDB.get(i);
                        scriptService.deleteScriptById(scriptOfDB.getId());
                    }
                    result.put("success", true);
                } else {
                    result.put("success", false);
                }
                result.put("list", scriptList);
                result.put("error", error);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("该文案不属于当前用户");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("该文案不存在");
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/exportScriptList/{copyId}")
    @ApiOperation("根据文案id导出 excel 脚本")
    @PreAuthorize("hasAuthority('VMS_SCRIPT_EXPORT')")
    public ResponseEntity importPlantSurfaceFile(HttpServletResponse response,
                                                 @ApiParam(value = "文案id", required = true) @PathVariable("copyId") String copyId) throws Exception {
        // 检查登录状态
        User loginUser = SysTools.getLoginUser();
        if (ObjectUtil.isEmpty(loginUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请先登录");
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        Copy copyDB = copyService.getCopyById(copyId);

        // 检查是否是当前登录用户的文案
        if (!copyDB.getUserId().equals(loginUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("非法操作");
        }


//        File templateFile = ResourceUtils.getFile("classpath:templates/excel/vms/ShootingScriptTemplate.xlsx");
        // 从url下载文件到file对象
        String url = "https://7072-prod-2ghfmhr55c2cacf4-1253634941.tcb.qcloud.la/VMS/ShootingScriptTemplate/ShootingScriptTemplate.xlsx?sign=e0c0f56ef9bb46f87f3b1950a6125bb7&t=1685808733";
        byte[] bytes = HttpUtil.downloadBytes(url);
        InputStream templateStream = new ByteArrayInputStream(bytes);

        // 在这里使用 inputStream 来操作 Excel 文件
        IoUtil.close(templateStream);


        List<ScriptExcelDTO> scriptExcelDTOS = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(copyDB)) {
            String title = copyDB.getTitle();
            LocalDateTime now = LocalDateTime.now();
            // 保证下载到本地文件名不乱码的
            String fileName = URLEncoder.encode(title + "_" + now, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

//            if (copyDB.getUserId().equals(userId)) {
            ScriptDTO scriptDTO = new ScriptDTO();
//                scriptDTO.setUserId(userId);
            scriptDTO.setCopyId(copyId);
            List<Script> scriptList = scriptService.getListByCondition(scriptDTO);
            for (Script script : scriptList) {
                ScriptExcelDTO scriptExcelDTO = BeanUtil.copyProperties(script, ScriptExcelDTO.class);
                scriptExcelDTOS.add(scriptExcelDTO);
            }
            for (int i = 0; i < scriptExcelDTOS.size(); i++) {
                ScriptExcelDTO scriptExcelDTO = scriptExcelDTOS.get(i);
                if (ObjectUtil.isNotEmpty(scriptExcelDTO.getFinished())) {
                    if (scriptExcelDTO.getFinished().equals("true")) {
                        scriptExcelDTO.setFinished("已完成");
                    } else {
                        scriptExcelDTO.setFinished("未完成");
                    }
                } else {
                    scriptExcelDTO.setFinished("未完成");
                }

            }

            ExcelWriter excelWriter = EasyExcel
                    .write(response.getOutputStream(), ScriptExcelDTO.class)
                    .withTemplate(templateStream).build();

            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("date", LocalDateTime.now());
            dataMap.put("title", copyDB.getTitle());
            dataMap.put("topic", copyDB.getTopicName());
            dataMap.put("intro", copyDB.getIntro());
            List<ScriptExcelDTO> isFinished = scriptExcelDTOS.stream().filter(one -> one.getFinished().equals("已完成")).collect(Collectors.toList());
            dataMap.put("num", scriptExcelDTOS.size());
            dataMap.put("finishedNum", isFinished.size());
            excelWriter.fill(dataMap, writeSheet);
            excelWriter.fill(scriptExcelDTOS, writeSheet);
            excelWriter.finish();
//                EasyExcel
//                        .write(response.getOutputStream(), ScriptExcelDTO.class)
//                        .withTemplate(templateFile)
//                        .sheet("Sheet1")
//                        .doWrite(scriptExcelDTOS);
//            } else {
//                throw new Exception("没有权限导出脚本，请尝试刷新页面");
//            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("该文案不存在");
        }

        return ResponseEntity.ok("导出成功");
    }
}