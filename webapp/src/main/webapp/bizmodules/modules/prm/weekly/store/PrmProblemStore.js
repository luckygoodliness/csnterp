Ext.define("Weekly.store.PrmProblemStore", {
	extend: 'Ext.data.Store',
model: 'Weekly.model.PrmProblemModel',
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