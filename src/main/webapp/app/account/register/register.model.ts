import { Gender } from 'app/entities/enumerations/gender.model';
import * as dayjs from 'dayjs';

export class Registration {
  constructor(
    public login: string,
    public firstName: string,
    public lastName: string,
    public email: string,
    public password: string,
    public langKey: string,
    public mobileNumber: string,
    public birthday: dayjs.Dayjs,
    public gender: Gender,
    public city: string,
    public country: string
  ) {}
}
