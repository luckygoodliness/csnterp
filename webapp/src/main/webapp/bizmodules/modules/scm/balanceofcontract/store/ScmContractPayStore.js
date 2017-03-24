Ext.define("Balanceofcontract.store.ScmContractPayStore", {
	extend: 'Ext.data.Store',
model: 'Balanceofcontract.model.ScmContractPayModel',
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