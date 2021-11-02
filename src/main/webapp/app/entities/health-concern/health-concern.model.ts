import { IVitalSign } from 'app/entities/vital-sign/vital-sign.model';

export interface IHealthConcern {
  id?: number;
  name?: string;
  vitalSigns?: IVitalSign[] | null;
}

export class HealthConcern implements IHealthConcern {
  constructor(public id?: number, public name?: string, public vitalSigns?: IVitalSign[] | null) {}
}

export function getHealthConcernIdentifier(healthConcern: IHealthConcern): number | undefined {
  return healthConcern.id;
}
