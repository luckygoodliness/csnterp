package com.csnt.scdp.bizmodules.modules.ztest.ztestcode.dto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.ztest.ZtrainMain;

import java.util.List;


/**
 * Description:  ZtrainMainDto
 * Copyright: © 2017 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * 2017-02-24 09:51:18
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.ztest.ZtrainMain")
public class ZtrainMainDto extends ZtrainMain {

    @CascadeChilds(FK = "puuid|uuid")
    private List<ZtrainMainContentDto> ztrainMainContentDto;



    public List<ZtrainMainContentDto> getZtrainMainContentDto() {
        return ztrainMainContentDto;
    }

    public void setZtrainMainContentDto(List<ZtrainMainContentDto> childDto) {
        this.ztrainMainContentDto = childDto;
    }

    //页面编辑区edit-layout中 客户名称customerName表里面没有，为了展现出来，手动加上的
    //  所以要对应加上虚字段 ，用于关联展示出客户名称的名称，方便查询客户表中的字段赋值给这个虚字段。
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }




}