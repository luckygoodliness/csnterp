Ext.define("Scmsae.store.ScmSaeFormStore", {
	extend: 'Ext.data.Store',
model: 'Scmsae.model.ScmSaeFormModel',
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