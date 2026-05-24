package org.cryptoBros.presentation.Enum;

/**
 * Defines action command identifiers for UI buttons.
 */
public enum ButtonEnumeration {
    /** Navigates to the sign-up view. */
    SIGNUP,
    /** Navigates to the login view. */
    LOGIN,
    /** Confirms the registration form. */
    CONFIRM_SIGNUP,
    /** Confirms the login form. */
    CONFIRM_LOGIN,
    /** Navigates to the settings view. */
	SETTINGS,
    /** Navigates to the portfolio view. */
	PORTFOLIO,
    /** Logs out the current user. */
    LOGOUT,
    /** Navigates to the account view. */
    ACCOUNT,
    /** Deletes the current user account. */
    DELETE,
    /** Navigates to the home view. */
    HOME,
    /** Navigates to the manage crypto view. */
	MANAGE_CRYPTO,
    /** Confirms a balance deposit. */
    CONFIRM_BALANCE,
    /** Adds a new cryptocurrency. */
    ADD_CRYPTO,
    /** Navigates back to the previous view. */
	BACK,
    /** Generic confirmation action. */
    CONFIRM,
    /** Confirms a crypto purchase. */
    CONFIRM_PURCHASE,
    /** Navigates to the profile view. */
	PROFILE,
    /** Confirms a password change. */
	CHANGE_PASSWORD,
    /** Confirms a username change. */
	CHANGE_USERNAME,
    /** Confirms a profile picture change. */
	CHANGE_PROFILE_PIC,
    /** Edits a cryptocurrency name. */
	EDIT_CRYPTO
}
