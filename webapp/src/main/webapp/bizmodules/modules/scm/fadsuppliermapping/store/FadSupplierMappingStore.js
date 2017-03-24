Ext.define("Fadsuppliermapping.store.FadSupplierMappingStore", {
	extend: 'Ext.data.Store',
model: 'Fadsuppliermapping.model.FadSupplierMappingModel',
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