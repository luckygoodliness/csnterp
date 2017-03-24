Ext.define("Scmcontractchange.store.ScmContractChangeStore", {
	extend: 'Ext.data.Store',
model: 'Scmcontractchange.model.ScmContractChangeModel',
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