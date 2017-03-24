Ext.define("Card.model.AssetCardHistoryModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'assetCardUuid', 'historyField', 'cardCode', 'assetCode', 'assetName', 'assetTypeCode', 'deviceType', 'specification', 'model', 'storeplace', 'state', 'officeId', 'endUserCode', 'endUserName', 'liablePerson', 'fromNc', 'purchaseTime', 'discardTime', 'limitMonth', 'localValue', 'monthDepreciation', 'netValue', 'factoryName', 'releaseDate', 'identificationNumber', 'buildingProperty', 'area', 'chassisNumber', 'vehicleNumber', 'vehicleType', 'authorizationCode', 'checkedDate', 'validDate', 'annualCheckExpiredDate', 'insuranceExpiredDate', 'accessory', 'descp', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', {
            name: 'isVoid',
            defaultValue: 0
        }, 'locTimezone', 'tblVersion', 'source', 'seqNo', 'operationUnit', 'operator', 'operatorTel', 'status', 'unit']
    }
);