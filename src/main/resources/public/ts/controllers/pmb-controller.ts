import {ng, template} from 'entcore';
import {School, Schools} from "../models";
import {emailSendService, schoolService} from "../services";

interface ViewModel {
    schools: Schools;
    neoSchools: Schools;
    mainSchools: School[];
    secondarySchools: School[];
    selectedSchoolId : number;
    lightbox: {
        create: boolean,
        attach: boolean,
        delete: boolean
    };

    openCreate() : void;
    doCreate() : Promise<void>;
    closeCreate() : void;
    openAttach(selectedSchoolId: number) : void;
    doAttach() : Promise<void>;
    closeAttach() : void;
    openDelete(selectedSchoolId: number) : void;
    doDelete() : Promise<void>;
    closeDelete() : void;
    getSchool(schoolId: number) : School;
    getSecondarySchools(mSchool: School) : School[];
}


export const pmbController = ng.controller('PmbController', ['$scope',
    function ($scope) {

    const vm: ViewModel = this;

    const init = async () : Promise<void> => {
        vm.lightbox = {
            create: false,
            attach: false,
            delete: false
        };
        vm.schools = new Schools();
        vm.neoSchools = new Schools();
        await vm.schools.sync();
        await vm.neoSchools.syncNeo();
        let schoolsUAIs : any = [];
        for (let school of vm.schools.all) {
            schoolsUAIs.push(school.uai);
        }
        vm.neoSchools.all = vm.neoSchools.all.filter(neoSchool => !schoolsUAIs.includes(neoSchool.uai));
        vm.neoSchools.forEach(school =>  {
            school.toString = () => school.uai + " - " + school.nom;
            school.search = school.uai + school.nom;
        });
        vm.mainSchools = vm.schools.all.filter(school => school.principal);
        vm.secondarySchools = vm.schools.all.filter(school => !school.principal);
        vm.selectedSchoolId = null;
        $scope.safeApply();
    };

    vm.openCreate = () : void => {
        template.open('lightbox', 'lightbox/create');
        vm.lightbox.create = true;
    }

    vm.doCreate = async () : Promise<void> => {
        try {
            let schools = $scope.filterChoice.schools;
            for (let school of schools) {
                school.principal = true;
            }
            await schoolService.create(schools);
            vm.closeCreate();
        } catch (e) {
            throw e;
        }
    }

    vm.closeCreate = () : void => {
        template.close('lightbox');
        init();
    }

    vm.openAttach = (selectedSchoolId: number) : void => {
        vm.selectedSchoolId = selectedSchoolId;
        template.open('lightbox', 'lightbox/attach');
        vm.lightbox.attach = true;
    }

    vm.doAttach = async () : Promise<void> => {
        try {
            let schools = $scope.filterChoice.schools;
            for (let school of schools) {
                school.principal = false;
                school.id_principal = vm.selectedSchoolId;
            }
            await schoolService.create(schools);
            vm.closeAttach();
        } catch (e) {
            throw e;
        }
    }

    vm.closeAttach = () : void => {
        template.close('lightbox');
        init();
    }

    vm.openDelete = (selectedSchoolId: number) : void => {
        vm.selectedSchoolId = selectedSchoolId;
        template.open('lightbox', 'lightbox/delete');
        vm.lightbox.delete = true;
    }

    vm.doDelete = async () : Promise<void> => {
        try {
            await schoolService.delete(vm.selectedSchoolId);
            vm.closeDelete();
            await emailSendService.send(vm.schools.all.filter(school => school.id == vm.selectedSchoolId
                || school.id_principal == vm.selectedSchoolId));
        } catch (e) {
            throw e;
        }
    }

    vm.closeDelete = () : void => {
        template.close('lightbox');
        init();
    }

    vm.getSchool = (schoolId: number) : School => {
        return vm.schools.all.filter(school => school.id == schoolId)[0];
    }

    vm.getSecondarySchools = (mSchool: School) : School[] => {
        return vm.secondarySchools.filter(school => school.id_principal == mSchool.id);
    };

    $scope.filterChoice = {
        schools: []
    };

    init();
}]);