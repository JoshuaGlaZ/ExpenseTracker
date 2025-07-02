import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "user_session"
    private const val KEY_USER_ID = "USER_ID"
    private const val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveLoginSession(user_id: Int) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, user_id)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}