package com.afrosofttech.spring_starter.util.constants;

/**
 * Defines all possible user operation types in the system
 */
public enum OperationType {
    // User account operations
    USER_CREATED("User registration"),
    USER_LOGON("User login"),
    USER_LOGOUT("User logout"),
    USER_DEACTIVATED("User deactivated"),
    USER_PASSWORD_CHANGED("Password changed"),
    USER_PROFILE_UPDATED("Profile updated"),

    // Blog post operations
    POST_CREATED("Blog post created"),
    POST_UPDATED("Blog post updated"),
    POST_DELETED("Blog post deleted"),
    POST_PUBLISHED("Blog post published"),
    POST_UNPUBLISHED("Blog post unpublished"),

    // Comment operations
    COMMENT_CREATED("Comment created"),
    COMMENT_UPDATED("Comment updated"),
    COMMENT_DELETED("Comment deleted"),
    COMMENT_REPORTED("Comment reported"),

    // Administrative operations
    POST_MODERATED("Post moderated"),
    COMMENT_MODERATED("Comment moderated"),
    USER_BANNED("User banned"),
    USER_UNBANNED("User unbanned"),

    // System operations
    EMAIL_SENT("Email notification sent"),
    PASSWORD_RESET_REQUESTED("Password reset requested"),
    DATA_EXPORT_REQUESTED("Data export requested");

    private final String description;

    OperationType(String description) {
        this.description = description;
    }

    /**
     * Returns a human-readable description of the operation
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if this operation type is related to posts
     */
    public boolean isPostOperation() {
        return name().startsWith("POST_");
    }

    /**
     * Checks if this operation type is related to comments
     */
    public boolean isCommentOperation() {
        return name().startsWith("COMMENT_");
    }

    /**
     * Checks if this operation type is related to user management
     */
    public boolean isUserOperation() {
        return name().startsWith("USER_");
    }
}
