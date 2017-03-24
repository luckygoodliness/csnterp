Ext.define("Apply.store.AssetDiscardApplyStore", {
	extend: 'Ext.data.Store',
model: 'Apply.model.AssetDiscardApplyModel',
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