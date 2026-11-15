package com.example.calmafinal.data.mapper;

import com.example.calmafinal.data.entities.UsuarioEntity;
import com.example.calmafinal.domain.model.Empleado;
import com.example.calmafinal.domain.model.Usuario;
import com.example.calmafinal.domain.model.UsuarioParticular;

/**
 * Traduce entre la entidad Room y el modelo de dominio, de modo que el dominio no conozca
 * Room y la capa data no tenga reglas de negocio.
 */
public final class UsuarioMapper {

    private UsuarioMapper() {}

    public static Usuario aDominio(UsuarioEntity e) {
        if (UsuarioEntity.TIPO_EMPLEADO.equals(e.getTipo())) {
            return new Empleado(e.getId(), e.getNombreCompleto(), e.getCorreo(), e.getPasswordHash(),
                    e.getCodigoEmpresa(), e.isConvenioActivo());
        }
        return new UsuarioParticular(e.getId(), e.getNombreCompleto(), e.getCorreo(),
                e.getPasswordHash(), e.getTelefono());
    }

    /** El id queda en 0 para que Room lo autogenere al insertar; en una actualización se conserva. */
    public static UsuarioEntity aEntidad(Usuario u) {
        UsuarioEntity e = new UsuarioEntity();
        e.setId(u.getId());
        e.setNombreCompleto(u.getNombreCompleto());
        e.setCorreo(u.getCorreo());
        e.setPasswordHash(u.getPasswordHash());

        if (u instanceof Empleado) {
            Empleado emp = (Empleado) u;
            e.setTipo(UsuarioEntity.TIPO_EMPLEADO);
            e.setCodigoEmpresa(emp.getCodigoEmpresa());
            e.setConvenioActivo(emp.isConvenioActivo());
        } else if (u instanceof UsuarioParticular) {
            e.setTipo(UsuarioEntity.TIPO_PARTICULAR);
            e.setTelefono(((UsuarioParticular) u).getTelefono());
        } else {
            throw new IllegalArgumentException("Tipo de usuario no soportado: " + u.getClass().getName());
        }
        return e;
    }
}
