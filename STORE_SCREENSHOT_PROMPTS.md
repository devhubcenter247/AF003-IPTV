# Store Screenshot Prompts — StreamWave TV

6 màn hình được chọn để làm ảnh screenshot quảng bá cho Google Play / App Store, kèm prompt chi tiết cho từng màn. Khác với ảnh onboarding (phong cách 3D, mờ ảo, không chữ), **ảnh screenshot store phải là mockup phẳng, sắc nét, đúng như ảnh chụp màn hình thật** — có chèn headline marketing trực tiếp trong ảnh (đúng convention store screenshot).

## 1. Quy tắc chung cho cả 6 prompt

Dán đoạn này vào đầu MỖI prompt bên dưới (đã có sẵn trong từng prompt, không cần copy lại) — liệt kê ở đây để bạn hiểu logic chung:

- **Không 3D, không góc nghiêng phối cảnh**: mockup điện thoại đặt thẳng, chính diện (0° tilt), giống ảnh chụp sản phẩm phẳng (flat-lay), không phải render isometric/xoay góc.
- **Không mờ ảo**: không glow lan toả, không bokeh, không depth-of-field blur, không soft-focus. Toàn bộ chi tiết UI phải sắc nét, rõ ràng như ảnh chụp màn hình thật (crisp, in-focus, pixel-sharp).
- **Có chữ marketing thật trong ảnh** (ngược với ảnh onboarding): headline lớn + subheadline nhỏ, đặt phía trên hoặc dưới mockup, không đè lên nội dung UI quan trọng.
- **Độ phân giải**: `1284x2778 px` (chuẩn App Store iPhone 6.5", cũng dùng tốt cho Google Play). Nếu cần bản Google Play riêng, export lại `1080x1920 px` cùng bố cục.
- **Bảng màu thương hiệu**: nền `#0A0A1A` (navy đậm), accent cyan `#22D3EE`, gradient `#00F5FF → #6366F1`, chữ chính trắng, chữ phụ xám `#A0A3B1`.
- **Không dùng logo/kênh thật có bản quyền**, không watermark, không QR/barcode.

---

## 2. Màn 1 — Home / Duyệt kênh

**File gợi ý:** `store_screenshot_1_home.png`
**Headline:** "Every Channel, One App" — Subhead: "Browse and organize all your live channels in one clean dashboard."

```text
Create a Google Play / App Store marketing screenshot, 1284x2778 pixels,
portrait. Flat, straight-on device mockup (0 degree tilt, no 3D perspective,
no rotation) of a modern bezel-less smartphone centered in frame, held
perfectly upright like a professional flat product photo — not an angled or
isometric render. Everything must be in crisp, sharp, in-focus detail with
no blur, no glow haze, no bokeh, no soft-focus lighting.

Background: solid deep navy #0A0A1A with a very subtle, barely-visible
diagonal gradient toward #1A1A2E in the corners — flat and clean, no
particles, no glowing orbs, no smoke effects.

Bold white headline text at the top of the canvas, large sans-serif bold
font: "Every Channel, One App". Below it, smaller gray subheadline: "Browse
and organize all your live channels in one clean dashboard."

Inside the phone screen mockup, recreate this exact app UI in sharp flat
detail:
- Top row: bold white app name text "StreamWave TV" on the left, small
  circular outlined info icon on the top right.
- A rounded-corner (12px radius) hero banner card below the header, thin
  white 50%-opacity border, showing an abstract dark blue/purple TV-static
  style background image (no real content, no logos), with a small
  pill-shaped button in the bottom-left corner of the banner filled with a
  cyan-to-indigo gradient (#00F5FF to #6366F1) and a white play-triangle
  icon, no visible text.
- Below the banner, a row of pill-shaped filter chips: the first chip is
  outlined white with a plus icon (add new), followed by 3-4 smaller chips
  in a horizontally scrollable row — one chip has a solid cyan-to-indigo
  gradient border to show it's selected, the others are plain thin white
  10%-opacity outlines with small flat icons only (globe, link, folder,
  device icons) — no text labels needed, icon-only chips.
- Below that, a vertical list of 4-5 playlist rows, each row: a small
  circular badge with a gradient cyan-to-indigo fill and a white number
  inside, a two-line text block (a bold white generic title bar shape and a
  smaller gray subtitle shape — represent as abstract rounded rectangles if
  exact text is not needed, not real words), and a small rounded translucent
  icon button on the right edge of the row.
- A bottom navigation bar fixed at the screen bottom with exactly 4 icons:
  home, TV/channel, heart (favorite), gear (settings) — the first "home"
  icon rendered in the cyan-to-indigo gradient with a small white label
  underneath, the other three icons flat gray with gray labels.

Sharp UI rendering style, flat design, no drop shadows except minimal subtle
card elevation, no 3D icons, no embedded watermark, no real brand logos, no
QR codes, no barcodes.
```

---

## 3. Màn 2 — Channel Reel (vuốt xem kênh kiểu TikTok)

**File gợi ý:** `store_screenshot_2_reel.png`
**Headline:** "Swipe. Watch. Repeat." — Subhead: "Flip through live channels with a single swipe, TikTok-style."

```text
Create a Google Play / App Store marketing screenshot, 1284x2778 pixels,
portrait. Flat, straight-on device mockup (0 degree tilt, no 3D perspective)
of a modern bezel-less smartphone centered in frame, professional flat
product photo style. Everything crisp and sharp, no blur, no glow haze, no
bokeh, no soft focus.

Background: solid deep navy #0A0A1A, flat and clean, no particles, no
glowing orbs.

Bold white headline text at the top: "Swipe. Watch. Repeat." Smaller gray
subheadline below it: "Flip through live channels with a single swipe,
TikTok-style."

Inside the phone screen mockup, recreate this exact full-bleed vertical
video-reel UI in sharp flat detail:
- The entire screen is filled edge-to-edge by one abstract dark
  blue-and-purple live-broadcast style background (representing a paused
  video frame, no real content, no logos, no real people/faces).
- A small rounded green pill badge in the top-right corner with a white dot
  and no text (representing a live indicator, icon-only, no letters).
- A vertical stack of 2 small circular icon buttons on the right edge,
  vertically centered-low: a heart/favorite icon (white outline) and a
  speaker/mute icon (white outline), each sitting on a softly rounded dark
  translucent circular backdrop, with a tiny gray caption label placeholder
  bar under each (short rounded rectangle shape, not real text).
- Bottom-left corner: a small circular channel-logo placeholder (plain
  rounded avatar with an abstract TV icon, no real logo), next to a small
  rounded pill tag in cyan with no text, and two lines of bold white text
  placeholder bars representing a channel name (use generic rounded bar
  shapes if avoiding real words, or the literal words "Channel One" in
  clean bold white sans-serif if legible text is acceptable).
- A soft dark gradient scrim fading from transparent to black only in the
  bottom 20% of the phone screen, purely for legibility contrast — this
  gradient must stay flat and subtle, not glowing or hazy.
- No bottom navigation bar visible on this screen (it's a full-bleed
  immersive video view).

Sharp UI rendering style, flat design, minimal subtle shadows only, no 3D
icons, no watermark, no real brand or channel logos, no real faces/people,
no QR codes, no barcodes.
```

---

## 4. Màn 3 — Trình phát trực tiếp (Live Player, dọc)

**File gợi ý:** `store_screenshot_3_player.png`
**Headline:** "Live TV, Instantly" — Subhead: "Tap and stream any channel in seconds — smooth and reliable."

```text
Create a Google Play / App Store marketing screenshot, 1284x2778 pixels,
portrait. Flat, straight-on device mockup (0 degree tilt, no 3D
perspective), professional flat product photo style. Crisp, sharp, in-focus
throughout — no blur, no glow haze, no bokeh.

Background: solid deep navy #0A0A1A, flat and clean.

Bold white headline text at the top: "Live TV, Instantly". Smaller gray
subheadline: "Tap and stream any channel in seconds — smooth and reliable."

Inside the phone screen mockup, recreate this exact live-player UI in sharp
flat detail:
- Top third of the screen: a solid black video player area showing an
  abstract dark blue stadium/broadcast-light scene (no real footage, no
  logos, no real people), with playback control overlay visible on top of
  it:
  - Top row inside the player: a small circular back-arrow button on the
    left, a bold white channel name label in the middle, and a small green
    pill badge with a white dot on the right (icon-only, no text).
  - Center of the player: three circular buttons in a horizontal row —
    a smaller skip-previous icon button, a larger centered play/pause
    button (filled cyan-to-indigo gradient circle with a white play
    triangle), and a smaller skip-next icon button.
  - Bottom-right corner of the player, a tight row of 5 small circular
    icon buttons: heart/favorite (cyan tinted), expand/fullscreen arrows,
    cast/screen-connect icon, a crescent-moon clock icon (sleep timer), and
    a padlock icon.
- Below the player area (bottom two-thirds of the phone screen): a rounded
  search bar with a magnifier icon and placeholder text "Search channels...",
  followed by a vertical list of 4-5 channel rows, each with a small
  rounded logo placeholder square, a bold white title bar, and a thin cyan
  outline highlighting one row as "currently selected".

Sharp UI rendering style, flat design, minimal shadows, no 3D icons, no
watermark, no real broadcaster logos, no QR codes, no barcodes.
```

---

## 5. Màn 4 — Toàn màn hình / Cast màn hình lớn

**File gợi ý:** `store_screenshot_4_fullscreen.png`
**Headline:** "Go Big Screen" — Subhead: "Fullscreen playback and cast controls for the best seat in the house."

```text
Create a Google Play / App Store marketing screenshot, 1284x2778 pixels,
portrait. Flat, straight-on device mockup (0 degree tilt, no 3D
perspective), professional flat product photo style. Crisp, sharp, in-focus,
no blur, no glow haze, no bokeh.

Background: solid deep navy #0A0A1A, flat and clean. Optionally, faintly
visible in the far background behind the phone (much smaller scale, flat
silhouette, not glowing), a simple flat outline icon of a widescreen
television to visually hint at "casting to a bigger screen" — kept subtle
and not the main focus.

Bold white headline text at the top: "Go Big Screen". Smaller gray
subheadline: "Fullscreen playback and cast controls for the best seat in the
house."

Inside the phone screen mockup, recreate this exact fullscreen landscape
live-player UI, rotated so the phone mockup itself is shown in landscape
orientation (wider than tall) but still placed flat and straight-on with no
3D tilt, centered within the portrait canvas:
- The entire landscape screen is filled edge-to-edge by a solid black video
  area showing an abstract dark blue broadcast scene (no real footage, no
  logos), with a semi-transparent dark scrim overlay for the controls:
  - Top row: small circular back button on the left, bold white channel
    name in the middle, small green live-dot pill badge, and a small
    hamburger-menu icon button on the far right.
  - Center row: skip-previous, larger play/pause, skip-next circular
    buttons, evenly spaced and slightly larger than the portrait player.
  - Bottom-right row: heart/favorite icon, a "minimize" icon (diagonal
    arrows pointing inward), a cast/screen-connect icon, a crescent-moon
    clock icon (sleep timer), and a padlock icon — five small circular
    buttons in a tight row.
  - A thin vertical translucent panel sliding in from the right edge,
    covering about 30% of the landscape width, dark navy 90%-opacity
    background, containing a small search bar and 2-3 abstract channel-list
    row placeholders (rounded rectangles), representing an open channel
    list drawer.

Sharp UI rendering style, flat design, no 3D icons, no watermark, no real
brand or channel logos, no QR codes, no barcodes.
```

---

## 6. Màn 5 — Favorites & Lịch sử xem

**File gợi ý:** `store_screenshot_5_favorites.png`
**Headline:** "Yours, Organized" — Subhead: "Save favorites, revisit history, and find what you love faster."

```text
Create a Google Play / App Store marketing screenshot, 1284x2778 pixels,
portrait. Flat, straight-on device mockup (0 degree tilt, no 3D
perspective), professional flat product photo style. Crisp, sharp, in-focus,
no blur, no glow haze, no bokeh.

Background: solid deep navy #0A0A1A, flat and clean.

Bold white headline text at the top: "Yours, Organized". Smaller gray
subheadline: "Save favorites, revisit history, and find what you love
faster."

Inside the phone screen mockup, recreate this exact favorites/history UI in
sharp flat detail:
- Top row: a bold white screen title "Favorites", a small cyan text badge
  showing a channel count (e.g. a short number + label shape), and a small
  grid/list toggle icon button on the top-right.
- A pill-shaped segmented tab bar directly below, translucent white 5%
  background, containing 3 tabs: "All" (TV icon), "Favorites" (heart icon,
  shown as the selected tab with a solid rounded highlight and soft cyan
  glow-free flat fill), "History" (clock icon) — selected tab in white
  icon+label on a filled rounded pill, other two tabs flat gray icon+label.
- A rounded search bar below the tabs with a magnifier icon.
- A horizontally scrollable row of small pill filter chips below the search
  bar — one chip solid cyan filled (selected, "All"), 3-4 other chips thin
  white-outlined (unselected, generic short label bars).
- Main content area: a vertical list of 4 rows, each row containing: a
  small rounded-square logo placeholder thumbnail on the left, a bold white
  title bar and a small cyan-tinted rounded pill tag (group label) plus a
  small gray "time ago" label underneath, and on the right edge a small
  circular heart icon button filled solid cyan (indicating it's favorited).

Sharp UI rendering style, flat design, minimal shadows, no 3D icons, no
watermark, no real brand or channel logos, no QR codes, no barcodes.
```

---

## 7. Màn 6 — Thêm Playlist (Import M3U)

**File gợi ý:** `store_screenshot_6_addplaylist.png`
**Headline:** "Bring Your Own Playlist" — Subhead: "Import any M3U or M3U8 IPTV playlist in seconds."

```text
Create a Google Play / App Store marketing screenshot, 1284x2778 pixels,
portrait. Flat, straight-on device mockup (0 degree tilt, no 3D
perspective), professional flat product photo style. Crisp, sharp, in-focus,
no blur, no glow haze, no bokeh.

Background: solid deep navy #0A0A1A, flat and clean.

Bold white headline text at the top: "Bring Your Own Playlist". Smaller gray
subheadline: "Import any M3U or M3U8 IPTV playlist in seconds."

Inside the phone screen mockup, recreate this exact add-playlist UI in sharp
flat detail:
- Top row: small circular back button on the left, bold white title text
  "Add Channel to Playlist" next to it.
- A segmented tab row below with 3 pill tabs, each with a small flat icon:
  "URL" (link icon, shown as the selected tab with a solid filled rounded
  highlight, no glow), "File" (document icon, unselected, flat gray
  outline), "Gallery" (image-stack icon, unselected, flat gray outline).
- Below the tabs, a short gray instructional text line: "Paste a URL or tap
  a suggestion below".
- A numbered step layout: a small filled cyan circle with the number "1"
  next to a white rounded text-input field with a pencil icon and
  placeholder text "Playlist name"; below it a filled cyan circle with the
  number "2" next to a white rounded text-input field with a link icon and
  placeholder text "https://example.com/playlist.m3u".
- Below the inputs, a small rounded info tip box with a light-bulb icon and
  a short gray text line about downloading playlists.
- A thin divider line with small centered gray text "Or use a sample
  playlist".
- Below that, 3 horizontal suggestion cards stacked vertically, each with a
  small rounded-square icon avatar on the left (simple flat emoji-style
  icon: a sports ball, a newspaper, a film reel), a bold white short title
  bar and a smaller gray "~N channels" subtitle, and a small circular
  outlined checkmark icon on the right edge.
- Bottom of the screen: a full-width pill-shaped button filled with the
  cyan-to-indigo gradient (#00F5FF to #6366F1), bold white text "Add
  Playlist" centered inside it.

Sharp UI rendering style, flat design, minimal shadows, no 3D icons, no
watermark, no real brand logos, no QR codes, no barcodes.
```

---

## 8. Ghi chú khi generate

- Chạy từng prompt độc lập trong công cụ tạo ảnh AI bạn đang dùng (Claude web nếu có tool tạo ảnh, Midjourney, DALL·E, Gemini...).
- Sau khi có ảnh, kiểm tra lại: đúng độ phân giải, chữ headline không bị lỗi font/chính tả (AI hay sinh sai chữ — cần soát kỹ hoặc thêm chữ bằng tay sau nếu model render chữ xấu), UI bên trong khớp đúng bố cục thật của app để không gây hiểu nhầm cho người dùng khi review app lên store (Google Play/App Store có thể từ chối nếu screenshot khác quá xa app thật).
- Nếu công cụ tạo ảnh render chữ tiếng Anh trong ảnh bị lỗi (chuyện thường gặp với AI), cân nhắc: tạo ảnh KHÔNG có headline (bỏ đoạn "Bold white headline..." trong prompt), rồi tự chèn text bằng Figma/Photoshop sau — sẽ chính xác 100% về chữ.
