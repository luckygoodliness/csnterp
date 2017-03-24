Ext.define("Notesplan.model.ScmNotesPlanDetailModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'scmNotesPlanId', 'scmContractCode', 'supplierId', 'supplierName', 'prmProjectMainId', 'conclusionLineIn', 'conclusionLineOut', 'conclusionLineContract', 'conclusionLineFinancial', 'conclusionRateIn', 'conclusionRateOut', 'conclusionRateContract', 'conclusionRateReceipt', 'scmContractAmount', 'scmContractInvoice', 'paid', 'remark', 'companyCode', 'companyName', 'createBy', 'createTime', 'updateBy', 'updateTime', {
            name: 'isVoid',
            defaultValue: 0
        }, 'locTimezone', 'tblVersion', 'seqNo', 'markTime', 'createByContract',
            //额外字段
            'createByContractName', 'officeName', 'prmProjectMainIdName', 'projectId']
    }
);