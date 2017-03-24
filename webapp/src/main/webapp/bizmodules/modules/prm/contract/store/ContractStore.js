Ext.define("Contract.store.ContractStore", {
	extend: 'Ext.data.Store',
model: 'Contract.model.ContractModel',
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