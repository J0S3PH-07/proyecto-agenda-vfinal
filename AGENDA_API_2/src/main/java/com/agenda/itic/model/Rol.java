package com.agenda.itic.model;

import java.util.Set;

public enum Rol {
        ADMIN(Set.of("SALA_CREATE", "SALA_UPDATE", "SALA_DELETE", "USUARI_CREATE", "USUARI_UPDATE", "USUARI_DELETE")),
        PROFESSOR(Set.of("SALA_CREATE", "SALA_UPDATE", "SALA_DELETE")),
        USUARI(Set.of());
        
        private final Set<String> permisos;
        Rol(Set<String> permisos) {
            this.permisos = permisos;
        }
        public Set<String> getPermisos() {
            return permisos;
        }
}
