package com.csnt.scdp.bizmodules.modules.prm.receipts.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.prm.PrmReceipts;

import java.util.List;


/**
 * Description:  PrmReceiptsDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-23 09:19:57
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmReceipts")
public class PrmReceiptsDto extends PrmReceipts {
    @CascadeChilds(FK = "prmReceiptsUuid|uuid")
    private List<PrmReceiptsScmInvoiceDto> prmReceiptsScmInvoiceDto;

    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    public List<PrmReceiptsScmInvoiceDto> getPrmReceiptsScmInvoiceDto() {
        return prmReceiptsScmInvoiceDto;
    }

    public void setPrmReceiptsScmInvoiceDto(List<PrmReceiptsScmInvoiceDto> prmReceiptsScmInvoiceDto) {
        this.prmReceiptsScmInvoiceDto = prmReceiptsScmInvoiceDto;
    }

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
        this.cdmFileRelationDto = childDto;
    }
}