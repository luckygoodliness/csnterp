Ext.define("Monitorlaborcostandothershareexpense.store.MonitorOtherShareStore", {
	extend: 'Ext.data.Store',
model: 'Monitorlaborcostandothershareexpense.model.MonitorOtherShareModel',
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