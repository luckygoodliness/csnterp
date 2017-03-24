Ext.define("Scmsaeapproval.store.ScmSaeResultStore", {
	extend: 'Ext.data.Store',
model: 'Scmsaeapproval.model.ScmSaeResultModel',
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