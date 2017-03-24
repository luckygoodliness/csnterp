Ext.define("Type.store.AssetTypeStore", {
	extend: 'Ext.data.Store',
model: 'Type.model.AssetTypeModel',
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