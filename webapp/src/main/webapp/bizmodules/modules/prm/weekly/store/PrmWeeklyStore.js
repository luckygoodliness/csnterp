Ext.define("Weekly.store.PrmWeeklyStore", {
	extend: 'Ext.data.Store',
model: 'Weekly.model.PrmWeeklyModel',
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