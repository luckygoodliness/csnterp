Ext.define("Ztestcode.store.ZtrainMainContentStore", {
	extend: 'Ext.data.Store',
model: 'Ztestcode.model.ZtrainMainContentModel',
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