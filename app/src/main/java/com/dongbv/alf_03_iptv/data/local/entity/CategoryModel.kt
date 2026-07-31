package com.dongbv.alf_03_iptv.data.local.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.dongbv.alf_03_iptv.R
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Stable
@Parcelize
data class CategoryModel(
    val index: Int,
    @param:StringRes val title: Int,
    val count: Int,
    val assetPath: String,
    var select: Boolean = false
) : Parcelable {
}

@Stable
@Parcelize
data class LanguageModel(
    val index: Int,
    val code: String,  
    val name: String,
    val count: Int,
    val url: String,   
    var select: Boolean = false
) : Parcelable {
}

// Returns the LanguageModel whose code matches the device locale, or null if not in the list.
fun detectDeviceLanguage(): LanguageModel? {
    val lang = Locale.getDefault().language  
    return listLanguageModel.find { it.code == lang }
}

val listLanguageModel: List<LanguageModel>
    get() = listOf(
    LanguageModel(1,  "ak", "Akan",              2,    "https://iptv-org.github.io/iptv/languages/aka.m3u"),
    LanguageModel(2,  "sq", "Albanian",          22,   "https://iptv-org.github.io/iptv/languages/sqi.m3u"),
    LanguageModel(3,  "am", "Amharic",           1,    "https://iptv-org.github.io/iptv/languages/amh.m3u"),
    LanguageModel(4,  "ar", "Arabic",            271,  "https://iptv-org.github.io/iptv/languages/ara.m3u"),
    LanguageModel(5,  "hy", "Armenian",          8,    "https://iptv-org.github.io/iptv/languages/hye.m3u"),
    LanguageModel(6,  "az", "Azerbaijani",       12,   "https://iptv-org.github.io/iptv/languages/aze.m3u"),
    LanguageModel(7,  "bn", "Bengali",           5,    "https://iptv-org.github.io/iptv/languages/ben.m3u"),
    LanguageModel(8,  "bs", "Bosnian",           3,    "https://iptv-org.github.io/iptv/languages/bos.m3u"),
    LanguageModel(9,  "bg", "Bulgarian",         16,   "https://iptv-org.github.io/iptv/languages/bul.m3u"),
    LanguageModel(10, "my", "Burmese",           16,   "https://iptv-org.github.io/iptv/languages/mya.m3u"),
    LanguageModel(11, "ca", "Catalan",           2,    "https://iptv-org.github.io/iptv/languages/cat.m3u"),
    LanguageModel(12, "zh", "Chinese",           1102, "https://iptv-org.github.io/iptv/languages/zho.m3u"),
    LanguageModel(13, "hr", "Croatian",          14,   "https://iptv-org.github.io/iptv/languages/hrv.m3u"),
    LanguageModel(14, "cs", "Czech",             18,   "https://iptv-org.github.io/iptv/languages/ces.m3u"),
    LanguageModel(15, "da", "Danish",            14,   "https://iptv-org.github.io/iptv/languages/dan.m3u"),
    LanguageModel(16, "nl", "Dutch",             66,   "https://iptv-org.github.io/iptv/languages/nld.m3u"),
    LanguageModel(17, "en", "English",           563,  "https://iptv-org.github.io/iptv/languages/eng.m3u"),
    LanguageModel(18, "et", "Estonian",          3,    "https://iptv-org.github.io/iptv/languages/est.m3u"),
    LanguageModel(19, "fo", "Faroese",           1,    "https://iptv-org.github.io/iptv/languages/fao.m3u"),
    LanguageModel(20, "fi", "Finnish",           3,    "https://iptv-org.github.io/iptv/languages/fin.m3u"),
    LanguageModel(21, "fr", "French",            84,   "https://iptv-org.github.io/iptv/languages/fra.m3u"),
    LanguageModel(22, "gl", "Galician",          2,    "https://iptv-org.github.io/iptv/languages/glg.m3u"),
    LanguageModel(23, "ka", "Georgian",          41,   "https://iptv-org.github.io/iptv/languages/kat.m3u"),
    LanguageModel(24, "de", "German",            192,  "https://iptv-org.github.io/iptv/languages/deu.m3u"),
    LanguageModel(25, "he", "Hebrew",            15,   "https://iptv-org.github.io/iptv/languages/heb.m3u"),
    LanguageModel(26, "hi", "Hindi",             131,  "https://iptv-org.github.io/iptv/languages/hin.m3u"),
    LanguageModel(27, "hu", "Hungarian",         18,   "https://iptv-org.github.io/iptv/languages/hun.m3u"),
    LanguageModel(28, "is", "Icelandic",         3,    "https://iptv-org.github.io/iptv/languages/isl.m3u"),
    LanguageModel(29, "id", "Indonesian",        47,   "https://iptv-org.github.io/iptv/languages/ind.m3u"),
    LanguageModel(30, "it", "Italian",           142,  "https://iptv-org.github.io/iptv/languages/ita.m3u"),
    LanguageModel(31, "ja", "Japanese",          15,   "https://iptv-org.github.io/iptv/languages/jpn.m3u"),
    LanguageModel(32, "jv", "Javanese",          3,    "https://iptv-org.github.io/iptv/languages/jav.m3u"),
    LanguageModel(33, "kk", "Kazakh",            21,   "https://iptv-org.github.io/iptv/languages/kaz.m3u"),
    LanguageModel(34, "km", "Khmer",             6,    "https://iptv-org.github.io/iptv/languages/khm.m3u"),
    LanguageModel(35, "rw", "Kinyarwanda",       5,    "https://iptv-org.github.io/iptv/languages/kin.m3u"),
    LanguageModel(36, "ko", "Korean",            31,   "https://iptv-org.github.io/iptv/languages/kor.m3u"),
    LanguageModel(37, "ku", "Kurdish",           6,    "https://iptv-org.github.io/iptv/languages/kur.m3u"),
    LanguageModel(38, "lo", "Lao",               4,    "https://iptv-org.github.io/iptv/languages/lao.m3u"),
    LanguageModel(39, "lv", "Latvian",           4,    "https://iptv-org.github.io/iptv/languages/lav.m3u"),
    LanguageModel(40, "lb", "Luxembourgish",     2,    "https://iptv-org.github.io/iptv/languages/ltz.m3u"),
    LanguageModel(41, "mk", "Macedonian",        5,    "https://iptv-org.github.io/iptv/languages/mkd.m3u"),
    LanguageModel(42, "zh", "Mandarin Chinese",  176,  "https://iptv-org.github.io/iptv/languages/cmn.m3u"),
    LanguageModel(43, "zh", "Min Nan Chinese",   2,    "https://iptv-org.github.io/iptv/languages/nan.m3u"),
    LanguageModel(44, "mn", "Mongolian",         6,    "https://iptv-org.github.io/iptv/languages/mon.m3u"),
    LanguageModel(45, "nb", "Norwegian Bokmål",  2,    "https://iptv-org.github.io/iptv/languages/nob.m3u"),
    LanguageModel(46, "fa", "Persian",           86,   "https://iptv-org.github.io/iptv/languages/fas.m3u"),
    LanguageModel(47, "pl", "Polish",            9,    "https://iptv-org.github.io/iptv/languages/pol.m3u"),
    LanguageModel(48, "pt", "Portuguese",        165,  "https://iptv-org.github.io/iptv/languages/por.m3u"),
    LanguageModel(49, "ps", "Pushto",            6,    "https://iptv-org.github.io/iptv/languages/pus.m3u"),
    LanguageModel(50, "ro", "Romanian",          89,   "https://iptv-org.github.io/iptv/languages/ron.m3u"),
    LanguageModel(51, "ru", "Russian",           251,  "https://iptv-org.github.io/iptv/languages/rus.m3u"),
    LanguageModel(52, "sr", "Serbian",           12,   "https://iptv-org.github.io/iptv/languages/srp.m3u"),
    LanguageModel(53, "si", "Sinhala",           4,    "https://iptv-org.github.io/iptv/languages/sin.m3u"),
    LanguageModel(54, "sk", "Slovak",            34,   "https://iptv-org.github.io/iptv/languages/slk.m3u"),
    LanguageModel(55, "so", "Somali",            1,    "https://iptv-org.github.io/iptv/languages/som.m3u"),
    LanguageModel(56, "es", "Spanish",           343,  "https://iptv-org.github.io/iptv/languages/spa.m3u"),
    LanguageModel(57, "sv", "Swedish",           11,   "https://iptv-org.github.io/iptv/languages/swe.m3u"),
    LanguageModel(58, "tl", "Tagalog",           9,    "https://iptv-org.github.io/iptv/languages/tgl.m3u"),
    LanguageModel(59, "ta", "Tamil",             15,   "https://iptv-org.github.io/iptv/languages/tam.m3u"),
    LanguageModel(60, "th", "Thai",              44,   "https://iptv-org.github.io/iptv/languages/tha.m3u"),
    LanguageModel(61, "tr", "Turkish",           69,   "https://iptv-org.github.io/iptv/languages/tur.m3u"),
    LanguageModel(62, "tk", "Turkmen",           11,   "https://iptv-org.github.io/iptv/languages/tuk.m3u"),
    LanguageModel(63, "uk", "Ukrainian",         159,  "https://iptv-org.github.io/iptv/languages/ukr.m3u"),
    LanguageModel(64, "ur", "Urdu",              23,   "https://iptv-org.github.io/iptv/languages/urd.m3u"),
    LanguageModel(65, "vi", "Vietnamese",        67,   "https://iptv-org.github.io/iptv/languages/vie.m3u"),
    LanguageModel(66, "zh", "Yue Chinese",       13,   "https://iptv-org.github.io/iptv/languages/yue.m3u"),
    LanguageModel(67, "",   "Undefined",         192,  "https://iptv-org.github.io/iptv/languages/undefined.m3u"),
)

val listCategoryModel: List<CategoryModel>
    get() = listOf(
    CategoryModel(1,  R.string.category_247_tv,              0,   "category_file/24_7_tv.m3u"),
    CategoryModel(2,  R.string.category_action,              0,   "category_file/actions.m3u"),
    CategoryModel(3,  R.string.category_comedy,              0,   "category_file/comedy.m3u"),
    CategoryModel(4,  R.string.category_documentary,         0,   "category_file/documentary.m3u"),
    CategoryModel(5,  R.string.category_drama,               0,   "category_file/drama.m3u"),
    CategoryModel(6,  R.string.category_education,           0,   "category_file/education.m3u"),
    CategoryModel(7,  R.string.category_fashion,             0,   "category_file/fashion.m3u"),
    CategoryModel(8,  R.string.category_general_local,       0,   "category_file/general_local.m3u"),
    CategoryModel(9,  R.string.category_kid_cartoon,         0,   "category_file/kid_cartoon.m3u"),
    CategoryModel(10, R.string.category_movies,              0,   "category_file/movies.m3u"),
    CategoryModel(11, R.string.category_music,               0,   "category_file/music.m3u"),
    CategoryModel(12, R.string.category_news,                0,   "category_file/news.m3u"),
    CategoryModel(13, R.string.category_religious_spiritual, 0,   "category_file/religious_spiritual.m3u"),
    CategoryModel(14, R.string.category_sport,               0,   "category_file/sport.m3u"),
    CategoryModel(15, R.string.category_travel_food,         0,   "category_file/travel_food.m3u"),
)
