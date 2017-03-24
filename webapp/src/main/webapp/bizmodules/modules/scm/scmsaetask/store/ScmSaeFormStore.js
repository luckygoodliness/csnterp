Ext.define("Scmsaetask.store.ScmSaeFormStore", {
	extend: 'Ext.data.Store',
model: 'Scmsaetask.model.ScmSaeFormModel',
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