package cn.ideaswork.ideacoder.domain.vms.script;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ScriptServiceImpl implements ScriptService {
    @Autowired
    private ScriptDao scriptDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Script saveScript(final Script script) {
        return scriptDao.save(script);
    }

    @Override
    public List<Script> getAllScripts() {
        return scriptDao.findAll();
    }

    @Override
    public Script getScriptById(final String id) {
        return scriptDao.findById(id).orElse(new Script());
    }

    @Override
    @Transactional
    public Script updateScriptById(final Script script, final String id) {
        Script scriptDb = scriptDao.findById(id).orElse(new Script());
        User loginUser = SysTools.getLoginUser();
        String userId = loginUser.getId();
        scriptDb.setUserId(userId);
        BeanUtils.copyProperties(script, scriptDb);
        return scriptDao.save(scriptDb);
    }

    @Override
    @Transactional
    public void deleteScriptById(final String id)  {
        Script scriptDb = scriptDao.findById(id).orElse(new Script());
        User loginUser = SysTools.getLoginUser();
        String userId = loginUser.getId();
        if (userId.equals(scriptDb.getUserId())) {
            scriptDao.deleteById(id);
        }
    }

    @Override
    public Boolean isScriptExist(final String id) {
        return scriptDao.existsById(id);
    }

    @Override
    public Page<Script> getPageByCondition(final ScriptDTO scriptDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(scriptDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(scriptDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCopyId())) {
            Criteria criteria = Criteria.where("copyId").is(scriptDTO.getCopyId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCopyName())) {
            Criteria criteria = Criteria.where("copyName").regex(scriptDTO.getCopyName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getLocation())) {
            Criteria criteria = Criteria.where("location").regex(scriptDTO.getLocation());
            query.addCriteria(criteria);
        }
        if (scriptDTO.getScene() != null) {
            Criteria criteria = Criteria.where("Scene").is(scriptDTO.getScene());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getPlot())) {
            Criteria criteria = Criteria.where("plot").regex(scriptDTO.getPlot());
            query.addCriteria(criteria);
        }
        if (scriptDTO.getShot() != null) {
            Criteria criteria = Criteria.where("shot").is(scriptDTO.getShot());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getShotSize())) {
            Criteria criteria = Criteria.where("shotSize").is(scriptDTO.getShotSize());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getShotAngle())) {
            Criteria criteria = Criteria.where("shotAngle").is(scriptDTO.getShotAngle());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getShotMove())) {
            Criteria criteria = Criteria.where("shotMove").is(scriptDTO.getShotMove());
            query.addCriteria(criteria);
        }
//        if (!StringUtils.isEmpty(scriptDTO.getShotMethod())) {
//            Criteria criteria = Criteria.where("shotMethod").is(scriptDTO.getShotMethod());
//            query.addCriteria(criteria);
//        }
        if (!StringUtils.isEmpty(scriptDTO.getContent())) {
            Criteria criteria = Criteria.where("content").regex(scriptDTO.getContent());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCaption())) {
            Criteria criteria = Criteria.where("caption").regex(scriptDTO.getCaption());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCch())) {
            Criteria criteria = Criteria.where("cch").is(scriptDTO.getCch());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getBz())) {
            Criteria criteria = Criteria.where("bz").is(scriptDTO.getBz());
            query.addCriteria(criteria);
        }
        if (scriptDTO.getFinished() != null) {
            Criteria criteria = Criteria.where("finished").is(scriptDTO.getFinished());
            query.addCriteria(criteria);
        }
//        Sort sort = pageable.getSort();
        Sort sort = Sort.by(Sort.Direction.ASC, "pxh");
        query.with(sort);
        return this.listToPage(mongoTemplate.find(query, Script.class), pageable);
    }

    @Override
    public Page<Script> getPageByConditionPsh(final ScriptDTO scriptDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(scriptDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(scriptDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getUserId())) {
            Criteria criteria = Criteria.where("userId").is(scriptDTO.getUserId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCopyId())) {
            Criteria criteria = Criteria.where("copyId").is(scriptDTO.getCopyId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCopyName())) {
            Criteria criteria = Criteria.where("copyName").regex(scriptDTO.getCopyName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getLocation())) {
            Criteria criteria = Criteria.where("location").regex(scriptDTO.getLocation());
            query.addCriteria(criteria);
        }
        if (scriptDTO.getScene() != null) {
            Criteria criteria = Criteria.where("Scene").is(scriptDTO.getScene());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getPlot())) {
            Criteria criteria = Criteria.where("plot").regex(scriptDTO.getPlot());
            query.addCriteria(criteria);
        }
        if (scriptDTO.getShot() != null) {
            Criteria criteria = Criteria.where("shot").is(scriptDTO.getShot());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getShotSize())) {
            Criteria criteria = Criteria.where("shotSize").is(scriptDTO.getShotSize());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getShotAngle())) {
            Criteria criteria = Criteria.where("shotAngle").is(scriptDTO.getShotAngle());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getShotMove())) {
            Criteria criteria = Criteria.where("shotMove").is(scriptDTO.getShotMove());
            query.addCriteria(criteria);
        }
//        if (!StringUtils.isEmpty(scriptDTO.getShotMethod())) {
//            Criteria criteria = Criteria.where("shotMethod").is(scriptDTO.getShotMethod());
//            query.addCriteria(criteria);
//        }
        if (!StringUtils.isEmpty(scriptDTO.getContent())) {
            Criteria criteria = Criteria.where("content").regex(scriptDTO.getContent());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCaption())) {
            Criteria criteria = Criteria.where("caption").regex(scriptDTO.getCaption());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCch())) {
            Criteria criteria = Criteria.where("cch").is(scriptDTO.getCch());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getBz())) {
            Criteria criteria = Criteria.where("bz").is(scriptDTO.getBz());
            query.addCriteria(criteria);
        }
        if (scriptDTO.getFinished() != null) {
            Criteria criteria = Criteria.where("finished").is(scriptDTO.getFinished());
            query.addCriteria(criteria);
        }
//        Sort sort = pageable.getSort();
        Sort sort = Sort.by(Sort.Direction.ASC, "psh");
        query.with(sort);
        return this.listToPage(mongoTemplate.find(query, Script.class), pageable);
    }

    @Override
    public List<Script> getListByCondition(final ScriptDTO scriptDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(scriptDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(scriptDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getUserId())) {
            Criteria criteria = Criteria.where("userId").is(scriptDTO.getUserId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCopyId())) {
            Criteria criteria = Criteria.where("copyId").is(scriptDTO.getCopyId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCopyName())) {
            Criteria criteria = Criteria.where("copyName").regex(scriptDTO.getCopyName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getLocation())) {
            Criteria criteria = Criteria.where("location").regex(scriptDTO.getLocation());
            query.addCriteria(criteria);
        }
        if (scriptDTO.getScene() != null) {
            Criteria criteria = Criteria.where("Scene").is(scriptDTO.getScene());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getPlot())) {
            Criteria criteria = Criteria.where("plot").regex(scriptDTO.getPlot());
            query.addCriteria(criteria);
        }
        if (scriptDTO.getShot() != null) {
            Criteria criteria = Criteria.where("shot").is(scriptDTO.getShot());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getShotSize())) {
            Criteria criteria = Criteria.where("shotSize").is(scriptDTO.getShotSize());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getShotAngle())) {
            Criteria criteria = Criteria.where("shotAngle").is(scriptDTO.getShotAngle());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getShotMove())) {
            Criteria criteria = Criteria.where("shotMove").is(scriptDTO.getShotMove());
            query.addCriteria(criteria);
        }
//        if (!StringUtils.isEmpty(scriptDTO.getShotMethod())) {
//            Criteria criteria = Criteria.where("shotMethod").is(scriptDTO.getShotMethod());
//            query.addCriteria(criteria);
//        }
        if (!StringUtils.isEmpty(scriptDTO.getContent())) {
            Criteria criteria = Criteria.where("content").regex(scriptDTO.getContent());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCaption())) {
            Criteria criteria = Criteria.where("caption").regex(scriptDTO.getCaption());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getCch())) {
            Criteria criteria = Criteria.where("cch").is(scriptDTO.getCch());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(scriptDTO.getBz())) {
            Criteria criteria = Criteria.where("bz").is(scriptDTO.getBz());
            query.addCriteria(criteria);
        }
        if (scriptDTO.getFinished() != null) {
            Criteria criteria = Criteria.where("finished").is(scriptDTO.getFinished());
            query.addCriteria(criteria);
        }
        if (scriptDTO.getOrderByPxh() != null) {
            if (scriptDTO.getOrderByPxh()) {
                Sort sort = Sort.by(Sort.Order.asc("pxh"));
                query.with(sort);
            } else {
                Sort sort = Sort.by(Sort.Order.asc("psh"));
                query.with(sort);
            }
        }

        return mongoTemplate.find(query, Script.class);
    }

    @Override
    public List<Script> moveUpByPxh(String loginUserId, String id) {
        Script scriptById = this.getScriptById(id);
        Integer currentPxh = scriptById.getPxh();

        ScriptDTO condition = new ScriptDTO();
        condition.setUserId(loginUserId);
        condition.setCopyId(scriptById.getCopyId());
        condition.setOrderByPxh(true);

        List<Script> listByCondition = this.getListByCondition(condition);

        Integer tempPxh = 0;

        if (listByCondition.size() > 1) {
            if (listByCondition.get(0).getPxh() == currentPxh) {
                return listByCondition;
            } else {
                for (int i = 0; i < listByCondition.size(); i++) {
                    Script script = listByCondition.get(i);
                    Integer itemPxh = script.getPxh();
                    if (itemPxh == currentPxh) {
                        Script aScript = listByCondition.get(i - 1);
                        Integer aPxh = aScript.getPxh();
                        tempPxh = aPxh;
                        aScript.setPxh(currentPxh);
                        scriptById.setPxh(tempPxh);
                        this.updateScriptById(aScript, aScript.getId());
                        this.updateScriptById(scriptById, scriptById.getId());
                        break;
                    }
                }
            }
        }

        return this.getListByCondition(condition);
    }

    @Override
    public List<Script> moveDownByPxh(String loginUserId, String id)  {
        Script scriptById = this.getScriptById(id);
        Integer currentPxh = scriptById.getPxh();

        ScriptDTO condition = new ScriptDTO();
        condition.setUserId(loginUserId);
        condition.setCopyId(scriptById.getCopyId());
        condition.setOrderByPxh(true);

        List<Script> listByCondition = this.getListByCondition(condition);

        Integer tempPxh = 0;

        if (listByCondition.size() > 1) {
            Integer index = listByCondition.size() - 1;
            if (listByCondition.get(index).getPxh() == currentPxh) {
                return listByCondition;
            } else {
                // 获取
                for (int i = 0; i < listByCondition.size(); i++) {
                    Script script = listByCondition.get(i);
                    Integer itemPxh = script.getPxh();
                    if (itemPxh == currentPxh) {
                        Script bScript = listByCondition.get(i + 1);
                        Integer bPxh = bScript.getPxh();
                        tempPxh = bPxh;
                        bScript.setPxh(currentPxh);
                        scriptById.setPxh(tempPxh);
                        this.updateScriptById(bScript, bScript.getId());
                        this.updateScriptById(scriptById, scriptById.getId());
                        break;
                    }
                }
            }
        }

        return this.getListByCondition(condition);
    }

    @Override
    public List<Script> moveUpByPsh(String loginUserId, String id)  {
        Script scriptById = this.getScriptById(id);
        Integer currentPsh = scriptById.getPsh();

        ScriptDTO condition = new ScriptDTO();
        condition.setUserId(loginUserId);
        condition.setCopyId(scriptById.getCopyId());
        condition.setOrderByPxh(false);

        List<Script> listByCondition = this.getListByCondition(condition);

        Integer tempPsh = 0;

        if (listByCondition.size() > 1) {
            if (listByCondition.get(0).getPsh() == currentPsh) {
                return listByCondition;
            } else {
                for (int i = 0; i < listByCondition.size(); i++) {
                    Script script = listByCondition.get(i);
                    Integer itemPsh = script.getPsh();
                    if (itemPsh == currentPsh) {
                        Script aScript = listByCondition.get(i - 1);
                        Integer aPsh = aScript.getPsh();
                        tempPsh = aPsh;
                        aScript.setPsh(currentPsh);
                        scriptById.setPsh(tempPsh);
                        this.updateScriptById(aScript, aScript.getId());
                        this.updateScriptById(scriptById, scriptById.getId());
                        break;
                    }
                }
            }
        }

        return this.getListByCondition(condition);
    }

    @Override
    public List<Script> moveDownByPsh(String loginUserId, String id)  {
        Script scriptById = this.getScriptById(id);
        Integer currentPsh = scriptById.getPsh();

        ScriptDTO condition = new ScriptDTO();
        condition.setUserId(loginUserId);
        condition.setCopyId(scriptById.getCopyId());
        condition.setOrderByPxh(false);

        List<Script> listByCondition = this.getListByCondition(condition);

        Integer tempPsh = 0;

        if (listByCondition.size() > 1) {
            Integer index = listByCondition.size() - 1;
            if (listByCondition.get(index).getPsh() == currentPsh) {
                return listByCondition;
            } else {
                // 获取
                for (int i = 0; i < listByCondition.size(); i++) {
                    Script script = listByCondition.get(i);
                    Integer itemPsh = script.getPsh();
                    if (itemPsh == currentPsh) {
                        Script bScript = listByCondition.get(i + 1);
                        Integer bPsh = bScript.getPsh();
                        tempPsh = bPsh;
                        bScript.setPsh(currentPsh);
                        scriptById.setPsh(tempPsh);
                        this.updateScriptById(bScript, bScript.getId());
                        this.updateScriptById(scriptById, scriptById.getId());
                        break;
                    }
                }
            }
        }

        return this.getListByCondition(condition);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Script mapToEntity(final ScriptDTO scriptDTO, final Script script) {
        BeanUtils.copyProperties(scriptDTO, script);
        return script;
    }

    public ScriptDTO mapToDTO(final Script script, final ScriptDTO scriptDTO) {
        BeanUtils.copyProperties(script, scriptDTO);
        return scriptDTO;
    }

    @Override
    public Integer getMaxPxh(String userid, String copyId) {
        Integer maxPxh = 0;
        ScriptDTO scriptDTO = new ScriptDTO();
        scriptDTO.setCopyId(copyId);
        scriptDTO.setUserId(userid);
        scriptDTO.setOrderByPxh(true);
        List<Script> scirpts = this.getListByCondition(scriptDTO);
        if (scirpts.size() > 0) {
            Optional<Script> max = scirpts.stream().max(Comparator.comparingInt(one -> one.getPxh()));
            if (max.isPresent()) {
                maxPxh = max.get().getPxh();
            }
        }
        return maxPxh;
    }

    @Override
    public Map<String,Object> getImportScriptResult(MultipartFile file) throws Exception {
        Map<String,Object> result = new HashMap<String,Object>();
        ScriptDataImportListener scriptDataImportListener = new ScriptDataImportListener();
        String originalName = file.getOriginalFilename();
        String extension = FileNameUtil.getSuffix(originalName);
        if (extension.equals("xlsx") || extension.equals("xls")) {
            try (final InputStream inputStream = file.getInputStream()) {
                ExcelReaderBuilder read = EasyExcel.read(inputStream, ScriptExcelDTO.class, scriptDataImportListener).headRowNumber(3);
                read.sheet().doRead();
                List<Script> scriptList = scriptDataImportListener.getScriptList();
                String errorMsg = scriptDataImportListener.getErrorMsg();
                result.put("error",errorMsg);
                result.put("list",scriptList);
            } catch (IOException e) {
                final String errorMsg = "解析 Excel 文档出错";
                log.error(errorMsg, e);
                throw new Exception("解析 Excel 文档出错");
            }
        } else {
            final String errorMsg = "文件格式错误";
            log.error(errorMsg);
            throw new Exception("导入的文件格式错误");
        }
        return result;
    }

    @Override
    public Integer getScriptNumByUserId(String id) {
        return scriptDao.countByUserId(id);
    }

    @Override
    public Integer getScriptCountByCopyId(String copyId) {
        return scriptDao.countByCopyId(copyId);
    }
}