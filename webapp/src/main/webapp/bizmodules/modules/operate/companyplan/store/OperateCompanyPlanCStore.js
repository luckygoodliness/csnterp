Ext.define("Companyplan.store.OperateCompanyPlanCStore", {
	extend: 'Ext.data.Store',
model: 'Companyplan.model.OperateCompanyPlanCModel',
	autoLoad: false,
	sorters: [
		{
			property: 'seqNo',
			direction: 'ASC'
		}
	],
	proxy: {
		type: 'memory',
		reader: {
			type: 'json'
		}
	}
});