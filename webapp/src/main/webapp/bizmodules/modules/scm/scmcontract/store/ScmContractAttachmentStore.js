Ext.define("Scmcontract.store.ScmContractAttachmentStore", {
	extend: 'Ext.data.Store',
model: 'Scmcontract.model.ScmContractAttachmentModel',
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