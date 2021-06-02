import {Mix, Selectable, Selection} from "entcore-toolkit";
import {idiom, notify} from "entcore";
import {schoolService} from "../services";

export class School implements Selectable {
    owner: { userId: string; displayName: string };

    id: number;
    idneo: string;
    uai: string;
    nom: string;
    principal: boolean;
    id_principal: number;
    selected: boolean;

    constructor() {
        this.id = null;
        this.idneo = null;
        this.uai = null;
        this.nom = null;
        this.principal = null;
        this.id_principal = null;
        this.selected = false;
    }

    toJson() : Object {
        return {
            id: this.id,
            idneo: this.idneo,
            uai: this.uai,
            nom: this.nom,
            principal: this.principal,
            id_principal: this.id_principal,
        }
    }
}

export class Schools extends Selection<School> {
    all: School[];

    constructor() {
        super([]);
    }

    sync = async () : Promise<void> => {
        this.all = [];
        try {
            let { data } = await schoolService.list();
            this.all = Mix.castArrayAs(School, data);
        } catch (e) {
            notify.error(idiom.translate('pmb.error.schools.sync'));
            throw e;
        }
    };

    syncNeo = async () : Promise<void> => {
        this.all = [];
        try {
            let { data } = await schoolService.listNeo();
            this.all = Mix.castArrayAs(School, data);
        } catch (e) {
            notify.error(idiom.translate('pmb.error.schools.sync'));
            throw e;
        }
    };
}