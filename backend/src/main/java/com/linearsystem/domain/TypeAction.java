package com.linearsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeAction {

    RESUBMITTED("RESUBMITTED", "Reenviada"),
    DELETED("DELETED", "Excluída"),
    PENDENT("PENDENT", "Pendente");

    private String name;
    private String message;
}
