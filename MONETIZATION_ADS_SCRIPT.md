# Kịch bản kiếm tiền: IAA, IAP và Subscription

## 1. Phạm vi và nguyên tắc

Ứng dụng: **StreamWave TV – IPTV Player**.

Mục tiêu:

- Người dùng miễn phí vẫn xem được nội dung và sử dụng được các chức năng cốt lõi.
- Quảng cáo xuất hiện ở các điểm ngắt tự nhiên, không chen vào lúc người dùng đang xem.
- Gói Premium bán trải nghiệm **không quảng cáo**, thao tác nhanh và ổn định hơn.
- Không cam kết cung cấp hoặc sở hữu nội dung IPTV. Người dùng tự thêm playlist hợp pháp của họ.

Nguyên tắc bắt buộc:

- Không hiển thị interstitial khi đang phát video hoặc ngay sau khi quảng cáo vừa đóng.
- Không đặt quảng cáo đè lên nút điều khiển player.
- Luôn có nút đóng rõ ràng với quảng cáo toàn màn hình.
- Nếu quảng cáo chưa tải, bỏ qua quảng cáo; không khóa luồng chính.
- Tôn trọng giới hạn tần suất, trạng thái Premium, consent và chính sách Google Play/AdMob.

---

## 2. Mô hình sản phẩm đề xuất

### 2.1. Quyền lợi Free

- Thêm playlist bằng URL, File hoặc Gallery.
- Xem kênh, tìm kiếm, phân loại, Favorites và Watch History.
- Có banner quảng cáo ở màn hình danh sách.
- Có interstitial ở một số lần chuyển đổi màn hình hoặc sau khi hoàn tất một hành động.
- Có rewarded ad để nhận một phiên xem không quảng cáo ngắn hạn.

### 2.2. Quyền lợi Premium

- Xóa toàn bộ Banner, Interstitial, App Open và Native Ads.
- Không tự động phát quảng cáo sau khi mở app.
- Có thể mở rộng giới hạn playlist/kênh nếu sản phẩm muốn áp dụng giới hạn.
- Khôi phục giao dịch trên thiết bị khác bằng cùng tài khoản Google Play.
- Không bao gồm quyền truy cập vào các kênh trả phí hay nội dung bên thứ ba.

> Khuyến nghị giai đoạn đầu: chỉ bán quyền **Remove Ads + Premium experience**, không khóa chức năng xem cơ bản. Cách này dễ giải thích và giảm phản hồi tiêu cực.

### 2.3. Giới hạn import playlist cho Free

Đề xuất áp dụng giới hạn mềm: **Free được import 3 playlist thành công** bằng URL, File hoặc Gallery. Sau lượt thứ ba, user cần Premium để tiếp tục thêm playlist không giới hạn.

Quy tắc tính lượt:

- Chỉ trừ lượt khi playlist được parse và lưu thành công.
- URL lỗi, file sai định dạng, mất mạng, user hủy hoặc import thất bại không bị trừ lượt.
- Import lại đúng playlist/URL cũ không trừ thêm lượt; nhận diện bằng URL đã chuẩn hóa hoặc file hash.
- Playlist mẫu có sẵn trong app không tính vào quota.
- Hiển thị quota dạng `2/3 lượt import đã dùng`.
- Sau lần import thành công thứ hai, hiển thị `Bạn còn 1 lượt thêm playlist miễn phí`.
- Không giới hạn việc xem playlist đã có; chỉ giới hạn việc thêm playlist mới.

Sau khi hết 3 lượt, hiển thị Paywall với hai lựa chọn:

1. `Unlock Premium` — thêm playlist không giới hạn và xóa quảng cáo.
2. `Watch an ad to add 1 more playlist` — xem rewarded ad để nhận thêm 1 lượt.

Rewarded ad bổ sung tối đa 1–2 lượt/ngày. Giai đoạn đầu nên giữ quota là 3 playlist trọn đời; chỉ đổi sang quota theo tháng nếu dữ liệu cho thấy user thường xuyên bị chặn do cập nhật playlist.

Copy Paywall đề xuất:

> Bạn đã sử dụng 3 lượt thêm playlist miễn phí. Nâng cấp Premium để thêm playlist không giới hạn và xem không quảng cáo.

---

## 3. Các gói Subscription đề xuất

Tạo các sản phẩm trong Google Play Console với ID thật sau khi chốt giá theo từng thị trường.

| Gói hiển thị | Product ID mẫu | Chu kỳ | Vai trò |
|---|---|---:|---|
| Premium Monthly | `premium_monthly` | 1 tháng | Gói dễ thử, dùng làm mốc giá |
| Premium Yearly | `premium_yearly` | 1 năm | Gói chính, hiển thị “Best value” |
| Premium Weekly | `premium_weekly` | 1 tuần | Chỉ dùng nếu cần tối ưu doanh thu thử nghiệm |

### Giá tham khảo để A/B test

Không hard-code giá trong app; lấy giá từ Google Play Billing. Có thể bắt đầu bằng:

- Monthly: khoảng **US$2.99–4.99/tháng**.
- Yearly: khoảng **US$19.99–29.99/năm**, tương đương giảm 35–55% so với trả theo tháng.
- Weekly: chỉ nên thử ở thị trường có hành vi mua ngắn hạn; không đặt làm lựa chọn mặc định.

Hiển thị giá và điều khoản phải lấy từ Store, bao gồm thuế, chu kỳ gia hạn và cách hủy. Không dùng câu “mua một lần” nếu đây là subscription tự gia hạn.

### Thứ tự hiển thị trên Paywall

1. **Yearly – Best value**: chọn sẵn, làm nổi bật số tiền tiết kiệm.
2. Monthly: lựa chọn linh hoạt.
3. Weekly: đặt cuối hoặc tắt trong giai đoạn đầu.

CTA:

- `Start Premium`
- `Continue with Yearly`
- Bản tiếng Việt: `Bắt đầu Premium`, `Tiếp tục với gói năm`

Dòng bắt buộc dưới CTA:

> Subscription tự động gia hạn cho đến khi bạn hủy. Bạn có thể quản lý hoặc hủy trong phần Subscriptions của Google Play. Việc gỡ ứng dụng không tự hủy subscription.

---

## 4. Vị trí IAA chi tiết

### 4.1. Banner Ads

**Vị trí:** cuối màn hình Home, Channel, Favorites và Settings nếu còn đủ khoảng trống; không đặt trong Play Screen.

**Kịch bản:**

1. Mở màn hình danh sách.
2. Chờ nội dung chính hiển thị xong.
3. Load banner ở vùng cuối màn hình.
4. Nếu load thất bại, giữ nguyên layout, không hiện vùng trống lớn.
5. Khi Premium active, không khởi tạo hoặc render banner.

**Ad unit ID mẫu:** `banner_home`, `banner_channel`, `banner_favorite`.

### 4.2. Interstitial Ads

Chỉ hiển thị sau khi người dùng hoàn tất một hành động, không hiển thị ngay khi vừa mở app.

Các trigger đề xuất:

- Sau khi người dùng đóng Player và quay lại danh sách, mỗi **3 lần xem**.
- Sau khi thêm playlist thành công, tối đa mỗi **2 lần thêm playlist**.
- Khi chuyển từ Home sang Channel/Favorites, tối đa mỗi **4 phút**.
- Sau khi hoàn thành onboarding lần đầu: **không hiển thị**.

Điều kiện chung:

- Không hiển thị nếu Premium.
- Không hiển thị nếu lần hiển thị trước cách đây dưới 120 giây.
- Không hiển thị nếu app đang ở background, đang loading hoặc player đang phát.
- Chỉ show khi ad đã preload và lifecycle đang RESUMED.

**Mẫu flow sau khi đóng player:**

```text
User bấm Back
  -> Đóng player / lưu history
  -> Nếu đủ frequency cap và interstitial đã sẵn sàng: show ad
  -> Ad đóng hoặc không có ad
  -> Quay về Home/Channel bình thường

#### Danh sách trigger interstitial mở rộng

| Mã trigger | Sau hành động | Tần suất đề xuất | Không được show khi |
|---|---|---:|---|
| `after_player_exit` | Đóng Player | Sau 3 phiên xem thành công | Stream lỗi, buffering hoặc user xem dưới 10 giây |
| `after_playlist_add` | Thêm playlist thành công | Mỗi 2 lần import | Đang parse/lưu hoặc import lỗi |
| `after_playlist_delete` | Xóa playlist thành công | Mỗi 3 lần xóa | User đang ở empty state |
| `after_channel_switch` | Chuyển sang nhóm/khu vực khác | Mỗi 5 lần | Đang search/filter hoặc đang mở Player |
| `after_favorite_batch` | Sau 5 lần thêm/xóa Favorite | Tối đa 1 lần | Ngay sau thao tác heart |
| `after_guide_complete` | Hoàn thành Guide | Tối đa 1 lần/ngày | Lần đầu user mở Guide |
| `after_settings_action` | Hoàn thành thao tác Settings | Tối đa 1 lần/4 giờ | Language, Privacy, Terms, Rate, Share |
| `after_session_milestone` | Đạt mốc 10/20 lượt mở nội dung | Tối đa 1 lần/mốc | Trong 120 giây từ ad gần nhất |

Quy tắc chọn trigger:

1. Chỉ chạy **một interstitial** cho một chuỗi hành động; nếu nhiều trigger cùng đúng điều kiện, ưu tiên `after_player_exit`, sau đó `after_playlist_add`, rồi các trigger còn lại.
2. Dùng frequency cap toàn app: tối thiểu 120 giây giữa hai interstitial, tối đa 4 lần/ngày và tối đa 2 lần trong một session 30 phút.
3. Sau khi user đóng interstitial, không mở Paywall ngay lập tức. Chỉ hiển thị card Premium ở màn hình kế tiếp hoặc khi user chủ động bấm.
4. Nếu ad chưa preload, bỏ qua trigger và tiếp tục flow; không đợi preload đồng bộ.
5. Không đếm impression nếu ad chưa thực sự được hiển thị.

#### Flow interstitial chuẩn

```text
Action hoàn tất
  -> Ghi nhận trigger pending
  -> Kiểm tra Premium + consent + app RESUMED + không có dialog/player
  -> Kiểm tra cooldown, daily cap và session cap
  -> Nếu ad ready: show đúng 1 lần
  -> onDismissed/onFailed: clear trigger pending
  -> Tiếp tục destination ban đầu
```

Các placement interstitial nên tách riêng trong analytics và Remote Config: `inter_after_player`, `inter_after_import`, `inter_after_action`, `inter_milestone`. Có thể dùng nhiều ad unit trong `list_ads` để fallback/rotation, nhưng không được gọi nhiều interstitial nối tiếp cho cùng một action.
+

## 9. Checklist trước khi gắn vào app

- [ ] Tạo AdMob app và ad unit ID cho từng placement.
- [ ] Khai báo App ID, consent flow và privacy options.
- [ ] Tạo subscription base plans/offers trong Google Play Console.
- [ ] Thay product ID mẫu bằng ID production.
- [ ] Lấy giá, currency và chu kỳ trực tiếp từ Billing.
- [ ] Xử lý purchase, pending, canceled, expired và restore.
- [ ] Acknowledge giao dịch đúng hạn.
- [ ] Không hiển thị ads trên Player hoặc trong lúc loading playlist.
- [ ] Kiểm tra Premium bằng tài khoản test license.
- [ ] Dùng test ad unit khi debug; không click quảng cáo thật.
- [ ] Kiểm thử mất mạng, xoay màn hình, background/foreground và cài lại app.
- [ ] Kiểm tra link Privacy Policy, Terms và Manage subscription.
- [ ] Kiểm tra tất cả ngôn ngữ đang hỗ trợ, tối thiểu English và Vietnamese.

## 10. Kịch bản ngắn để team triển khai

```text
Free user mở app
  -> Xem Home/Channel có banner
  -> Thêm playlist hoặc xem kênh
  -> Sau frequency cap, interstitial xuất hiện ở điểm chuyển tự nhiên
  -> User có thể vào Settings > Go Premium
  -> Chọn Yearly hoặc Monthly
  -> Google Play xác nhận giao dịch
  -> App xác minh entitlement
  -> Tắt toàn bộ quảng cáo và hiển thị Premium active

Premium user mở app
  -> Query entitlement
  -> Không khởi tạo/không hiển thị quảng cáo
  -> Sử dụng toàn bộ luồng xem như bình thường
  -> Có thể Restore purchases hoặc Manage subscription
```

---

# 11. Tổng hợp toàn bộ screen để phân tích sâu

Phần này là bản đồ sản phẩm theo các màn hình đang có trong source. Cột “Kiếm tiền” là cơ hội phù hợp, không có nghĩa phải đặt quảng cáo ở tất cả vị trí.

## 11.1. Sơ đồ luồng tổng thể

Splash
  -> Language lần đầu
  -> Onboarding / Intro
  -> Personalization
  -> MainActivity
       ├─ Home
       │    ├─ Watch Now
       │    ├─ Playlist detail
       │    ├─ Add Channel
       │    └─ How to Add Channel
       ├─ Channel
       │    └─ Playlist detail -> Player
       ├─ Favorites
       │    └─ Player
       └─ Settings
            ├─ Language
            ├─ How to Add Channel
            └─ About Us

## 11.2. Bảng tổng hợp nhanh

| Screen | Mục đích | Giá trị người dùng | Kiếm tiền phù hợp | Ưu tiên |
|---|---|---|---|---|
| Splash | Khởi tạo app | Vào app nhanh | App Open chỉ từ lần mở sau | Không quảng cáo lần đầu |
| Language | Chọn ngôn ngữ | Cá nhân hóa | Không quảng cáo | Trải nghiệm đầu |
| Onboarding | Giải thích tính năng | Hiểu app trước khi dùng | CTA mềm đến Premium | Không interstitial |
| Personalization | Chọn sở thích | Gợi ý nội dung phù hợp | Có thể test paywall sau hoàn tất | Không khóa |
| Home | Trung tâm khám phá | Xem playlist, thêm nội dung | Banner, native, CTA Premium | Cao |
| Channel | Duyệt toàn bộ kênh | Tìm kiếm/lọc kênh | Banner, native | Cao |
| Favorites | Mở kênh yêu thích | Truy cập nhanh | Banner nhẹ | Trung bình |
| Watch Now | Tiếp tục xem | Mở lại nội dung gần đây | Banner ngoài player | Trung bình |
| Playlist detail | Xem kênh trong playlist | Chọn kênh | Banner, interstitial sau hành động | Cao |
| Player dọc | Phát kênh | Giá trị cốt lõi | Không quảng cáo trên player | Bảo vệ UX |
| Player ngang | Xem fullscreen | Trải nghiệm xem tốt nhất | Không quảng cáo trong lúc phát | Bảo vệ UX |
| Add Channel | Thêm URL/File/Gallery | Tạo thư viện riêng | Paywall mềm nếu có giới hạn | Cao |
| Guide | Hướng dẫn thêm kênh | Giảm lỗi sử dụng | Banner cuối trang, CTA Add | Thấp |
| Settings | Quản lý app | Ngôn ngữ, hướng dẫn, thông tin | Điểm đặt Go Premium tốt nhất | Rất cao |
| Language Settings | Đổi ngôn ngữ | Cá nhân hóa | Không quảng cáo | Thấp |
| About Us | Tin cậy, chia sẻ, đánh giá | Hỗ trợ sản phẩm | Card Premium | Trung bình |
| Playback Error Dialog | Xử lý stream lỗi | Thử lại/chuyển kênh | Không quảng cáo | Bắt buộc sạch |
| Quit Dialog | Xác nhận thoát | Tránh thoát nhầm | Không ép xem ad | Không kiếm tiền |

## 11.3. Phân tích từng screen

### A. Splash Screen

Chức năng: khởi tạo app, kiểm tra cấu hình, điều hướng sang Language/Intro/Main.

Kịch bản:

1. User mở app.
2. App kiểm tra trạng thái đã hoàn tất onboarding.
3. Điều hướng sang luồng phù hợp.
4. Không giữ user ở splash lâu chỉ để chờ quảng cáo.

Monetization:

- Không interstitial.
- Không App Open ở lần cài đầu.
- App Open chỉ dùng khi app quay lại từ background theo cooldown.
- Không đặt banner vì đây là màn hình chuyển tiếp.

KPI: thời gian cold start đến Home, crash-free startup, tỷ lệ hoàn tất onboarding.

### B. Language Screen

Chức năng: chọn ngôn ngữ khi cài lần đầu.

Phân tích:

- User chưa hiểu sản phẩm và chưa có ý định mua.
- Lưu ngôn ngữ trước khi chuyển màn hình.
- Nút tiếp tục luôn rõ ràng.

Monetization: không IAA, không Paywall. Đây là bước giảm rào cản sử dụng.

### C. Onboarding / Intro Screen

Thông điệp hiện có:

- Live TV, Wherever You Are.
- Your Content in One Place.
- Make It Truly Yours.
- Watch Your Way.

Kịch bản:

1. Hiển thị 3–4 slide ngắn.
2. User bấm Next hoặc Get Started.
3. Slide cuối chuyển sang Personalization.
4. Lưu trạng thái hoàn tất để không lặp lại.

Monetization:

- Không interstitial sau slide.
- Không App Open.
- Có thể thêm thông điệp mềm ở slide cuối: Enjoy a cleaner viewing experience with Premium.
- Không dùng nút mua làm CTA chính.

### D. Personalization Screen

Chức năng: chọn category như News, Movies, Sport, Kids, Music, Documentary.

Kịch bản:

1. Hiển thị category.
2. User chọn tối thiểu một mục.
3. Lưu lựa chọn.
4. Chuyển vào Home.

Cơ hội Premium:

- Có thể test giới thiệu Premium nhẹ sau Continue ở một tỷ lệ nhỏ user.
- Không mở purchase flow tại đây.
- Không hiển thị quảng cáo trước khi user thấy giá trị sản phẩm.

KPI: tỷ lệ chọn category, hoàn tất personalization, chuyển sang Home.

### E. MainActivity / Shell

MainActivity chứa bottom navigation với bốn tab: Home, Channel, Favorites, Setting.

Quy tắc chung:

- Banner không che bottom navigation.
- Chuyển tab không tự show interstitial liên tục.
- Kiểm tra Premium trước khi render ads.
- Khi mở Player, dừng logic show ads.
- Frequency cap dùng chung toàn app, không tính riêng từng tab.

### F. Home Screen

Nội dung: hero/banner, playlist/category, Watch Now, Add New, danh sách nội dung/kênh.

Hành vi:

- Tap playlist -> Playlist detail.
- Tap Watch Now -> Watch Now.
- Tap Add New -> Add Channel.
- Tap How to Add Channel -> Guide.

Monetization:

- Banner ở cuối Home.
- Native ad sau item 8–10 nếu danh sách đủ dài.
- Card Remove Ads/Go Premium nhỏ, không che nội dung.
- Interstitial chỉ sau hành động hoàn tất hoặc khi rời Player.
- Không show ad ngay khi Home vừa load.

Paywall trigger tốt: user đã thêm playlist hoặc đã xem ít nhất một kênh rồi bấm Remove Ads.

### G. Channel Screen

Chức năng: hiển thị toàn bộ channel, search và filter theo group/category.

Monetization:

- Banner cuối danh sách.
- Native ad thưa, gắn nhãn Ad rõ ràng.
- Không interstitial khi user chỉ search hoặc đổi filter.
- Khi user mở Player rồi Back, cộng một lượt xem để tính cap.

KPI: search-to-play, channel open rate, favorite conversion, ad impressions/session.

### H. Favorites Screen

Chức năng: hiển thị channel đã lưu và empty state khi chưa có Favorite.

Empty state nên dùng:

> Chưa có kênh yêu thích. Hãy mở một kênh và nhấn biểu tượng trái tim để lưu lại.

CTA phụ: Browse channels.

Monetization:

- Banner nhẹ ở cuối danh sách.
- Không interstitial khi bấm heart.
- Không đặt Paywall ở empty state; ưu tiên giúp user có Favorite đầu tiên.
- Native ad chỉ dùng nếu danh sách đủ dài.

### I. Watch Now Screen

Chức năng: tiếp tục xem hoặc hiển thị channel gần đây/đề xuất.

Monetization:

- Banner ngoài khu vực player.
- Không show interstitial ngay trước khi phát.
- Nếu cần interstitial, show sau khi thoát player và đủ cap.
- Đây là screen có ý định xem cao nên ưu tiên UX.

### J. Playlist Detail Screen

Chức năng: hiển thị channel trong một playlist cụ thể.

Hành vi:

- Duyệt, search/filter channel.
- Tap channel -> Player.
- Quản lý hoặc xóa playlist nếu có nút.

Monetization:

- Banner cuối danh sách.
- Native ad sau item 8–10.
- Interstitial sau khi user rời playlist hoặc hoàn tất thao tác quản lý, không phải khi tap channel.
- Card Premium ở header phụ: Remove ads while browsing.

Paywall trigger: sau khi user mở cùng playlist 2–3 lần và có hành vi lặp lại.

### K. PlayChannelActivity / Player dọc

Chức năng: phát stream, chuyển channel, Favorite, sleep timer và điều khiển xem.

Đây là vùng bảo vệ trải nghiệm:

- Không banner.
- Không interstitial khi stream đang phát.
- Không App Open.
- Không native ad.
- Không Paywall che player.
- Không chuyển sang quảng cáo khi stream đang buffering.

Kịch bản kết thúc:

1. User Back hoặc đóng Player.
2. Lưu Watch History.
3. Kiểm tra frequency cap.
4. Nếu đủ điều kiện và ad preload sẵn, show interstitial sau khi Player đã đóng.
5. Ad đóng xong mới quay về danh sách.

### L. ChannelHorActivity / Player ngang

Chức năng: fullscreen landscape, điều khiển phát, chuyển channel và thoát fullscreen.

Quy tắc:

- Không quảng cáo trên vùng video.
- Không đổi orientation để ép xem quảng cáo.
- Không interstitial khi chuyển channel.
- Khi thoát fullscreen, chỉ cập nhật trạng thái.

KPI: play start success, watch duration, buffering rate, player exit rate.

### M. Add Channel Screen

Có ba hướng: URL, File và Gallery.

#### Quy tắc quota chung

1. Khi mở Add Channel, hiển thị quota còn lại ở đầu screen.
2. User vẫn được nhập/chọn dữ liệu bình thường khi còn lượt.
3. Khi bấm Add Playlist, kiểm tra quota trước khi bắt đầu xử lý.
4. Nếu quota còn, xử lý import; chỉ trừ lượt sau khi lưu thành công.
5. Nếu hết quota, không bắt đầu tải hoặc parse; mở Paywall giải thích lý do.
6. Nếu user đóng Paywall, quay lại Add Channel và giữ nguyên dữ liệu đã nhập/chọn.
7. Nếu import cùng nguồn đã tồn tại, xử lý như cập nhật playlist, không trừ lượt.

#### M1. Add via URL

Flow:

1. Mở Add New.
2. Chọn tab URL.
3. Nhập tên playlist.
4. Dán M3U/M3U8 URL hoặc chọn sample.
5. Nhấn Add Playlist.
6. App tải, parse và lưu.
7. Hiển thị success hoặc error.

Monetization:

- Không show ad khi đang nhập URL.
- Không interstitial trước Add Playlist.
- Sau lần import thành công thứ hai, hiển thị quota còn lại; không mở Paywall ngay nếu user chưa có ý định thêm tiếp.
- Sau lần import thành công thứ ba, hiển thị success trước; lần bấm Add Playlist tiếp theo mới mở Paywall.
- Nếu hết quota, cho phép chọn Premium hoặc rewarded ad thêm 1 lượt.

#### M2. Add via File

Flow: chọn File, nhập tên, chọn .m3u/.m3u8, kiểm tra, parse, lưu và hiển thị số channel.

Monetization: giống URL. Không quảng cáo trong file picker, parsing hoặc loading. Chỉ trừ quota sau khi file đã parse và lưu thành công.

#### M3. Add from Gallery

Flow: chọn Gallery, nhập tên collection, chọn nhiều video, xóa thumbnail nếu cần rồi lưu.

Monetization:

- Không show ad trong gallery picker.
- Gallery cũng dùng chung quota 3 playlist import thành công.
- Có thể dùng Premium để thêm collection không giới hạn.
- Không làm mất dữ liệu đã chọn khi Paywall đóng.

### N. How To Add Channel / Guide Screen

Chức năng: giải thích 3 cách thêm channel, FAQ và CTA Add a channel now.

Cơ hội:

- Banner ở cuối trang nếu cần IAA.
- CTA cuối trang: Add a channel now.
- Card nhỏ: Premium removes ads while you manage your library.
- Không show interstitial giữa các bước.

KPI: guide view -> add channel, FAQ expansion, CTA click.

### O. Setting Screen

Đây là vị trí Paywall tốt nhất vì user chủ động vào để quản lý trải nghiệm.

Nhóm nội dung hiện có: Language, About Us, How to Add Channel, Rate Us, Share App, Terms, Privacy Policy và Quit.

Bố cục đề xuất:

1. Card Premium ở đầu screen.
2. Trạng thái Free plan hoặc Premium active.
3. Nút Go Premium hoặc Manage subscription.
4. Setting còn lại bên dưới.
5. Banner thấp hơn card Premium nếu còn không gian.

Không nên:

- Tự mở Paywall khi user chỉ vào Settings.
- Interstitial trước Language/Privacy Policy.
- Wording gây áp lực như You must pay to continue.

### P. Language Setting Screen

Chức năng: thay đổi ngôn ngữ sau khi vào app.

Monetization: không quảng cáo. Đổi ngôn ngữ không được reset entitlement, playlist hoặc frequency cap.

Kiểm thử: toàn bộ Paywall, lỗi Billing và ad label phải có bản dịch.

### Q. About Us Screen

Chức năng: giới thiệu app, version, share, rate, Terms và Privacy Policy.

Cơ hội:

- Card Support StreamWave TV – Go Premium.
- Khi bấm Rate hoặc Share, không show interstitial ngay sau thao tác.
- Privacy Policy và Terms phải mở được khi chưa mua Premium.

KPI: rate click, share click, policy open, Premium click.

### R. Playback Error Dialog

Kịch bản:

1. Player phát hiện lỗi.
2. Hiển thị Unable to play this channel.
3. Cho phép Next channel hoặc Try again.
4. Không tính lỗi phát là một phiên xem hoàn tất.
5. Không show quảng cáo trong dialog.

Lý do: user đang thất vọng; quảng cáo ở đây dễ làm tăng uninstall và review xấu.

### S. Quit Dialog

Chức năng: xác nhận thoát app.

Monetization: không interstitial và không rewarded ad để trì hoãn việc thoát. Chỉ có Cancel và Quit rõ ràng.

## 11.4. Ma trận quyết định quảng cáo

| Tình huống | Banner | Interstitial | App Open | Rewarded | Paywall |
|---|---:|---:|---:|---:|---:|
| Lần mở app đầu tiên | Không | Không | Không | Không | Không |
| Home đã load | Có | Không ngay | Không | CTA tùy chọn | Card mềm |
| Đổi tab liên tục | Có | Theo cooldown | Không | Không | Không |
| Mở Player | Không | Không | Không | Không | Không |
| Đóng Player | Ngoài player | Có nếu đủ cap | Không | Không | Có thể sau nhiều lần |
| Thêm playlist thành công | Có ở list | Có theo cap | Không | Không | Có nếu đạt giới hạn |
| Settings | Có nếu phù hợp | Không tự động | Không | Có | Vị trí chính |
| Guide | Có cuối trang | Không giữa bước | Không | Không | Card mềm |
| Playback Error | Không | Không | Không | Không | Không |
| Premium active | Không | Không | Không | Ẩn CTA | Manage subscription |

## 11.5. Danh sách trigger ưu tiên

### P0 — nên làm trước

1. Banner Home/Channel/Favorites.
2. Interstitial sau khi đóng Player, cooldown 120 giây.
3. App Open cooldown 4 giờ.
4. Paywall từ Settings.
5. Premium tắt toàn bộ ads.
6. Restore purchases và Manage subscription.
7. Analytics cho purchase/ad events.

### P1 — làm sau khi có dữ liệu

1. Rewarded 30 phút tắt quảng cáo.
2. Rewarded thêm 1 playlist sau khi hết quota.
3. Native ad trong danh sách dài.
4. Paywall sau khi hết 3 lượt import.
5. A/B test Yearly mặc định và Monthly.
6. Remote Config quota và frequency cap.

### P2 — chỉ làm khi cần

1. Weekly subscription.
2. Remove Ads Lifetime.
3. Chuyển quota 3 playlist sang quota theo tháng.
4. Offer/trial theo quốc gia.
5. Cá nhân hóa Paywall theo hành vi.

## 11.6. Sự kiện Analytics theo screen

| Event | Screen | Khi nào gửi |
|---|---|---|
| screen_view | Tất cả | Khi screen visible |
| onboarding_complete | Onboarding | Bấm Get Started |
| personalization_complete | Personalization | Lưu category |
| playlist_add_start | Add Channel | Bắt đầu nhập/chọn |
| playlist_add_success | Add Channel | Lưu thành công |
| playlist_add_failed | Add Channel | Parse/save lỗi |
| channel_play_start | Player | Stream bắt đầu |
| channel_play_error | Player | Phát lỗi |
| channel_play_end | Player | User thoát player |
| favorite_added | Channel/Player | Bấm Favorite |
| paywall_view | Home/Settings/Player exit | Paywall visible |
| plan_selected | Paywall | Chọn gói |
| purchase_started | Paywall | Mở Google Play |
| purchase_success | Paywall | Xác minh thành công |
| restore_success | Settings/Paywall | Khôi phục thành công |
| ad_impression | Home/Channel/Guide | Ad impression callback |
| interstitial_shown | Sau action | Ad được hiển thị |
| reward_earned | Settings/Paywall | Earned reward |

## 11.7. Kết luận phân tích

- Player là trải nghiệm cốt lõi, không đặt IAA trực tiếp lên màn hình này.
- Home và Settings là hai nơi quan trọng nhất: Home tạo nhận biết, Settings tạo chuyển đổi.
- Add Channel và Playlist detail có intent cao; chỉ mở Paywall sau khi user nhận được giá trị hoặc đạt giới hạn rõ ràng.
- Giới hạn hợp lý là 3 playlist import thành công trọn đời; không tính lỗi, retry, playlist mẫu hoặc cập nhật playlist cũ.
- Khi hết quota, nên có rewarded ad cho thêm 1 lượt để giảm cảm giác bị chặn đột ngột.
- Onboarding, Language, Error Dialog và Quit Dialog nên sạch quảng cáo.
- Giai đoạn đầu nên phát hành với Banner + Interstitial có cap + Paywall Yearly/Monthly. Sau khi có dữ liệu mới thêm Rewarded, Native và Weekly.


### 4.3. App Open Ads

**Vị trí:** khi người dùng quay lại app sau một khoảng thời gian, không phải mọi lần mở activity.

Điều kiện đề xuất:

- Chỉ hiển thị từ lần mở app thứ hai trở đi.
- Chỉ show nếu app ở background tối thiểu 30 giây.
- Cooldown tối thiểu 4 giờ mỗi người dùng.
- Không show khi đang mở Player, Add Playlist hoặc Paywall.
- Không show trong 2 phút đầu sau khi cài app.
- Không show cho Premium.

**Mẫu flow:**

```text
App resume
  -> Kiểm tra Premium + consent + cooldown
  -> Kiểm tra app-open ad đã preload
  -> Nếu đủ điều kiện: hiển thị ad
  -> Sau khi đóng: tiếp tục màn hình trước đó
```

### 4.4. Rewarded Ads

Rewarded ad là tùy chọn, người dùng chủ động bấm xem để nhận quyền lợi. Không tự bật rewarded ad.

Reward đề xuất:

- `Watch 1 ad to remove ads for 30 minutes`
- Bản tiếng Việt: `Xem 1 quảng cáo để tắt quảng cáo trong 30 phút`
- `Watch 1 ad to add 1 more playlist`
- Bản tiếng Việt: `Xem 1 quảng cáo để thêm 1 playlist`

Flow:

1. Hiển thị nút `Watch ad` trong Settings hoặc Paywall phụ.
2. Giải thích phần thưởng trước khi xem.
3. Người dùng xác nhận.
4. Hiển thị rewarded ad.
5. Chỉ cộng phần thưởng khi callback xác nhận earned reward.
6. Lưu thời gian hết hạn local và đồng bộ trạng thái UI.
7. Nếu ad lỗi hoặc người dùng đóng sớm: không cấp thưởng, hiển thị `No reward was granted`.

Với reward thêm playlist:

- Chỉ cấp lượt sau callback xác nhận earned reward.
- Mỗi lượt rewarded chỉ mở đúng 1 lần import thành công.
- Không cộng lượt nếu user xem xong nhưng import vẫn thất bại.
- Giới hạn 1–2 lượt bổ sung/ngày và lưu ngày reset rõ ràng.

Không dùng rewarded ad để mở khóa nội dung mà ứng dụng không có quyền phân phối.

### 4.5. Native Ads (tùy chọn)

Chỉ dùng trong danh sách dài như Home/Channel, chèn sau item thứ 8–10 và tối đa 1 native ad mỗi 10 item. Gắn nhãn `Ad` rõ ràng. Nếu giao diện hiện tại chưa có card phù hợp, bỏ qua native ads để tránh làm rối UX.

### 4.6. Bản đồ placement quảng cáo đang hỗ trợ

Các placement dưới đây tương ứng với cấu hình Remote Config và code wrapper hiện tại. Tên placement nên được giữ ổn định để dễ theo dõi analytics và A/B test.

| Loại | Placement/code | Vị trí đề xuất | Điều kiện hiển thị |
|---|---|---|---|
| Banner | `BannerAll` | Home, Channel, Favorites, Watch Now, Playlist detail, Guide, Settings | Cuối nội dung; không dùng trong Player |
| Banner | `BannerSplash` | Splash hoặc màn hình chuyển tiếp ngắn | Chỉ khi không làm chậm cold start; không dùng lần mở đầu nếu chưa có consent |
| Native | `Language1` | Language bước 1 | Chỉ test sau khi nội dung và nút Continue đã hiển thị |
| Native | `Language2` | Language bước 2 | Không che lựa chọn ngôn ngữ; có thể tắt riêng theo quốc gia |
| Native | `NativeSplash` | Splash | Tùy chọn; không chặn điều hướng và không đợi ad quá lâu |
| Native | `Onboarding` | Slide onboarding | Tối đa 1 card ở slide cuối; không đặt sát CTA chính |
| Native | `OnboardingFull` | Trang onboarding riêng | Chỉ dùng một biến thể, không dùng đồng thời với `Onboarding` |
| Native | `NativeFeature` | Card giữa các tính năng trên Home/Guide | Gắn nhãn `Ad`; tối đa 1 card mỗi màn hình |
| Native | `NativeMediumAll` | Item trong danh sách Home/Channel/Playlist | Sau item 8–10, lặp lại tối thiểu mỗi 10 item |
| Native | `NativeSmallAll` | Card nhỏ hoặc khu vực cuối danh sách | Dùng khi không đủ chiều cao cho native medium |
| Native banner | `BannerAll` trong `NativeBannerPlacement` | Khu vực inline giữa các block nội dung | Chọn một loại: native banner hoặc banner thường, không render cả hai |
| Rewarded | `RewardEdit` | Hết quota import hoặc tính năng cần thêm 1 lượt | User chủ động bấm xem; chỉ cấp thưởng khi callback `onRewarded` |

#### Quy tắc phối hợp nhiều ad format

- Một màn hình chỉ nên có tối đa **1 banner + 1 native**; không đặt native medium, native small và native banner cùng lúc.
- Không hiển thị `BannerSplash` và `NativeSplash` đồng thời trong cùng một lần mở app.
- `Language1`, `Language2`, `Onboarding` và `OnboardingFull` là các biến thể thử nghiệm; chỉ bật một nhóm theo Remote Config.
- Native ad phải nằm trong scroll content, không đẩy nút Continue, Add Playlist, Play hoặc bottom navigation ra ngoài vùng dễ bấm.
- Ad chưa load thì trả về layout tự nhiên, không để khoảng trống và không retry liên tục khi user đang thao tác.
- Khi `isPremium = true`, không preload, không render và không hiển thị CTA rewarded.
- Consent chưa hoàn tất, mạng lỗi hoặc app đang background thì không gọi show ad.

#### Kịch bản native trong danh sách

```text
Mở Home/Channel/Playlist detail
  -> Render item nội dung trước
  -> Nếu danh sách >= 10 item và user là Free: preload NativeMediumAll
  -> Chèn ad sau item 8 hoặc 10
  -> Khi scroll đến ad: kiểm tra Premium + consent + lifecycle
  -> Ad load thành công: render card có nhãn Ad
  -> Ad lỗi: xóa item quảng cáo, không giữ placeholder
```

Không chèn native sau mỗi lần refresh hoặc khi user chỉ search/filter. Tối đa một impression native cho mỗi placement trong một phiên màn hình; nếu user rời rồi quay lại, áp dụng frequency cap dùng chung.

#### Kịch bản banner và native banner

```text
Screen visible
  -> Nội dung chính đã render
  -> Kiểm tra Premium, consent và ad enable
  -> Chọn banner thường hoặc native banner theo Remote Config
  -> Preload/load ad
  -> Thành công: hiển thị ở vùng cuối hoặc inline đã định trước
  -> Thất bại: ẩn container và giải phóng khoảng trống
```

Banner có thể reload theo `timeReloadBanner`, nhưng không reload chỉ vì recomposition, đổi filter hoặc xoay màn hình. Khi chuyển vào Player phải dừng reload và gỡ banner khỏi vùng player.

#### Kịch bản rewarded `RewardEdit`

Rewarded chỉ dùng cho quyền lợi có giá trị rõ ràng, ví dụ:

- `Xem quảng cáo để thêm 1 playlist` khi quota đã hết.
- `Xem quảng cáo để mở thêm 1 lượt chỉnh sửa/import` nếu sản phẩm cần giới hạn thao tác.
- `Xem quảng cáo để tắt quảng cáo trong 30 phút` nếu đã triển khai trạng thái temporary ad-free.

Flow chuẩn:

```text
User bấm Watch ad
  -> Hiển thị reward và thời hạn/quyền lợi trước khi xem
  -> Nếu ad chưa sẵn sàng: báo rõ và giữ nguyên trạng thái, không tự mở ad khác
  -> Show rewarded
  -> onRewarded: cấp đúng 1 reward và ghi analytics
  -> onDismissed: tiếp tục flow hiện tại
  -> User đóng sớm/error: không cấp reward
```

Không cấp thưởng trong `onDismissed`; callback đóng quảng cáo chỉ có nghĩa là kết thúc UI. Với reward thêm playlist, chỉ trừ reward sau khi import thành công; import lỗi không được làm mất lượt đã xem quảng cáo. Giới hạn đề xuất: tối đa 3 rewarded/ngày cho mỗi loại reward và cooldown 60 giây giữa hai lần gọi.

#### Ma trận rewarded mở rộng

| Reward placement | Quyền lợi | Điểm mở | Giới hạn |
|---|---|---|---:|
| `reward_add_playlist` | Thêm 1 playlist | Paywall sau khi quota hết | 1 lượt/import, tối đa 3/ngày |
| `reward_edit` | Thêm 1 lượt chỉnh sửa | Add Channel hoặc màn hình Edit | 3/ngày |
| `reward_ad_free_30m` | Tắt IAA 30 phút | Settings hoặc card Premium | 3/ngày, không cộng dồn quá 60 phút |
| `reward_unlock_session` | Một phiên xem sạch quảng cáo | Watch Now trước khi mở nội dung | 1 lần/phiên, không dùng trên Player |
| `reward_retry_import` | Retry một lần parse/import | Sau lỗi mạng tạm thời | Chỉ khi lỗi retryable, 2/ngày |
| `reward_feature` | Mở một tính năng phụ trong thời gian ngắn | Native Feature CTA hoặc Guide | Tối đa 1 lần cho mỗi feature/ngày |

Không hiển thị tất cả reward cùng lúc. Mỗi màn hình chỉ nên có một CTA rewarded rõ ràng, với copy nêu chính xác phần thưởng và thời hạn. Reward không được thay thế Premium khi quyền lợi liên quan đến xóa quảng cáo vĩnh viễn hoặc subscription.

#### Kịch bản banner dày nhưng an toàn

Để có nhiều banner mà không làm layout nhảy, chia theo nhóm screen và chỉ chọn một placement đang visible:

| Nhóm | Banner placement | Vị trí | Tối đa |
|---|---|---|---:|
| Browse | `banner_home`, `banner_channel` | Cuối màn hình danh sách | 1/screen |
| Library | `banner_favorite`, `banner_watch_now` | Sau nhóm nội dung cuối | 1/screen |
| Playlist | `banner_playlist_detail` | Cuối playlist detail | 1/screen |
| Support | `banner_guide`, `banner_settings` | Cuối nội dung | 1/screen |
| Transition | `banner_splash` | Splash/transition | 1/lần mở |

Không render banner ở cả parent và child cùng lúc. Khi screen có native banner inline, banner cuối màn hình phải bị tắt cho screen đó. Chỉ reload khi ad view đã đủ thời gian tối thiểu do Remote Config quy định hoặc khi placement thay đổi.

#### Kịch bản native nhiều vị trí

| Vị trí | Format | Quy tắc |
|---|---|---|
| Home hero dưới nhóm đầu | `NativeFeature` | 1 card, chỉ sau khi hero đã load |
| Home danh sách dài | `NativeMediumAll` | Sau item 8, lặp lại mỗi 10 item |
| Home compact/list ít chiều cao | `NativeSmallAll` | Thay medium, không dùng đồng thời |
| Channel sau filter result | `NativeMediumAll` | Chỉ khi có từ 10 kết quả, không show sau mỗi filter |
| Playlist detail | `NativeSmallAll` | Sau item 6 hoặc cuối danh sách |
| Guide FAQ | `NativeFeature` | Tối đa 1 card ở cuối Guide |
| Language/Onboarding | `Language1`, `Language2`, `Onboarding` | Chỉ bật một variant theo Remote Config |
| Splash | `NativeSplash` hoặc `OnboardingFull` | Chọn một, không chặn điều hướng |

Mỗi native card phải có label `Ad/Quảng cáo`, khoảng cách thị giác với item nội dung và vùng click đúng theo chuẩn ad view. Nếu danh sách ngắn, không cố chèn đủ số lượng ad; nội dung người dùng luôn được ưu tiên.

#### Cấu hình Remote Config đề xuất

```text
banner_all_enabled
banner_splash_enabled
native_language_enabled
native_splash_enabled
native_onboarding_enabled
native_onboarding_full_enabled
native_feature_enabled
native_medium_all_enabled
native_small_all_enabled
native_banner_enabled
native_insert_after_item = 8
native_repeat_every_items = 10
reward_edit_enabled
reward_max_per_day = 3
reward_cooldown_seconds = 60
```

Mỗi ad unit trong `list_ads` phải có `enable_ad` riêng để có thể xoay vòng unit, tắt một unit lỗi hoặc chạy A/B test mà không cần phát hành app mới. Chỉ dùng ad unit test trong debug/QA.

#### Analytics bắt buộc cho từng format

Gửi các event `ad_load_start`, `ad_loaded`, `ad_failed`, `ad_impression`, `ad_clicked`, `ad_closed` và `ad_not_ready` với thuộc tính `format`, `placement`, `screen`, `ad_unit`, `is_premium`, `consent_state` và `session_id`.

Rewarded bổ sung `reward_started`, `reward_earned`, `reward_not_earned`, `reward_applied` và `reward_apply_failed`. Không ghi nhận `reward_earned` chỉ dựa trên việc quảng cáo đã mở hoặc đã đóng.

---

## 5. Kịch bản Paywall / mua Subscription

### 5.1. Điểm mở Paywall

Thêm nút `Go Premium` ở:

- Settings: card cố định ở đầu màn hình.
- Khi người dùng đóng interstitial lần thứ ba trong một phiên.
- Sau khi rewarded ad hết hạn.
- Từ banner nhỏ “Remove ads” trên Home/Channel.

Không mở Paywall tự động ngay lần đầu mở app.

### 5.2. Paywall chính

**Tiêu đề:**

> Watch without interruptions

**Mô tả:**

> Enjoy StreamWave TV with a cleaner viewing experience and no advertising interruptions.

**Bản tiếng Việt:**

> Xem liền mạch, không bị gián đoạn

> Tận hưởng StreamWave TV với giao diện gọn gàng và không có quảng cáo làm gián đoạn trải nghiệm.

**Danh sách quyền lợi:**

- No banner ads
- No interstitial ads
- No app-open ads
- Watch your playlists with a cleaner experience
- Restore your purchase anytime

**CTA:** `Unlock Premium`

**Link phụ:**

- `Restore purchases`
- `Terms of Service`
- `Privacy Policy`
- `Manage subscription`

### 5.3. Mẫu khi người dùng bấm mua

```text
User chọn gói
  -> Hiển thị giá, chu kỳ và điều khoản lấy từ Google Play
  -> User bấm Unlock Premium
  -> Mở Google Play purchase flow
  -> Purchase thành công
  -> Xác minh purchase token
  -> Acknowledge/consume đúng loại sản phẩm
  -> Cập nhật entitlement premium = true
  -> Tắt quảng cáo ngay
  -> Hiển thị màn hình Success
```

**Success copy:**

> Premium is active

> All advertising has been removed. Enjoy your viewing experience.

Bản tiếng Việt:

> Premium đã được kích hoạt

> Quảng cáo đã được tắt. Chúc bạn xem nội dung thoải mái hơn.

### 5.4. Các trạng thái lỗi

| Tình huống | Thông báo |
|---|---|
| User hủy Google Play | `Purchase canceled. You can try again anytime.` |
| Sản phẩm chưa tải | `Plans are temporarily unavailable. Please try again later.` |
| Mất mạng | `Check your internet connection and try again.` |
| Giao dịch pending | `Your purchase is pending confirmation from Google Play.` |
| Token không hợp lệ | `We couldn't verify this purchase. Please contact support.` |
| Restore không tìm thấy | `No active Premium purchase was found for this account.` |

Không tự bật lại quảng cáo ngay khi có lỗi tạm thời nếu entitlement local vẫn còn hợp lệ; trạng thái phải được kiểm tra lại với Billing khi app resume.

---

## 6. IAP một lần (tùy chọn)

Nếu muốn có IAP không tự gia hạn, dùng một sản phẩm không tiêu thụ riêng:

| Sản phẩm | Product ID mẫu | Quyền lợi |
|---|---|---|
| Remove Ads Lifetime | `remove_ads_lifetime` | Tắt toàn bộ quảng cáo vĩnh viễn |

Paywall nên hiển thị lựa chọn:

- `Premium Yearly` – lựa chọn đề xuất.
- `Remove Ads Lifetime` – trả một lần, không gia hạn.

Không hiển thị “Lifetime” nếu backend không thể khôi phục quyền lợi bằng purchase history. Sản phẩm non-consumable phải được acknowledge và restore được trên thiết bị khác.

---

## 7. Logic entitlement đề xuất

```text
isPremium =
  active subscription đã xác minh
  OR remove_ads_lifetime đã xác minh

if isPremium:
  disable banner, interstitial, app-open, native, rewarded CTA
else:
  enable ads theo frequency cap
```

Khi app khởi động:

1. Đọc trạng thái local để UI phản hồi nhanh.
2. Kết nối Billing và query purchases hiện tại.
3. Xác minh trạng thái subscription/non-consumable.
4. Cập nhật entitlement.
5. Chỉ khởi tạo ad sau khi đã biết trạng thái Premium, hoặc dùng trạng thái an toàn để tránh nhấp nháy quảng cáo.

Sự kiện Analytics nên có:

`paywall_view`, `plan_selected`, `purchase_started`, `purchase_success`, `purchase_failed`, `purchase_pending`, `restore_started`, `restore_success`, `restore_empty`, `subscription_canceled`, `ad_impression`, `ad_failed`, `interstitial_shown`, `reward_started`, `reward_earned`, `reward_not_earned`.

Thuộc tính nên gửi kèm: `screen`, `placement`, `plan_id`, `price`, `currency`, `is_premium`, `app_version`, `country`.

---

## 8. Tần suất ban đầu và A/B test

### Cấu hình an toàn để phát hành

- Banner: Home/Channel/Favorites, không có trong Player.
- Interstitial: mỗi 3 phiên xem hoặc 2 lần thêm playlist, cooldown 120 giây.
- App Open: cooldown 4 giờ, background tối thiểu 30 giây.
- Rewarded: 30 phút tắt quảng cáo, tối đa 3 lần/ngày.
- Paywall: Settings và sau các điểm có ý định rõ ràng; tối đa 2 lần/ngày.

### Biến Remote Config nên có

`ads_enabled`, `banner_enabled`, `interstitial_enabled`, `interstitial_cooldown_seconds`, `interstitial_watch_count`, `app_open_enabled`, `app_open_cooldown_hours`, `reward_enabled`, `reward_duration_minutes`, `paywall_variant`, `yearly_discount_text`.

### Chỉ số theo dõi

- Ad impressions per DAU.
- ARPDAU từ IAA.
- Paywall view → purchase conversion.
- Subscription trial/paid conversion nếu có trial.
- D1/D7 retention.
- Crash rate và thời gian mở Player.
- Tỷ lệ gỡ app sau khi tăng tần suất interstitial.

Không chạy A/B test nhiều biến cùng lúc. Ưu tiên test: Paywall copy → thứ tự gói → giá → frequency cap.

---
