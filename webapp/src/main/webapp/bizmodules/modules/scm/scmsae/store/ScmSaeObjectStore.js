Ext.define("Scmsae.store.ScmSaeObjectStore", {
	extend: 'Ext.data.Store',
model: 'Scmsae.model.ScmSaeObjectModel',
	autoLoad: false,
	sorters: [
		{
			property: 'supplierName',
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