import {idiom, ng, notify} from 'entcore';
import http, {AxiosResponse} from 'axios';
import {School} from '../models';

export interface SchoolService {
    list() : Promise<AxiosResponse>;
    listNeo() : Promise<AxiosResponse>;
    create(schools: School[]) : Promise<AxiosResponse>;
    delete(schoolId: number) : Promise<AxiosResponse>;
}

export const schoolService: SchoolService = {

    async list() : Promise<AxiosResponse> {
        try {
            return http.get('/pmb/schools');
        } catch (err) {
            notify.error(idiom.translate('pmb.error.schoolService.list'));
            throw err;
        }
    },

    async listNeo() : Promise<AxiosResponse> {
        try {
            return http.get('/pmb/schools/neo');
        } catch (err) {
            notify.error(idiom.translate('pmb.error.schoolService.list'));
            throw err;
        }
    },

    async create(schools: School[]) : Promise<AxiosResponse> {
        try {
            return http.post('/pmb/schools', schools);
        } catch (err) {
            notify.error(idiom.translate('pmb.error.schoolService.create'));
            throw err;
        }
    },

    async delete(schoolId: number) : Promise<AxiosResponse> {
        try {
            return await http.delete(`/pmb/schools/${schoolId}`);
        } catch (err) {
            notify.error(idiom.translate('pmb.error.schoolService.delete'));
            throw err;
        }
    }

};

export const SchoolService = ng.service('SchoolService', (): SchoolService => schoolService);