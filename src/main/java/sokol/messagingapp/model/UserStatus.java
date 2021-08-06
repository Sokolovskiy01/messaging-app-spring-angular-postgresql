package sokol.messagingapp.model;

public enum UserStatus {
    Active, // normal user which can chat with everyone
    Limited, // user that got many reports and not allowed to chat with people he's not friends with
    Banned // user that has no longer access to app
}
