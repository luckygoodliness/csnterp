package com.csnt.scdp.bizmodules.modules.workflow.common.intf;

import java.util.Map;

/**
 * Description:  ErpWorkflowManager
 * Copyright: © 2015 Aardwolf Studio. All rights reserved.
 * Company:CSNT中海网络科技股份有限公司
 *
 * @author liujingyuan
 * @version 1.0
 */
public interface ErpWorkflowManager {

    void start(Map contextMap);

    void complete(Map contextMap);

    void assign(Map contextMap);

    void reject(Map contextMap);

    void cancel(Map contextMap);
}

