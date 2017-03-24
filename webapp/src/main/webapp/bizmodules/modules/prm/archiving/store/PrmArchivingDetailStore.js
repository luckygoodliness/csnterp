Ext.define("Archiving.store.PrmArchivingDetailStore", {
	extend: 'Ext.data.Store',
model: 'Archiving.model.PrmArchivingDetailModel',
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