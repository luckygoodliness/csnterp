Ext.define("Transfer.model.AssetTransferModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'ransferApplyId', 'outOfficeIdDesc', 'outPersonCode', 'inPersonCode', 'inOfficeIdDesc', 'applyDate', 'state', 'cardUuid', 'outPersonName', 'outOfficeId', 'cardCode', 'operatePerson', 'inPersonName', 'inOfficeId', 'companyCode', 'companyName', 'departmentCode',
            'createBy', 'createOffice', 'outLiablePerson', 'outLiablePersonDesc', 'inLiablePerson', 'inLiablePersonDesc', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', {
                name: 'isVoid',
                defaultValue: 0
            }, 'locTimezone', 'tblVersion', 'remark', 'ransferType']
    }
);