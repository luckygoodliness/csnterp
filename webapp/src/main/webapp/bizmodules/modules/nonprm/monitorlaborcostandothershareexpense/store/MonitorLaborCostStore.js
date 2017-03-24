Ext.define("Monitorlaborcostandothershareexpense.store.MonitorLaborCostStore", {
	extend: 'Ext.data.Store',
model: 'Monitorlaborcostandothershareexpense.model.MonitorLaborCostModel',
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