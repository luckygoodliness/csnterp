Ext.define("Scmsaeappraisercase.store.ScmSaeAppraiserCaseDStore", {
	extend: 'Ext.data.Store',
model: 'Scmsaeappraisercase.model.ScmSaeAppraiserCaseDModel',
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