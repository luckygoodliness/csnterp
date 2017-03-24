Ext.define("Scmcontractchange.store.ScmContractChangeDStore", {
	extend: 'Ext.data.Store',
model: 'Scmcontractchange.model.ScmContractChangeDModel',
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