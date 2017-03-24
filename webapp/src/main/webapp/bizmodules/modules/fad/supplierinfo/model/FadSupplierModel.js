Ext.define("FadSupplierinfo.model.FadSupplierModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'supplierCode', 'supplierCodeForGroup', 'inoutType', 'completeName', 'oldName', 'englishName', 'simpleName', 'supplierOthername', 'industryType', 'apName', 'apId', 'registeredCapital', 'registeredCapitalCurrency', 'fixedAsset', 'fixedAssetCurrency', 'registeredDate', 'employeeNumber', 'contactInfo', 'url', 'country', 'province', 'city', 'registeredAddress', 'postcode', 'businessScope', 'companyIntroduce', 'mainClient', 'swiftCode', 'startDate', 'endDate', 'supplierStatus', 'supplierType', 'enterpriseScale', 'supplierProperty', 'organizationType', 'enterpriseType', 'levelCode', 'taxRegistrationNo', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'seqNo', 'state', 'isEBusiness','ncCode', 'taxTypes', 'supplierGenre', 'inoutTypeCombo', 'supplierStatusCombo']
    }
);