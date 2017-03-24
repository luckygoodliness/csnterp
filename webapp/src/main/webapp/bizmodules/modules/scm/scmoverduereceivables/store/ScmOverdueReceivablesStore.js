Ext.define("Scmoverduereceivables.store.ScmOverdueReceivablesStore", {
	extend: 'Ext.data.Store',
model: 'Scmoverduereceivables.model.ScmOverdueReceivablesModel',
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