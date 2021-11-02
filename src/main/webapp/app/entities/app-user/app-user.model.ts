import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IAppUser {
  id?: number;
  mobileNumber?: string | null;
  birthday?: dayjs.Dayjs;
  gender?: Gender;
  profileImageContentType?: string | null;
  profileImage?: string | null;
  city?: string | null;
  country?: string | null;
  user?: IUser;
}

export class AppUser implements IAppUser {
  constructor(
    public id?: number,
    public mobileNumber?: string | null,
    public birthday?: dayjs.Dayjs,
    public gender?: Gender,
    public profileImageContentType?: string | null,
    public profileImage?: string | null,
    public city?: string | null,
    public country?: string | null,
    public user?: IUser
  ) {}
}

export function getAppUserIdentifier(appUser: IAppUser): number | undefined {
  return appUser.id;
}
