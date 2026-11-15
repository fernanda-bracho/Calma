package com.example.calmafinal.data.mapper;

import com.example.calmafinal.data.entities.ContactoEmergenciaEntity;
import com.example.calmafinal.domain.model.ContactoEmergencia;

public final class ContactoMapper {

    private ContactoMapper() {}

    public static ContactoEmergencia aDominio(ContactoEmergenciaEntity e) {
        return new ContactoEmergencia(e.getId(), e.getNombre(), e.getTelefono(), e.getRelacion());
    }

    public static ContactoEmergenciaEntity aEntidad(long usuarioId, ContactoEmergencia c) {
        ContactoEmergenciaEntity e = new ContactoEmergenciaEntity(
                usuarioId, c.getNombre(), c.getTelefono(), c.getRelacion());
        e.setId(c.getId());
        return e;
    }
}
