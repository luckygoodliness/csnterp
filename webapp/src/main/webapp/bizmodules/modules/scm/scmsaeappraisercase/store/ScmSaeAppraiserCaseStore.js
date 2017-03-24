Ext.define("Scmsaeappraisercase.store.ScmSaeAppraiserCaseStore", {
	extend: 'Ext.data.Store',
model: 'Scmsaeappraisercase.model.ScmSaeAppraiserCaseModel',
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