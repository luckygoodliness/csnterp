Ext.define("Scmcontract.store.ScmContractPaytypeStore", {
	extend: 'Ext.data.Store',
model: 'Scmcontract.model.ScmContractPaytypeModel',
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