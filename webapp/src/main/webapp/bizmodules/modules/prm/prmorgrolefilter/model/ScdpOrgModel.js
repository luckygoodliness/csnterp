Ext.define("Prmorgrolefilter.model.ScdpOrgModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','puuid','seqNo','orgCode','orgType','orgName',{name: 'isActive', defaultValue: 0},'currency','thousandSeparator','decimalSeparator','dateFormat','address1','address2','address3','address4','orgTel','orgFax','orgMail','orgQq','orgWeixin','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','cityCode','contacts','countryCode','shortCode','stateCode','validFrom','validTo','weekStart']
	}
);