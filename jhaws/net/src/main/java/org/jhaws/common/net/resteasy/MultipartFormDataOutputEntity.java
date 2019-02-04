package org.jhaws.common.net.resteasy;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

public class MultipartFormDataOutputEntity {
    public static final GenericEntity<MultipartFormDataOutput> generic(MultipartFormDataOutput mdo) {
        return new GenericEntity<MultipartFormDataOutput>(mdo) {/**/};
    }

    public static final Entity<GenericEntity<MultipartFormDataOutput>> entity(GenericEntity<MultipartFormDataOutput> ge) {
        return Entity.entity(ge, MediaType.MULTIPART_FORM_DATA_TYPE);
    }

    public static final Entity<GenericEntity<MultipartFormDataOutput>> entity(MultipartFormDataOutput mdo) {
        return entity(generic(mdo));
    }
}
