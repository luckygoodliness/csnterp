Ext.define("Brief.store.BriefStore", {
	extend: 'Ext.data.Store',
model: 'Brief.model.BriefModel',
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