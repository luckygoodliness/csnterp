Ext.define("Supplierinfor.store.ScmSupplierContactsStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfor.model.ScmSupplierContactsModel',
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