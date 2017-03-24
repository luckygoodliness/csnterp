Ext.define("Extraitem.store.NonProjectExtraItemStore", {
	extend: 'Ext.data.Store',
model: 'Extraitem.model.NonProjectExtraItemModel',
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