Ext.define("Card.model.AssetCardTransferModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'cardCode', 'assetCode', 'assetName', 'state', 'assetTypeCode', 'deviceType', 'specification', 'model',
            'storeplace', 'status', 'officeId', 'officeIdDesc', 'endUserCode', 'endUserName', 'liablePerson', 'fromNc', 'purchaseTime',
            'discardTime', 'limitMonth', 'localValue', 'monthDepreciation', 'netValue', 'factoryName', 'releaseDate', 'identificationNumber', 'buildingProperty', 'area',
            'chassisNumber', 'vehicleNumber', 'vehicleType', 'authorizationCode', 'checkedDate',
            'validDate', 'annualCheckExpiredDate', 'insuranceExpiredDate', 'accessory', 'descp', 'companyCode', 'companyName',
            'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'unit',
            {
                name: 'isVoid',
                defaultValue: 0
            }, 'locTimezone', 'tblVersion', 'source', 'operationUnit', 'operator', 'operatorTel', 'assetHandoverUuid', 'ransferType']
    }
);