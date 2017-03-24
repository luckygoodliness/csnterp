Ext.define("Companyplan.store.OperateCompanyPlanDStore", {
	extend: 'Ext.data.Store',
model: 'Companyplan.model.OperateCompanyPlanDModel',
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