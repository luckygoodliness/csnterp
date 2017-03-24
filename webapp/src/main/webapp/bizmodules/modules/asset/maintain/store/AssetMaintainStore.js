Ext.define("Maintain.store.AssetMaintainStore", {
	extend: 'Ext.data.Store',
model: 'Maintain.model.AssetMaintainModel',
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