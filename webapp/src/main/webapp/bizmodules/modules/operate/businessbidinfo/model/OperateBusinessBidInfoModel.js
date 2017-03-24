Ext.define("Businessbidinfo.model.OperateBusinessBidInfoModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'officeId', 'state', 'operateKeyProjectsInfoId', 'projectShowName', 'designerId', 'managementId',
            'cdmBillStateCombo', 'operateBy', 'operateTime', 'canBeDeleted', 'canBeModified', 'projectSourceType', 'canMoneyBeModified',
            'cdmTaxRegionCombo', 'operateBidResultCombo', 'officeName', 'operateByName', 'regionId', 'projectName', 'proprietorUnit',
            'comBidUnit', 'comBidNumber', 'bidingDocStart', 'bidingDocEnd', 'bidingDocPrice', 'bidBond', 'eotm', 'bod',
            'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone',
            'tblVersion', {
                name: 'isVoid',
                defaultValue: 0
            }, 'bidResult', 'priceFixing', 'contractorOfficeDesc']
    }
);