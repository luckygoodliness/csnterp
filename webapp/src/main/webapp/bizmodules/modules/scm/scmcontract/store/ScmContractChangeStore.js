Ext.define("Scmcontract.store.ScmContractChangeStore", {
	extend: 'Ext.data.Store',
model: 'Scmcontract.model.ScmContractChangeModel',
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