package com.math.solver.mathsolverapp.utils.preferences

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.billingDataPurchasedKey
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.billingRequireKey
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.firstTimeAskingPermission
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.isAcceptPermissionOverlayKey
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.isShowFirstScreenKey
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.is_service_enable_key
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.is_service_running_key
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.keyInAppPurchaseOrderId
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.keyPackageTokenPurchased
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_enable_clap
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_enable_sound
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_enable_whistle
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_flash_clap
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_flash_mode_clap
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_flash_mode_sound
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_flash_mode_whistle
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_flash_sound
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_flash_whistle
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_history_record
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_isRatingDoneFirstTime
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_isSecondOpenApp
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_is_push_noti_sale
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_is_rated
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_list_record_sound_recognite
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_melody_clap
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_melody_length_clap
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_melody_length_sound
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_melody_length_whistle
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_melody_sound
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_melody_volume_clap
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_melody_volume_sound
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_melody_volume_whistle
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_melody_whistle
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_package_name_purchase
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_ring_tone_alarm
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_show_show_case_in_history
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_show_show_case_in_mainactivity
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_show_show_case_in_setupactivity
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_text_recognite_vosk_sound_selected
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_time_flash_off
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_time_flash_on
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_tracking_adjust_in_app
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_vibrate_clap
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_vibrate_sound
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.key_vibrate_whistle
import com.math.solver.mathsolverapp.utils.preferences.SharePreference.selectedLanguageCodeKey


class SharedPreferenceUtils(private val sharedPreferences: SharedPreferences) {

    /* ---------- Billing ---------- */

    var isFirstTimeAskingPermissionOverlay: Boolean
        get() = sharedPreferences.getBoolean(firstTimeAskingPermission, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(firstTimeAskingPermission, value)
                apply()
            }
        }
    var isAcceptPermissionOverlay: Boolean
        get() = sharedPreferences.getBoolean(isAcceptPermissionOverlayKey, false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(isAcceptPermissionOverlayKey, value)
                apply()
            }
        }

    /* ---------- Permission ---------- */

    var isAppPurchased: Boolean
        get() = sharedPreferences.getBoolean(billingRequireKey, false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(billingRequireKey, value)
                apply()
            }
        }

    fun isPurchasedApp() : LiveData<Boolean> {
        return BooleanLiveData(sharedPreferences, billingRequireKey, false)
    }

    var isAppPurchasedPackageData: String
        get() = sharedPreferences.getString(billingDataPurchasedKey, "") ?: ""
        set(value) {
            sharedPreferences.edit().apply {
                putString(billingDataPurchasedKey, value)
                apply()
            }
        }

    var packageTokenPurchased: String
        get() = sharedPreferences.getString(keyPackageTokenPurchased, "") ?: ""
        set(value) {
            sharedPreferences.edit().apply {
                putString(keyPackageTokenPurchased, value)
                apply()
            }
        }

    var isPackageNamePurchase: String
        get() = sharedPreferences.getString(key_package_name_purchase, "") ?: ""
        set(value) {
            sharedPreferences.edit().apply {
                putString(key_package_name_purchase, value)
                apply()
            }
        }

    var orderIdInAppPurchase: String
        get() = sharedPreferences.getString(keyInAppPurchaseOrderId, "") ?: ""
        set(value) {
            sharedPreferences.edit().apply {
                putString(keyInAppPurchaseOrderId, value)
                apply()
            }
        }


    var isTrackingAdjustInApp: Boolean
        get() = sharedPreferences.getBoolean(key_tracking_adjust_in_app, false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_tracking_adjust_in_app, value)
                apply()
            }
        }


    var isSecondOpenApp: Boolean
        get() = sharedPreferences.getBoolean(key_isSecondOpenApp, false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_isSecondOpenApp, value)
                apply()
            }
        }



    /* ---------- UI ---------- */

    var is_service_enable: Boolean
        get() = sharedPreferences.getBoolean(is_service_enable_key, false) ?: false
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(is_service_enable_key, value)
                apply()
            }
        }

    fun isServiceEnable() : LiveData<Boolean> {
        return BooleanLiveData(sharedPreferences, is_service_enable_key, false)
    }


    var is_service_running: Boolean
        get() = sharedPreferences.getBoolean(is_service_running_key, false) ?: false
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(is_service_running_key, value)
                apply()
            }
        }
    fun isServiceRunning() : LiveData<Boolean> {
        return BooleanLiveData(sharedPreferences, is_service_running_key, false)
    }



    var showFirstScreen: Boolean
        get() = sharedPreferences.getBoolean(isShowFirstScreenKey, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(isShowFirstScreenKey, value)
                apply()
            }
        }

    var selectedLanguageCode: String
        get() = sharedPreferences.getString(selectedLanguageCodeKey, "en") ?: "en"
        set(value) {
            sharedPreferences.edit().apply {
                putString(selectedLanguageCodeKey, value)
                apply()
            }
        }


    var listHistoryRecordString: String
        get() = sharedPreferences.getString(key_history_record, "") ?: ""
        set(value) {
            sharedPreferences.edit().apply {
                putString(key_history_record, value)
                apply()
            }
        }

    var flash_whistle: Boolean
        get() = sharedPreferences.getBoolean(key_flash_whistle, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_flash_whistle, value)
                apply()
            }
        }

    var flash_clap: Boolean
        get() = sharedPreferences.getBoolean(key_flash_clap, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_flash_clap, value)
                apply()
            }
        }

    var flash_sound: Boolean
        get() = sharedPreferences.getBoolean(key_flash_sound, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_flash_sound, value)
                apply()
            }
        }

    var vibrate_whistle: Boolean
        get() = sharedPreferences.getBoolean(key_vibrate_whistle, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_vibrate_whistle, value)
                apply()
            }
        }

    var vibrate_clap: Boolean
        get() = sharedPreferences.getBoolean(key_vibrate_clap, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_vibrate_clap, value)
                apply()
            }
        }

    var vibrate_sound: Boolean
        get() = sharedPreferences.getBoolean(key_vibrate_sound, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_vibrate_sound, value)
                apply()
            }
        }

    var melody_whistle: Boolean
        get() = sharedPreferences.getBoolean(key_melody_whistle, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_melody_whistle, value)
                apply()
            }
        }

    var melody_clap: Boolean
        get() = sharedPreferences.getBoolean(key_melody_clap, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_melody_clap, value)
                apply()
            }
        }

    var melody_sound: Boolean
        get() = sharedPreferences.getBoolean(key_melody_sound, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_melody_sound, value)
                apply()
            }
        }

    var melody_length_clap: Int
        get() = sharedPreferences.getInt(key_melody_length_clap, 500)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(key_melody_length_clap, value)
                apply()
            }
        }

    var melody_length_sound: Int
        get() = sharedPreferences.getInt(key_melody_length_sound, 500)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(key_melody_length_sound, value)
                apply()
            }
        }

    var melody_length_whistle: Int
        get() = sharedPreferences.getInt(key_melody_length_whistle, 500)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(key_melody_length_whistle, value)
                apply()
            }
        }

    var melody_volume_whistle: Int
        get() = sharedPreferences.getInt(key_melody_volume_whistle, 50)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(key_melody_volume_whistle, value)
                apply()
            }
        }

    var melody_volume_sound: Int
        get() = sharedPreferences.getInt(key_melody_volume_sound, 50)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(key_melody_volume_sound, value)
                apply()
            }
        }

    var melody_volume_clap: Int
        get() = sharedPreferences.getInt(key_melody_volume_clap, 50)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(key_melody_volume_clap, value)
                apply()
            }
        }

    var enable_clap: Boolean
        get() = sharedPreferences.getBoolean(key_enable_clap, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_enable_clap, value)
                apply()
            }
        }

    var enable_whistle: Boolean
        get() = sharedPreferences.getBoolean(key_enable_whistle, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_enable_whistle, value)
                apply()
            }
        }

    var enable_sound: Boolean
        get() = sharedPreferences.getBoolean(key_enable_sound, false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_enable_sound, value)
                apply()
            }
        }


    var ring_tone_alarm: Int
        get() = sharedPreferences.getInt(key_ring_tone_alarm, 0)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(key_ring_tone_alarm, value)
                apply()
            }
        }

    var flash_mode_clap: Int
        get() = sharedPreferences.getInt(key_flash_mode_clap, 0)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(key_flash_mode_clap, value)
                apply()
            }
        }


    var flash_mode_whistle: Int
        get() = sharedPreferences.getInt(key_flash_mode_whistle, 0)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(key_flash_mode_whistle, value)
                apply()
            }
        }


    var flash_mode_sound: Int
        get() = sharedPreferences.getInt(key_flash_mode_sound, 0)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(key_flash_mode_sound, value)
                apply()
            }
        }


    var text_recognite_vosk_sound_selected: String
        get() = sharedPreferences.getString(key_text_recognite_vosk_sound_selected, "") ?: ""
        set(value) {
            sharedPreferences.edit().apply {
                putString(key_text_recognite_vosk_sound_selected, value)
                apply()
            }
        }


    var list_record_sound_recognite: String
        get() = sharedPreferences.getString(key_list_record_sound_recognite, "") ?: ""
        set(value) {
            sharedPreferences.edit().apply {
                putString(key_list_record_sound_recognite, value)
                apply()
            }
        }


    var show_show_case_in_setupactivity: Boolean
        get() = sharedPreferences.getBoolean(key_show_show_case_in_setupactivity, false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_show_show_case_in_setupactivity, value)
                apply()
            }
        }


    var show_show_case_in_mainactivity: Boolean
        get() = sharedPreferences.getBoolean(key_show_show_case_in_mainactivity, true)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_show_show_case_in_mainactivity, value)
                apply()
            }
        }


    var show_show_case_in_history: Boolean
        get() = sharedPreferences.getBoolean(key_show_show_case_in_history, false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_show_show_case_in_history, value)
                apply()
            }
        }


    var isRatingDoneFirstTime: Boolean
        get() = sharedPreferences.getBoolean(key_isRatingDoneFirstTime, false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_isRatingDoneFirstTime, value)
                apply()
            }
        }

    var is_rated: Boolean
        get() = sharedPreferences.getBoolean(key_is_rated, false) ?: false
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_is_rated, value)
                apply()
            }
        }

    //tracking
    var is_tracking_first_open_slash: Boolean
        get() = sharedPreferences.getBoolean("is_tracking_first_open_slash", false) ?: false
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean("is_tracking_first_open_slash", value)
                apply()
            }
        }
    var is_tracking_first_open_language: Boolean
        get() = sharedPreferences.getBoolean("is_tracking_first_open_language", false) ?: false
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean("is_tracking_first_open_language", value)
                apply()
            }
        }
    var is_tracking_first_open_use_phone: Boolean
        get() = sharedPreferences.getBoolean("is_tracking_first_open_use_phone", false) ?: false
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean("is_tracking_first_open_use_phone", value)
                apply()
            }
        }
    var is_tracking_first_open_onboarding: Boolean
        get() = sharedPreferences.getBoolean("is_tracking_first_open_onboarding", false) ?: false
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean("is_tracking_first_open_onboarding", value)
                apply()
            }
        }
    var is_tracking_first_open_permission: Boolean
        get() = sharedPreferences.getBoolean("is_tracking_first_open_permission", false) ?: false
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean("is_tracking_first_open_permission", value)
                apply()
            }
        }
    var is_tracking_first_open_main: Boolean
        get() = sharedPreferences.getBoolean("is_tracking_first_open_main", false) ?: false
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean("is_tracking_first_open_main", value)
                apply()
            }
        }

    var is_push_noti_sale: Boolean
        get() = sharedPreferences.getBoolean(key_is_push_noti_sale, false) ?: false
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(key_is_push_noti_sale, value)
                apply()
            }
        }



    var time_flash_on: Float
        get() = sharedPreferences.getFloat(key_time_flash_on, 1F)
        set(value) {
            sharedPreferences.edit().apply {
                putFloat(key_time_flash_on, value)
                apply()
            }
        }

    fun timeFlashOnLiveData() : LiveData<Float> {
        return FloatLiveData(sharedPreferences, key_time_flash_on, 1F)
    }

    var time_flash_off: Float
        get() = sharedPreferences.getFloat(key_time_flash_off, 1F)
        set(value) {
            sharedPreferences.edit().apply {
                putFloat(key_time_flash_off, value)
                apply()
            }
        }

    fun timeFlashOffLiveData() : LiveData<Float> {
        return FloatLiveData(sharedPreferences, key_time_flash_off, 1F)
    }

}