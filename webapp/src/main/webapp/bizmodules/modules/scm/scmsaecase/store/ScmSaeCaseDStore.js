Ext.define("Scmsaecase.store.ScmSaeCaseDStore", {
	extend: 'Ext.data.Store',
model: 'Scmsaecase.model.ScmSaeCaseDModel',
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