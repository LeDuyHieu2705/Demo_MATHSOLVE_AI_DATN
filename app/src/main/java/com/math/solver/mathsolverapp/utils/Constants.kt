package com.math.solver.mathsolverapp.utils


class Constants {

    class Intent {
        companion object {
            const val PRANK = "PRANK"
            const val PRANK_NAME = "PRANK_NAME"
            const val PRANK_IMAGE = "PRANK_IMAGE"
            const val PRANK_SOUND = "PRANK_SOUND"
            const val DATA_MODEL = "DATA_MODEL"
            const val FAVOURITE_MODE = "FAVOURITE_MODE"
            const val FAVOURITE_MODE_LIGHT_SABER = "FAVOURITE_MODE_LIGHT_SABER"
            const val FAVOURITE_MODE_GUN = "FAVOURITE_MODE_GUN"
            const val VOICE_RECORD = "VOICE_RECORD"
            const val RECORD_TO_CHANGE_VOICE = "RECORD_TO_CHANGE_VOICE"
            const val CHANGE_VOICE_WITH_FILE = "CHANGE_VOICE_WITH_FILE"

            const val DATA_VOICE_SAVE = "DATA_VOICE_SAVE"
            internal var KEY_POSITION_EFFECT = "key_position_effect"
            internal var KEY_FILENAME_EFFECT = "key_filename_effect"
            internal var KEY_SIZE_VOICE = "key_size_effect"


        }
    }


    companion object {
        const val VOICE_RECORD = "VOICE_RECORD"


        @JvmField
        var lastShowAdFull = 0L

        @JvmField
        var CURRENT_LANGUAGE = ""

        @JvmField
        var ACCESS_TOKEN_GSM = ""

        const val SERVICE_ACTION_START = "action_start"

        internal var KEY_PATH_VOICE = "key_path_voice"
        internal var KEY_SCREEN_INTO_VOICE_EFFECTS = "key_screen_into_voice_effect"

        //setting app
        const val APP_SETTINGS = "CSCMOBI_APP_FLASH_SETTINGS"
        const val SETTINGS_IS_PRO = "SETTINGS_IS_PRO"
        const val SETTINGS_IS_FIRST_OPEN_APP = "SETTINGS_IS_FIRST_OPEN_APP"
        const val LANGUAGE_APP = "LANGUAGE_APP"
        const val CURRENT_SUB = "CURRENT_SUB"
        const val SETTINGS_START_TIME_OPEN_APP = "SETTINGS_START_TIME_OPEN_APP"
        const val SETTINGS_SHOW_AD_FULL = "SETTINGS_SHOW_AD_FULL"
        const val SETTINGS_SHOW_AD_REWARD = "SETTINGS_SHOW_AD_REWARD"
        const val SETTINGS_RETENTION_1_D = "SETTINGS_RETENTION_1_D"
        const val SETTINGS_RETENTION_3_D = "SETTINGS_RETENTION_3_D"
        const val SETTINGS_RETENTION_7_D = "SETTINGS_RETENTION_7_D"
        const val SETTINGS_RETENTION_14_D = "SETTINGS_RETENTION_14_D"
        const val SETTINGS_RETENTION_20_D = "SETTINGS_RETENTION_20_D"
        const val SETTINGS_RETENTION_30_D = "SETTINGS_RETENTION_30_D"

        const val GO_TO_MENU = "GO_TO_MENU"

        //general configs
        const val KEY_APP_CONFIGS = "android_app_configs"

        //for Ads
        const val KEY_PASS_REVIEW_APP = "android_pass_review_app"

        //open app
        const val KEY_AD_OPEN_APP = "android_ad_open_app"
        const val KEY_AD_INTER_OPEN_APP = "android_ad_inter_open_app"

        //banner
        const val KEY_AD_BANNER_SPLASH = "android_ad_banner_splash"
        const val KEY_INTERVAL_AD_COLLAPSE = "android_interval_ad_collapse"
        const val KEY_AD_BANNER_MAIN = "android_ad_banner_main"
        const val KEY_AD_BANNER_SETTING = "android_ad_banner_setting"
        const val KEY_AD_BANNER_FLASH_TYPE = "android_ad_banner_flash_type"
        const val KEY_AD_BANNER_ADVANCED = "android_ad_banner_advanced"

        //native
        const val KEY_AD_NATIVE_LANGUAGE = "android_ad_native_language"
        const val KEY_AD_NATIVE_ONBOARD = "android_ad_native_onboard"
        const val KEY_AD_NATIVE_SETTING = "android_ad_native_setting"
        const val KEY_AD_NATIVE_LAMP = "android_ad_native_lamp"
        const val KEY_AD_NATIVE_FLASH_TYPE = "android_ad_native_flash_type"
        const val KEY_AD_NATIVE_NOTI = "android_ad_native_noti"
        const val KEY_AD_NATIVE_ADVANCED = "android_ad_native_advanced"
        const val KEY_AD_NATIVE_APP_SELECT = "android_ad_native_app_select"

        //inter
        const val KEY_INTERVAL_AD_FULL = "android_interval_ad_full"
        const val KEY_AD_INTER_ONBOARD_GO_MAIN = "android_ad_inter_onboard_go_main"
        const val KEY_AD_INTER_MAIN_LAMP = "android_ad_inter_main_lamp"
        const val KEY_AD_INTER_MAIN_NOTI = "android_ad_inter_main_noti"
        const val KEY_AD_INTER_SETTING_BACK = "android_ad_inter_setting_back"
        const val KEY_AD_INTER_SETTING_GO_LANGUAGE = "android_ad_inter_setting_go_language"
        const val KEY_AD_INTER_LAMP_TYPE_FLASH = "android_ad_inter_lamp_type_flash"
        const val KEY_AD_INTER_FLASH_TYPE_BACK = "android_ad_inter_flash_type_back"
        const val KEY_AD_INTER_FLASH_TYPE_ITEM_CLICK = "android_ad_inter_flash_type_item_click"
        const val KEY_AD_INTER_APP_SELECT_BACK = "android_ad_inter_app_select_back"
        const val KEY_AD_INTER_APP_SELECT_DONE = "android_ad_inter_app_select_done"
        const val KEY_AD_INTER_NOTI_APPLICATION = "android_ad_inter_noti_application"

        //reward
        const val KEY_AD_REWARD_NOTI_ADVANCED_SETTING = "android_ad_reward_noti_advanced_setting"

        //for subs
        var KEY_SUBS_MONTHLY = "com.csc.base.monthly"

        //for event log
        const val EVENT_IN_APP_PURCHASE_CUSTOM = "android_in_app_purchase_custom"
        const val EVENT_WATCH_5_ADS_FULL = "android_show_ad_full_5"
        const val EVENT_WATCH_10_ADS_FULL = "android_show_ad_full_10"
        const val EVENT_WATCH_5_ADS_REWARD = "android_show_ad_reward_5"
        const val EVENT_WATCH_10_ADS_REWARD = "android_show_ad_reward_10"
        const val EVENT_RETENTION_DAY_1 = "android_retention_day_1"
        const val EVENT_RETENTION_DAY_3 = "android_retention_day_3"
        const val EVENT_RETENTION_DAY_7 = "android_retention_day_7"
        const val EVENT_RETENTION_DAY_14 = "android_retention_day_14"
        const val EVENT_RETENTION_DAY_20 = "android_retention_day_20"
        const val EVENT_RETENTION_DAY_30 = "android_retention_day_30"

        //Tracking First Event
        const val FIRST_OPEN_SPLASH = "first_open_splash"
        const val FIRST_OPEN_LANGUAGE = "first_open_language"
        const val FIRST_OPEN_INTRO = "first_open_intro"
        const val FIRST_OPEN_HOME = "first_open_home"
        const val FIRST_OPEN_SUBSCRIPTION = "first_open_subscription"

        //Setting Func

        const val key_time_on = "key_time_on"
        const val key_time_off = "key_time_off"


        const val FROM_MENU = "FROM_MENU"
        const val TYPE_SUBJECT_SELECTED = "TYPE_SUBJECT_SELECTED"

    }

    class TypeData {
        companion object {
            const val FREE = "FREE"
            const val ADS = "ADS"
        }
    }

    class Language {
        companion object {
            const val ENGLISH = "EN"
            const val INDIA = "HI"
            const val FRANCE = "FR"
            const val GERMANY = "DE"
            const val INDONESIA = "IN"
            const val PORTUGAL = "PT"
            const val SPAIN = "ES"
            const val VIETNAM = "VI"
        }
    }

    object NavigationMain {
        const val LAMP = "LAMP"
        const val NOTI = "NOTI"
    }

    object TypeFlash {
        const val CUSTOM = "TYPE_CUSTOM"
        const val TYPE_FLASH = "TYPE_FLASH"
        const val HELP = "HELP"
        const val DISCO = "DISCO"
        const val MORE = "MORE"
    }

    class Ads {
        companion object {
            // id ad release
            const val NATIVE_AD_RELEASE = "ca-app-pub-8096866360181326/9930191878"
            const val NATIVE_AD_VIDEO_RELEASE = "ca-app-pub-8096866360181326/8617110207"
            const val INTER_AD_RELEASE = "ca-app-pub-8096866360181326/5990946867"
            const val REWARD_AD_RELEASE = "ca-app-pub-8096866360181326/4012754434"
            const val BANNER_AD_RELEASE = "ca-app-pub-8096866360181326/9484572961"
            const val BANNER_COLLAPSE_AD_RELEASE = "ca-app-pub-8096866360181326/4312137330"
            const val APP_OPEN_AD_RELEASE = "ca-app-pub-8096866360181326/5325836106"
            const val INTER_OPEN_AD_RELEASE = "ca-app-pub-8096866360181326/9303505916"

            // id ad test
            const val NATIVE_AD_TEST = "ca-app-pub-3940256099942544/2247696110"
            const val NATIVE_AD_VIDEO_TEST = "ca-app-pub-3940256099942544/2521693316"
            const val INTER_AD_TEST = "ca-app-pub-3940256099942544/1033173712"
            const val REWARD_AD_TEST = "ca-app-pub-3940256099942544/5224354917"
            const val BANNER_AD_TEST = "ca-app-pub-3940256099942544/6300978111"
            const val BANNER_COLLAPSE_AD_TEST = "ca-app-pub-3940256099942544/2014213617"
            const val APP_OPEN_AD_TEST = "ca-app-pub-3940256099942544/9257395921"
            const val INTER_OPEN_AD_TEST = "ca-app-pub-3940256099942544/1033173712"

            // native inline
            const val VIEW_TYPE_AD = 1
            const val VIEW_TYPE_CONTENT = 2
            const val AD_TYPE = "ADS_INLINE"
        }
    }


    class Model {
        companion object {
            const val FREE = "FREE"
            const val PRO = "PRO"
            const val ADS = "ADS"
        }
    }
}