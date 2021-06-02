package fr.openent.pmb.security;

import fr.openent.pmb.Pmb;

public enum WorkflowActions {
    STRUCTURE_EXPORT_RIGHT (Pmb.STRUCTURE_EXPORT_RIGHT);

    private final String actionName;

    WorkflowActions(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public String toString () {
        return this.actionName;
    }
}
