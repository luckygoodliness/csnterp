Ext.define("Scmsaeapproval.store.ScmSaeObjectStore", {
	extend: 'Ext.data.Store',
model: 'Scmsaeapproval.model.ScmSaeObjectModel',
	autoLoad: false,
	sorters: [
		{
			property: 'comprehensive',
			direction: 'desc'
		}
	],
	proxy: {
		type: 'memory',
		reader: {
			type: 'json'
		}
	}
});