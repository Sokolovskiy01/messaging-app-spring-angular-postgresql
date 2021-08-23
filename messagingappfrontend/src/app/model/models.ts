export interface AppUser {
    id: number | undefined,
    name: string,
    dateOfBirth: Date,
    gender: string,
    imageUrl: string,
    comment: string,
    email: string,
    password: string | null | undefined,
    userStatus: UserStatus,
    banMessage: string,
    lastLogin: Date
}

export interface Chat {
    id: number | undefined,
    user1: AppUser,
    user2: AppUser,
    user1Seen: boolean,
    user2Seen: boolean
}

export interface Message {
    id: number,
    chat: number,
    sender: number,
    messageContent: string,
    sentDate: Date
}

export enum UserStatus {
    Active, Limited, Banned
}
