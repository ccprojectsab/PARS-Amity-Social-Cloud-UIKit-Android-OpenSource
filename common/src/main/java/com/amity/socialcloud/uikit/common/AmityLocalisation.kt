package com.amity.socialcloud.uikit.common
/**
 * AmityLocalisation object class for centralized string management
 * Currently returns hardcoded strings - will be enhanced to support dynamic localization
 */
object AmityLocalisation {
    
    /**
     * Get string by resource ID
     * @param stringResId String resource ID
     * @return Hardcoded string value
     */
    fun getString(stringResId: Int): String {
        return "Hello World!"
    }
    
    /**
     * Get formatted string by resource ID with arguments
     * @param stringResId String resource ID
     * @param formatArgs Variable arguments for string formatting
     * @return Formatted hardcoded string value
     */
    fun getString(stringResId: Int, vararg formatArgs: Any?): String {
        return "Hello World!"
    }
} 