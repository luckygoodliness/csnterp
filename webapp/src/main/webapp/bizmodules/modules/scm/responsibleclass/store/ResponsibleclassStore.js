Ext.define("Responsibleclass.store.ResponsibleclassStore", {
	extend: 'Ext.data.Store',
model: 'Responsibleclass.model.ResponsibleclassModel',
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