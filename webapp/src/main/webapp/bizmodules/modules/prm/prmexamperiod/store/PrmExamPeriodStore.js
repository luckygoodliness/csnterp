Ext.define("PrmExamPeriod.store.PrmExamPeriodStore", {
	extend: 'Ext.data.Store',
model: 'PrmExamPeriod.model.PrmExamPeriodModel',
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