export type FormFieldType = 'text';

export type FormFieldValue = string | number | boolean;

export declare type FormFieldConstraints = Partial<{
    required: boolean;
    min: number | string;
    max: number | string;
    maxLength: number;
    minLength: number;
}>;

export interface FormFieldData {
    type: FormFieldType;
    name: string;
    value: FormFieldValue;
    placeholder?: string;
    disabled: boolean;
    constraints: FormFieldConstraints;
}

export interface FormData {
    name: string;
    fields: FormFieldData[];
    error?: boolean;
}
