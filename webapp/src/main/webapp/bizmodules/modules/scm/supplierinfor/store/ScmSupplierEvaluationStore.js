Ext.define("Supplierinfor.store.ScmSupplierEvaluationStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfor.model.ScmSupplierEvaluationModel',
	autoLoad: false,
	sorters: [
		{
			property: 'createTime',
			direction: 'DESC'
		}
	],
	proxy: {
		type: 'memory',
		reader: {
			type: 'json'
		}
	}
});