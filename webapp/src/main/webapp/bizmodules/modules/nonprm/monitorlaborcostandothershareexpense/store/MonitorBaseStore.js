Ext.define("Monitorlaborcostandothershareexpense.store.MonitorBaseStore", {
	extend: 'Ext.data.Store',
model: 'Monitorlaborcostandothershareexpense.model.MonitorBaseModel',
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