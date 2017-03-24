Ext.define("Weekly.store.PrmWeeklyPayStore", {
	extend: 'Ext.data.Store',
model: 'Weekly.model.PrmWeeklyPayModel',
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