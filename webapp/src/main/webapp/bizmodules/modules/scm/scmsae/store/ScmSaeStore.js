Ext.define("Scmsae.store.ScmSaeStore", {
	extend: 'Ext.data.Store',
model: 'Scmsae.model.ScmSaeModel',
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