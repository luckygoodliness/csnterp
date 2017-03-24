Ext.define("Companyplan.store.OperateCompanyPlanHStore", {
	extend: 'Ext.data.Store',
model: 'Companyplan.model.OperateCompanyPlanHModel',
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