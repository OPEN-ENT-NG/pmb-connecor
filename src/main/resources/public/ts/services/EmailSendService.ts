import {idiom, ng, notify} from 'entcore';
import http, {AxiosResponse} from 'axios';
import {School} from '../models';

export interface EmailSendService {
    send(schools: School[]) : Promise<AxiosResponse>;
}

export const emailSendService: EmailSendService = {

    async send(schools: School[]) : Promise<AxiosResponse> {
        try {
            return http.post('/pmb/email/send', schools);
        } catch (err) {
            notify.error(idiom.translate('pmb.error.emailSendService.send'));
            throw err;
        }
    }

};

export const EmailSendService = ng.service('EmailSendService', (): EmailSendService => emailSendService);