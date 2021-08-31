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

/* UI interfaces */

export interface AppUserColor {
    background: string,
    text: string
}

export let AppUserColorsArray: AppUserColor[] = [
    { background: '#d8edff', text: '#3797ec' },
    { background: '#ddf6d9', text: '#43c52d' },
    { background: '#fff0de', text: '#f69c2f' },
    { background: '#ffd9d9', text: '#f83835' }
]