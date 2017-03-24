Ext.define("Certificate.store.FadCertificateAccountStore", {
	extend: 'Ext.data.Store',
model: 'Certificate.model.FadCertificateAccountModel',
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