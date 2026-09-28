# Screen tracking

30 event tùy chỉnh gửi qua Firebase Analytics. Tham số `screen_name` là tên event bỏ tiền tố `screen_`.

## Quy tắc đếm

- Activity: gửi một lần khi tạo mới; không gửi lại khi khôi phục `savedInstanceState`, resume từ quảng cáo, trở lại từ Activity khác hoặc foreground.
- Tab chính: gửi khi vào Main và khi route thay đổi, bao gồm quay lại tab trước. Chọn lại cùng route không tạo thêm event.
- Onboarding: gửi khi trang dừng cuộn; quảng cáo có event riêng, không làm lệch số trang nội dung.
- Tab thêm playlist/favorite: gửi khi composable vào giao diện và khi đổi tab. Recomposition thông thường không gửi lại. Khi composable bị tạo lại (quay lại tab hoặc Activity được tạo lại), có thể có lượt mới.
- Preview không gửi event. Không gửi URL, tên playlist, tên kênh hoặc đường dẫn file.
- Event màn cha và tab/trang con là số liệu riêng; không cộng tất cả thành tổng lượt mở màn.
- Các event đo lượt truy cập màn hình, chưa đo số thao tác thành công hay thời lượng sử dụng. Dialog, bộ lọc và trình chọn file hệ thống không tính là màn riêng.

## Danh sách event

| Event | Màn hình / tab |
| --- | --- |
| `screen_splash` | Splash |
| `screen_language_1` | Ngôn ngữ bước 1 |
| `screen_language_2` | Ngôn ngữ bước 2 |
| `screen_onboarding` | Onboarding (màn cha) |
| `screen_personalization` | Cá nhân hóa |
| `screen_main` | Main (màn cha) |
| `screen_add_channel` | Thêm playlist (màn cha) |
| `screen_playlist` | Chi tiết playlist |
| `screen_play_channel` | Player dọc |
| `screen_play_channel_landscape` | Player ngang |
| `screen_watch_now` | Watch now |
| `screen_language_setting` | Cài đặt ngôn ngữ |
| `screen_about_us` | About us |
| `screen_how_to_add_channel` | Hướng dẫn thêm kênh |
| `screen_iap` | Premium |
| `screen_home` | Home |
| `screen_channel` | Channel |
| `screen_favorite` | Favorite (màn cha) |
| `screen_setting` | Setting |
| `screen_onboarding_1` | Onboarding 1 |
| `screen_onboarding_2` | Onboarding 2 |
| `screen_onboarding_3` | Onboarding 3 |
| `screen_onboarding_4` | Onboarding 4 |
| `screen_onboarding_ad` | Quảng cáo onboarding |
| `screen_add_channel_url` | Thêm qua URL |
| `screen_add_channel_file` | Thêm qua file |
| `screen_add_channel_gallery` | Thêm từ gallery |
| `screen_favorite_saved` | Kênh yêu thích |
| `screen_favorite_history` | Lịch sử xem |
| `screen_favorite_all` | Tất cả kênh |

## Kiểm tra trên thiết bị

Bật Firebase DebugView cho bản dev:

```sh
adb shell setprop debug.firebase.analytics.app com.dongbv.alf_03_iptv
```

Mở app, đi qua các màn/tab và đối chiếu event, `screen_name` trong DebugView. Kiểm tra quay lại tab, onboarding qua trang quảng cáo và recomposition khi nhập liệu.

Tắt sau khi kiểm tra:

```sh
adb shell setprop debug.firebase.analytics.app .none.
```

Tên event quản lý tại `app/src/main/java/com/dongbv/alf_03_iptv/analytics/AppScreen.kt`. Activity mới phải khai báo `analyticsScreen` theo hợp đồng của `BaseActivity`.
