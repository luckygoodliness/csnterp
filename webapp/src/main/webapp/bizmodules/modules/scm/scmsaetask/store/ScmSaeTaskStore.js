Ext.define("Scmsaetask.store.ScmSaeTaskStore", {
	extend: 'Ext.data.Store',
model: 'Scmsaetask.model.ScmSaeTaskModel',
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