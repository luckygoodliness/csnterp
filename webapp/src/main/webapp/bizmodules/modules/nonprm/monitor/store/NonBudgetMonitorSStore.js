Ext.define("Monitor.store.NonBudgetMonitorSStore", {
	extend: 'Ext.data.Store',
model: 'Monitor.model.NonBudgetMonitorSModel',
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