Ext.define("Prmunknownreceipts.store.PrmUnknownReceiptsStore", {
	extend: 'Ext.data.Store',
model: 'Prmunknownreceipts.model.PrmUnknownReceiptsModel',
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