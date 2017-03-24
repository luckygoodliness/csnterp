Ext.define("Archiving.store.PrmArchivingStore", {
	extend: 'Ext.data.Store',
model: 'Archiving.model.PrmArchivingModel',
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