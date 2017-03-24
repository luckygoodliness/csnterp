Ext.define("Supplierinfor.store.ScmSukpplierAptitudeStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfor.model.ScmSukpplierAptitudeModel',
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