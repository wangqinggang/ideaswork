package cn.ideaswork.ideacoder.domain.vms.script;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ScriptService {
    Script saveScript(Script script);

    List<Script> getAllScripts();

    Script getScriptById(final String id);

    Script updateScriptById(Script script, final String id) throws Exception;

    void deleteScriptById(final String id) throws Exception;

    Boolean isScriptExist(final String id);

    Page<Script> getPageByCondition(ScriptDTO scriptDTO, Pageable pageable);

    Page<Script> getPageByConditionPsh(ScriptDTO scriptDTO, Pageable pageable);

    List<Script> getListByCondition(ScriptDTO scriptDTO);

    List<Script> moveUpByPxh(String loginUserId, String id) throws Exception;

    List<Script> moveDownByPxh(String loginUserId, String id) throws Exception;

    List<Script> moveUpByPsh(String loginUserId, String id) throws Exception;

    List<Script> moveDownByPsh(String loginUserId, String id) throws Exception;

    Integer getMaxPxh(String id, String copyId);

    Map<String,Object> getImportScriptResult(MultipartFile file) throws Exception;

    Integer getScriptNumByUserId(String id);

    Integer getScriptCountByCopyId(String copyId);
}
