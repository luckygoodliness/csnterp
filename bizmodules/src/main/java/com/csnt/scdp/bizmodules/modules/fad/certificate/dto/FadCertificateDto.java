package com.csnt.scdp.bizmodules.modules.fad.certificate.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.fad.FadCertificate;

import java.util.List;


/**
 * Description:  FadCertificateDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-16 10:07:48
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.fad.FadCertificate")
public class FadCertificateDto extends FadCertificate {

    @CascadeChilds(FK = "fadCertificateId|uuid")
    private List<FadCertificateDetailDto> fadCertificateDetailDto;

    public List<FadCertificateDetailDto> getFadCertificateDetailDto() {
        return fadCertificateDetailDto;
    }

    public void setFadCertificateDetailDto(List<FadCertificateDetailDto> childDto) {
        this.fadCertificateDetailDto = childDto;
    }
}