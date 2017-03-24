package com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf;

import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:  PayreqManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:42:25
 */
public interface PayreqManager {

    /**
     * 设置支付明细的合同相关字段,需要从合同表中获取
     *
     * @param lst 格式 "'111','eee'" ,若uuids为空，则不加过滤
     * @return 结果
     */
    void setObjectPlusInfo(List<FadPayReqCDto> lst);

    void setObjectPlusInfo(FadPayReqHDto hdto);

    /**
     * 添加lst中没有的支付明细信息，需要从合同表中获取
     *
     * @param filter 过滤条件
     * @return 结果
     */
    void addContract2Object(List<FadPayReqCDto> lst, String filter);

    /**
     * 获取支付申请信息
     *
     * @param mapConditions
     * @return
     */
    List<FadPayReqH> getFadPayReqHbyCondition(Map<String, Object> mapConditions);

    /**
     * 获取最新年月的支付申请
     *
     * @return
     */
    FadPayReqH getLatestHandledPayReqH();

    /**
     * @param uuid
     * @return
     */
    FadPayReqH getFadPayReqHbyUuid(String uuid);

    /**
     * @param uuids
     * @return
     */
    List<FadPayReqC> getFadPayReqCbyUuids(List uuids);

    void updateFadPayReqHBillState(String uuid, String state);

    /**
     * 获取主表相关的字表数据
     *
     * @param puuid
     */
    List<FadPayReqC> getFadPayReqCbyPuuid(String puuid);

    /**
     * 过滤数据，若是事业部则按部门过滤
     *
     * @param lst
     * @param needFilter
     * @param userId
     * @return
     */
    List<FadPayReqCDto> filterFadPayReqCbyRight(List<FadPayReqCDto> lst, Boolean needFilter, String userId);

    void update(FadPayReqC fadPayReqC);

    /**
     * 关闭当月之前所有的月度支付信息
     *
     * @param fadPayReqh
     */
    void closePreviousMonthReq(FadPayReqH fadPayReqh);

    void endorseMonthReqChildren(FadPayReqH fadPayReqh);

    //获取支付明细的凭证号
    Map<String, Map> getFadCertificateNo(FadPayReqH fadPayReqh);

    //获取支供应商支付信息
    Map<String, Map> getScmSupplierPayInfo(String uuidsOrSql);

    public void sendMsg(String uuid, String month);

    Integer closeObsoletedPayment(int type,int months, int days);

}