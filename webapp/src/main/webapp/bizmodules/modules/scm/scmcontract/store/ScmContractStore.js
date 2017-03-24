Ext.define("Scmcontract.store.ScmContractStore", {
	extend: 'Ext.data.Store',
model: 'Scmcontract.model.ScmContractModel',
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