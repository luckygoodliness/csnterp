Ext.define("Balanceofcontract.store.ScmContractStore", {
	extend: 'Ext.data.Store',
model: 'Balanceofcontract.model.ScmContractModel',
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