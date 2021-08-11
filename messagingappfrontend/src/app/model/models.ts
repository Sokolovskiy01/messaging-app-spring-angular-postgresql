export interface AppUser {
    id: number,
    name: string,
    dateOfBirth: Date,
    gender: string,
    imageUrl: string,
    comment: string,
    email: string,
    userStatus: UserStatus,
    banMessage: string,
    lastLogin: Date
}

export interface Message {
    sender: AppUser,
    messageContent: string,
    sent: Date
}

/*
export interface Chat {

}
*/

export enum UserStatus {
    Active, Limited, Banned
}
