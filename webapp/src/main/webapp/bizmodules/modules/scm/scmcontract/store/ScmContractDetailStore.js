Ext.define("Scmcontract.store.ScmContractDetailStore", {
	extend: 'Ext.data.Store',
model: 'Scmcontract.model.ScmContractDetailModel',
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