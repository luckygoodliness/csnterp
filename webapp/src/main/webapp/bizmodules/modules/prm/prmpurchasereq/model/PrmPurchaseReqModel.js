Ext.define("Prmpurchasereq.model.PrmPurchaseReqModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'purchaseReqNo', 'bugdetId', 'subjectCode', 'state', 'stateNm', 'projectName', 'remark', 'companyCode', 'departmentCode', 'officeId', 'orgName', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'isProject', 'prmPurchaseReqDetailDto', 'prmPurchaseReqDetailDto_c', 'innerSupplierDesc','departmentCodeDesc']
    }
);