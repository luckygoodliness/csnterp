package com.csnt.scdp.bizmodules.modules.fad.certificate.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.fad.FadCertificateDetail;

import java.util.List;


/**
 * Description:  FadCertificateDetailDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-16 10:07:48
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.fad.FadCertificateDetail")
public class FadCertificateDetailDto extends FadCertificateDetail {

    @CascadeChilds(FK = "fadCertificateDetailId|uuid")
    private List<FadCertificateAccountDto> fadCertificateAccountDto;

    public List<FadCertificateAccountDto> getFadCertificateAccountDto() {
        return fadCertificateAccountDto;
    }

    public void setFadCertificateAccountDto(List<FadCertificateAccountDto> childDto) {
        this.fadCertificateAccountDto = childDto;
    }

}