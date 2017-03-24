Ext.define("Weekly.store.PrmProgressStore", {
	extend: 'Ext.data.Store',
model: 'Weekly.model.PrmProgressModel',
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