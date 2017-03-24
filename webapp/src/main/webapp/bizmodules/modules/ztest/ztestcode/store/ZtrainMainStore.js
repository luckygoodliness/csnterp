Ext.define("Ztestcode.store.ZtrainMainStore", {
	extend: 'Ext.data.Store',
model: 'Ztestcode.model.ZtrainMainModel',
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