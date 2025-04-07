package com.fermion.android.base.view.theme

import com.fermion.android.base.constants.AppThemeType
import io.paperdb.Paper
import timber.log.Timber


object ThemePreference {

    private const val APP_THEME = "AppTheme"
    fun setTheme(theme: AppThemeType) {
        Paper.book().write(APP_THEME, theme)
    }

    fun getTheme(): AppThemeType {
        try {
            return Paper.book().read(APP_THEME, AppThemeType.Default) ?: AppThemeType.Default
        } catch (e: Exception) {
            Timber.d(e)
        }
        return AppThemeType.Default
    }

}