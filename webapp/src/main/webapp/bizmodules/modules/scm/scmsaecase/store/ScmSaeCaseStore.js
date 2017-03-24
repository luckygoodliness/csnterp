Ext.define("Scmsaecase.store.ScmSaeCaseStore", {
	extend: 'Ext.data.Store',
model: 'Scmsaecase.model.ScmSaeCaseModel',
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