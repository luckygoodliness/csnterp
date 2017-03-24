Ext.define("Weekly.store.PrmBriefStore", {
	extend: 'Ext.data.Store',
model: 'Weekly.model.PrmBriefModel',
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