import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IHealthConcern } from 'app/entities/health-concern/health-concern.model';

export interface IVitalSign {
  id?: number;
  weightInPounds?: number | null;
  heightInInches?: number | null;
  bmi?: number | null;
  glassOfWater?: number | null;
  systolic?: number | null;
  diastolic?: number | null;
  currentBloodSugar?: number | null;
  lipidProfile?: number | null;
  appUser?: IAppUser;
  healthConcerns?: IHealthConcern[];
}

export class VitalSign implements IVitalSign {
  constructor(
    public id?: number,
    public weightInPounds?: number | null,
    public heightInInches?: number | null,
    public bmi?: number | null,
    public glassOfWater?: number | null,
    public systolic?: number | null,
    public diastolic?: number | null,
    public currentBloodSugar?: number | null,
    public lipidProfile?: number | null,
    public appUser?: IAppUser,
    public healthConcerns?: IHealthConcern[]
  ) {}
}

export function getVitalSignIdentifier(vitalSign: IVitalSign): number | undefined {
  return vitalSign.id;
}
