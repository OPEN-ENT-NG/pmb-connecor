import { Behaviours } from 'entcore';

const rights = {
    workflow: {
        access: 'fr.openent.pmb.controllers.PmbController|render'
    }
};

Behaviours.register('pmb', {
    rights: rights,
    dependencies: {}
});
