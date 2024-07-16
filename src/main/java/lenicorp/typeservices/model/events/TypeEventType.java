package lenicorp.typeservices.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum TypeEventType
{
    CREATE_TYPE("Création d'un type"),
    UPDATE_TYPE("modification d'un type"),
    DELETE_TYPE("Suppression d'un type"),
    RESTORE_TYPE("Restoration d'un type"),
    ADD_SOUS_TYPE("Ajout d'un sous type"),
    RESTORE_SOUS_TYPE("Restoration d'un sous type"),
    REMOVE_SOUS_TYPE("Suppression d'un sous type");
    private String event;
}
