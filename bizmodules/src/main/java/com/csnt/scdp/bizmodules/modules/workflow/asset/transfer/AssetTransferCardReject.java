package com.csnt.scdp.bizmodules.modules.workflow.asset.transfer;

import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("asset_transfer_card-reject")
@Transactional

public class AssetTransferCardReject extends AssetTransferCardComplete {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        return mapVar;
    }
}
