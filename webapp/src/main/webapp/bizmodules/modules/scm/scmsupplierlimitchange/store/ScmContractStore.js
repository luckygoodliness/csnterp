Ext.define("Scmsupplierlimitchange.store.ScmContractStore", {
	extend: 'Ext.data.Store',
model: 'Scmsupplierlimitchange.model.ScmContractModel',
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