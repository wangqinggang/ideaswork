package cn.ideaswork.ideacoder.domain.vms.script;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ScriptDataImportListener implements ReadListener<ScriptExcelDTO> {
    private List<Script> scriptList = new ArrayList<>();
    private String errorMsg = "";

    @Override
    public void invoke(ScriptExcelDTO scriptExcelDTO, AnalysisContext analysisContext) {
        ReadRowHolder readRowHolder = analysisContext.readRowHolder();
        Integer rowIndex = readRowHolder.getRowIndex();
//        log.info("解析到第 {} 条数据:{}", rowIndex, JSON.toJSONString(scriptExcelDTO));
        if (ObjectUtil.isEmpty(scriptExcelDTO.getPxh())) {
            int size = scriptList.size();
            scriptExcelDTO.setPxh(++size);
        }
        if (ObjectUtil.isEmpty(scriptExcelDTO.getPlot())) {
            errorMsg += "第 " + (rowIndex+1) + " 行情节为空\n";
            return;
        }
        if (ObjectUtil.isEmpty(scriptExcelDTO.getShotSize())) {
//            errorMsg += "第 " + rowIndex + " 行景别不能为空\n";
            scriptExcelDTO.setShotSize("中景");
        }
        if (ObjectUtil.isEmpty(scriptExcelDTO.getShotAngle())) {
//            errorMsg += "第 " + rowIndex + " 行景别不能为空\n";
            scriptExcelDTO.setShotAngle("视平");
        }
        if (ObjectUtil.isEmpty(scriptExcelDTO.getShotMove())) {
//            errorMsg += "第 " + rowIndex + " 行景别不能为空\n";
            scriptExcelDTO.setShotMove("固定");
        }
        if (ObjectUtil.isEmpty(scriptExcelDTO.getLocation())) {
//            errorMsg += "第 " + rowIndex + " 行场景不能为空\n";
            scriptExcelDTO.setLocation("默认");
        }
        if (ObjectUtil.isEmpty(scriptExcelDTO.getContent())) {
//            errorMsg += "第 " + rowIndex + " 行拍摄内容不能为空\n";
            scriptExcelDTO.setContent(scriptExcelDTO.getPlot());
        }
        Script script = BeanUtil.copyProperties(scriptExcelDTO, Script.class);
        if (ObjectUtil.isEmpty(scriptExcelDTO.getFinished())) {
            script.setFinished(false);
        } else {
            if (scriptExcelDTO.getFinished().equals("已完成")) {
                script.setFinished(true);
            } else {
                script.setFinished(false);
            }
        }

        if (ObjectUtil.isNotEmpty(scriptExcelDTO.getPxh())) {
            script.setPsh(script.getPxh());
            scriptList.add(script);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("解析完成数据，共 : {} 条", this.scriptList.size());
    }

    public List<Script> getScriptList() {
        return scriptList;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
