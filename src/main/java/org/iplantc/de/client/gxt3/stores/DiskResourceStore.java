package org.iplantc.de.client.gxt3.stores;

import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;

public class DiskResourceStore<M> extends TreeStore<M> {

    public DiskResourceStore(ModelKeyProvider<? super M> keyProvider) {
        super(keyProvider);
    }

}
