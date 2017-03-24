package com.csnt.scdp.bizmodules.modules.scm.notesplan.services.intf;

import java.util.Map;

/**
 * Description:  NotesplanManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-04 15:02:50
 */
public interface NotesplanManager {
    /**
     * 在编辑页面里点击自定义的查询按钮时的加载数据方法
     *
     * @param inMap
     * @return
     */
    Map loadDataDefault(Map inMap);

    /**
     * 记录时间（如果数据库无实体数据，则新增数据，否则更新mark_time字段)
     *
     * @param inMap
     * @return
     */
    Map markTime(Map inMap);

    /**
     * 新增下一期记录
     *
     * @param inMap
     * @return
     */
    Map addNewIssue(Map inMap);

    /**
     * 锁定一条记录
     *
     * @param inMap
     * @return
     */
    Map lockIssue(Map inMap);

    /**
     * 保存
     *
     * @param inMap
     * @return
     */
    Map save(Map inMap);
}