package com.csnt.scdp.bizmodules.modules.scm.notesplan.action;

import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.scm.notesplan.services.intf.NotesplanManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-04 15:02:50
 */

@Scope("singleton")
@Controller("notesplan-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "notesplan-manager")
    private NotesplanManager notesplanManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        //1.替换下面的detail
        Map detailMap = notesplanManager.loadDataDefault(inMap);
        List<Map> list = (List) detailMap.get("scmNotesPlanDetailDto");
        Map scmNotesPlanDto = (Map) out.get("scmNotesPlanDto");
        List<Map> scmNoteslanDetailDto = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            scmNoteslanDetailDto.add(list.get(i));
        }
        scmNotesPlanDto.put("scmNotesPlanDetailDto", scmNoteslanDetailDto);
        out.put("scmNotesPlanDto", scmNotesPlanDto);
        return out;
    }
}
