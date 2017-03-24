Ext.define("Progress.store.ProgressStore", {
	extend: 'Ext.data.Store',
model: 'Progress.model.PrmProgressModel',
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