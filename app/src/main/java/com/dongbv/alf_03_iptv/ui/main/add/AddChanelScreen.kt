package com.dongbv.alf_03_iptv.ui.main.add

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.data.local.entity.MediaModel
import com.dongbv.alf_03_iptv.data.local.entity.SourceType
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ColumnParentContent
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.SpaceW
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.dashedBorder
import com.dongbv.alf_03_iptv.style.gradientContent
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.rememberToaster
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.AppNeonButton
import com.dongbv.alf_03_iptv.ui.component.backgroundState
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.viewmodel.AddItemViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgSecondary
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvError
import com.dongbv.alf_03_iptv.widget.IptvPrimaryGradient
import com.dongbv.alf_03_iptv.widget.IptvSuccess
import com.dongbv.alf_03_iptv.widget.IptvSurface
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.IptvTextTertiary
import com.dongbv.alf_03_iptv.widget.IptvWarning
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline500
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.dongbv.alf_03_iptv.widget.mainColor
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

// ── Data ──────────────────────────────────────────────────────────────────────

private data class Suggestion(
    val name: String,
    val url: String,
    val emoji: String,
    val description: String,
    val channelHint: String
)

private val urlSuggestions = listOf(
    Suggestion(
        "Family TV",
        "https://iptv-org.github.io/iptv/categories/family.m3u",
        "👨‍👩‍👧",
        "Family-friendly content for all ages",
        "~200 channels"
    ),
    Suggestion(
        "Sports 24/7",
        "https://iptv-org.github.io/iptv/categories/sports.m3u",
        "⚽",
        "Live sports, matches & events worldwide",
        "~300 channels"
    ),
    Suggestion(
        "World News",
        "https://iptv-org.github.io/iptv/categories/news.m3u",
        "📰",
        "International news in many languages",
        "~500 channels"
    ),
)

private val fileSuggestions = listOf(
    Suggestion(
        "Vietnam IPTV",
        "https://iptv-org.github.io/iptv/countries/vn.m3u",
        "🇻🇳",
        "Vietnamese TV channels",
        "~100 channels"
    ),
    Suggestion(
        "Arabic IPTV",
        "https://iptv-org.github.io/iptv/countries/sa.m3u",
        "🌙",
        "Arabic content & entertainment",
        "~200 channels"
    ),
    Suggestion(
        "India IPTV",
        "https://iptv-org.github.io/iptv/countries/in.m3u",
        "🎬",
        "Indian channels & movies",
        "~500 channels"
    ),
)

private val tabLabels = listOf("URL", "File", "Gallery")
private val tabIcons =
    listOf(R.drawable.ic_app_url, R.drawable.ic_app_file, R.drawable.ic_app_video)
private val tabSubtitles = listOf(
    "Paste an M3U link from the internet",
    "Import a local .m3u / .m3u8 file",
    "Add videos from your device gallery"
)

@Composable
fun AddSChanelcreen(
    viewModel: AddItemViewModel,
    statusBarHeight: Dp,
    onBack: () -> Unit,
    onPickFile: () -> Unit,
    onPickGallery: () -> Unit,
    onShowLoading: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val toaster = rememberToaster()
    LaunchedEffect(Unit) {
        viewModel.saveState.collect { state ->
            when (state) {
                is AddItemViewModel.SaveState.Loading -> onShowLoading(true)
                is AddItemViewModel.SaveState.Success -> {
                    onShowLoading(false)
                    toaster.show(R.string.playlist_saved_successfully)
                    onBack()
                }

                is AddItemViewModel.SaveState.Error -> {
                    onShowLoading(false)
                    toaster.show(state.messageResId)
                }
            }
        }
    }

    val selectedFileUri by viewModel.selectedFileUri.collectAsStateWithLifecycle()
    val selectedFileName by viewModel.selectedFileName.collectAsStateWithLifecycle()
    val selectedSuggestionUrl by viewModel.selectedSuggestionUrl.collectAsStateWithLifecycle()
    val galleryMedia by viewModel.galleryMedia.collectAsStateWithLifecycle()

    AddScreenContent(
        statusBarHeight = statusBarHeight,
        selectedFileUri = selectedFileUri,
        selectedFileName = selectedFileName,
        selectedSuggestionUrl = selectedSuggestionUrl,
        galleryMedia = galleryMedia,
        onBack = onBack,
        onPickFile = onPickFile,
        onPickGallery = onPickGallery,
        onClearSelectedFile = { viewModel.clearSelectedFile() },
        onSetSelectedSuggestion = { viewModel.setSelectedSuggestion(it) },
        onRemoveGalleryMedia = { viewModel.removeGalleryMedia(it) },
        onSaveUrlPlaylist = { name, url -> viewModel.saveUrlPlaylist(name, url) },
        onSaveFilePlaylist = { name ->
            if (selectedSuggestionUrl != null) {
                viewModel.saveUrlPlaylist(name, selectedSuggestionUrl!!, SourceType.FILE)
            } else {
                viewModel.saveFilePlaylist(name, context)
            }
        },
        onSaveGalleryPlaylist = { viewModel.saveGalleryPlaylist(it) }
    )
}

// ── Stateless content ─────────────────────────────────────────────────────────

@Composable
fun AddScreenContent(
    statusBarHeight: Dp,
    selectedFileUri: Uri?,
    selectedFileName: String?,
    selectedSuggestionUrl: String?,
    galleryMedia: List<MediaModel?>,
    onBack: () -> Unit,
    onPickFile: () -> Unit,
    onPickGallery: () -> Unit,
    onClearSelectedFile: () -> Unit,
    onSetSelectedSuggestion: (String) -> Unit,
    onRemoveGalleryMedia: (MediaModel) -> Unit,
    onSaveUrlPlaylist: (String, String) -> Unit,
    onSaveFilePlaylist: (String) -> Unit,
    onSaveGalleryPlaylist: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var playlistName by remember { mutableStateOf("") }
    var urlInput by remember { mutableStateOf("") }

    val videoCount = galleryMedia.filterNotNull().size
    val isAddEnabled = playlistName.isNotBlank() && when (selectedTab) {
        0 -> urlInput.isNotBlank()
        1 -> selectedFileUri != null || selectedSuggestionUrl != null
        else -> videoCount > 0
    }


    val missingHint: String? = when {
        isAddEnabled -> null
        playlistName.isBlank() -> stringResource(R.string.enter_a_playlist_name_to_continue)
        selectedTab == 0 && urlInput.isBlank() -> stringResource(R.string.paste_a_url_or_tap_a_suggestion_below)
        selectedTab == 1 && selectedFileUri == null && selectedSuggestionUrl == null ->
            stringResource(R.string.upload_a_file_or_tap_a_suggestion_below)

        selectedTab == 2 && videoCount == 0 -> stringResource(R.string.select_at_least_one_video_from_your_gallery)
        else -> null
    }

    ColumnParentContent(
        modifier = Modifier.backgroundResource(R.drawable.img_bg_app),
        horizontalAlignment = Alignment.Start,
        onBack = onBack
    ) {
        SpaceH(statusBarHeight)


        AddHeader(title = stringResource(R.string.add_channel_to_playlist), onBack = onBack)

        SpaceH(12.Sdp)


        AddTabBar(
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                if (tab != selectedTab) {
                    selectedTab = tab
                    onClearSelectedFile()
                    urlInput = ""
                }
            }
        )

        SpaceH(8.Sdp)


        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(150)) },
            label = "subtitle"
        ) { tab ->
            Text(
                text = tabSubtitles[tab],
                style = headline400(fontSize = 11.Ssp, color = colorSecondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.Sdp),
                textAlign = TextAlign.Center
            )
        }

        SpaceH(12.Sdp)


        AnimatedContent(
            targetState = selectedTab,
            modifier = Modifier.weight(1f),
            transitionSpec = {
                val dir = if (targetState > initialState) 1 else -1
                (slideInHorizontally(tween(260)) { it * dir } + fadeIn(tween(200))) togetherWith
                        (slideOutHorizontally(tween(200)) { -it * dir } + fadeOut(tween(150)))
            },
            label = "tab_content"
        ) { tab ->
            when (tab) {
                0 -> AddUrlTab(
                    playlistName = playlistName,
                    urlInput = urlInput,
                    onPlaylistNameChange = { playlistName = it },
                    onUrlChange = { urlInput = it },
                    onSuggestionClick = { name, url -> playlistName = name; urlInput = url }
                )

                1 -> AddFileTab(
                    playlistName = playlistName,
                    selectedFileName = selectedFileName,
                    selectedSuggestionUrl = selectedSuggestionUrl,
                    onPlaylistNameChange = { playlistName = it },
                    onPickFile = onPickFile,
                    onSuggestionClick = { name, url ->
                        playlistName = name
                        onSetSelectedSuggestion(url)
                    },
                    onClearFile = onClearSelectedFile
                )

                else -> AddGalleryTab(
                    playlistName = playlistName,
                    galleryMedia = galleryMedia,
                    onPlaylistNameChange = { playlistName = it },
                    onPickGallery = onPickGallery,
                    onRemoveMedia = onRemoveGalleryMedia
                )
            }
        }



        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.Sdp)
                .padding(top = 8.Sdp, bottom = 12.Sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddButton(
                label = stringResource(R.string.add_playlist),
                isEnabled = isAddEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (selectedTab) {
                    0 -> onSaveUrlPlaylist(playlistName, urlInput)
                    1 -> onSaveFilePlaylist(playlistName)
                    else -> onSaveGalleryPlaylist(playlistName)
                }
            }

            if (missingHint != null) {
                SpaceH(8)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_app_info),
                        contentDescription = null,
                        tint = IptvTextTertiary,
                        modifier = Modifier.size(13.Sdp)
                    )
                    SpaceW(4)
                    Text(
                        text = missingHint,
                        style = headline400(fontSize = 11.Ssp, color = IptvTextTertiary),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun AddHeader(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.Sdp, vertical = 4.Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.Sdp)
                .clip(CircleShape)
                .background(white.opacity(8f))
                .onClick { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_app_back),
                contentDescription = null,
                tint = IptvTextPrimary,
                modifier = Modifier.size(16.Sdp)
            )
        }

        Spacer(Modifier.width(10.Sdp))

        Text(
            text = title,
            style = headline600(fontSize = 16.Ssp, color = IptvTextPrimary),
            modifier = Modifier.weight(1f)
        )
    }
}

// ── Tab bar ───────────────────────────────────────────────────────────────────

@Composable
private fun AddTabBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.Sdp),
        horizontalArrangement = Arrangement.spacedBy(8.Sdp)
    ) {
        tabLabels.forEachIndexed { index, label ->
            val isSelected = index == selectedTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .neonState(neonEnabled = isSelected)
                    .backgroundState(enabled = isSelected)
                    .onClick { onTabSelected(index) }
                    .padding(vertical = 8.Sdp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(tabIcons[index]),
                        contentDescription = null,
                        tint = white,
                        modifier = Modifier
                            .size(16.Sdp)
                    )
                    SpaceW(5)
                    Text(
                        text = label,
                        style = if (isSelected) {
                            headline600(fontSize = 12.Ssp, color = white)
                        } else {
                            headline400(fontSize = 12.Ssp, color = white.opacity(0.75f))
                        }
                    )
                }
            }
        }
    }
}

// ── URL tab ───────────────────────────────────────────────────────────────────

@Composable
private fun AddUrlTab(
    playlistName: String,
    urlInput: String,
    onPlaylistNameChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onSuggestionClick: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.Sdp)
    ) {

        StepSection(step = "1", label = stringResource(R.string.playlist_name))
        SpaceH(8.Sdp)
        InputFieldWithIcon(
            value = playlistName,
            hint = stringResource(R.string.e_g_my_sports_playlist),
            leadingIcon = R.drawable.ic_app_edit,
            onValueChange = onPlaylistNameChange
        )

        SpaceH(20.Sdp)


        StepSection(step = "2", label = stringResource(R.string.paste_your_m3u_url))
        SpaceH(8.Sdp)
        InputFieldWithIcon(
            value = urlInput,
            hint = "https://example.com/playlist.m3u",
            leadingIcon = R.drawable.ic_app_url,
            onValueChange = onUrlChange,
            keyboardType = KeyboardType.Uri
        )

        SpaceH(12.Sdp)

        TipBox(text = stringResource(R.string.search_public_iptv_m3u_playlist_on_google))

        SpaceH(20.Sdp)

        SectionDivider(label = "Or try a free playlist")
        SpaceH(12.Sdp)

        urlSuggestions.forEachIndexed { i, s ->
            SuggestionCard(
                suggestion = s,
                isSelected = urlInput == s.url,
                onClick = { onSuggestionClick(s.name, s.url) }
            )
            if (i < urlSuggestions.lastIndex) SpaceH(12.Sdp)
        }

        SpaceH(32)
    }
}

// ── File tab ──────────────────────────────────────────────────────────────────

@Composable
private fun AddFileTab(
    playlistName: String,
    selectedFileName: String?,
    selectedSuggestionUrl: String?,
    onPlaylistNameChange: (String) -> Unit,
    onPickFile: () -> Unit,
    onSuggestionClick: (String, String) -> Unit,
    onClearFile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.Sdp)
    ) {

        StepSection(step = "1", label = stringResource(R.string.playlist_name))
        SpaceH(8.Sdp)
        InputFieldWithIcon(
            value = playlistName,
            hint = stringResource(R.string.e_g_my_local_channels),
            leadingIcon = R.drawable.ic_app_edit,
            onValueChange = onPlaylistNameChange
        )

        SpaceH(20.Sdp)


        StepSection(step = "2", label = stringResource(R.string.upload_your_playlist_file))
        SpaceH(8.Sdp)

        FileUploadZone(
            selectedFileName = selectedFileName,
            onPickFile = onPickFile,
            onClearFile = onClearFile
        )

        SpaceH(12.Sdp)

        TipBox(text = stringResource(R.string.download_an_m3u_or_m3u8_file_from))

        SpaceH(20.Sdp)

        SectionDivider(label = stringResource(R.string.or_use_a_sample_playlist))
        SpaceH(12.Sdp)

        fileSuggestions.forEachIndexed { i, s ->
            SuggestionCard(
                suggestion = s,
                isSelected = selectedSuggestionUrl == s.url,
                onClick = { onSuggestionClick(s.name, s.url) }
            )
            if (i < fileSuggestions.lastIndex) SpaceH(12.Sdp)
        }

        SpaceH(32)
    }
}

// ── Gallery tab ───────────────────────────────────────────────────────────────

@Composable
private fun AddGalleryTab(
    playlistName: String,
    galleryMedia: List<MediaModel?>,
    onPlaylistNameChange: (String) -> Unit,
    onPickGallery: () -> Unit,
    onRemoveMedia: (MediaModel) -> Unit
) {
    val videoCount = galleryMedia.filterNotNull().size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.Sdp)
    ) {

        StepSection(step = "1", label = stringResource(R.string.playlist_name))
        SpaceH(8.Sdp)
        InputFieldWithIcon(
            value = playlistName,
            hint = "e.g. My Home Videos",
            leadingIcon = R.drawable.ic_app_edit,
            onValueChange = onPlaylistNameChange
        )

        SpaceH(20.Sdp)


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepSection(step = "2", label = stringResource(R.string.select_your_videos))
            if (videoCount > 0) {
                SpaceW(8)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(IptvSuccess.copy(alpha = 0.18f))
                        .padding(horizontal = 10.Sdp, vertical = 3.Sdp)
                ) {
                    Text(
                        text = stringResource(R.string.selected_video, videoCount),
                        style = headline600(fontSize = 10.Ssp, color = IptvSuccess)
                    )
                }
            }
        }

        SpaceH(10.Sdp)

        TipBox(
            text = stringResource(R.string.tap_the_card_to_open_your_gallery_and_pick_multiple_videos_at_once),
            color = IptvWarning
        )

        SpaceH(12.Sdp)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.Sdp),
            horizontalArrangement = Arrangement.spacedBy(8.Sdp),
            contentPadding = PaddingValues(bottom = 16.Sdp)
        ) {
            itemsIndexed(galleryMedia) { _, item ->
                if (item == null) GalleryAddButton(onPickGallery)
                else GalleryVideoItem(item, onRemoveMedia)
            }
        }
    }
}

// ── Reusable section components ───────────────────────────────────────────────

@Composable
private fun StepSection(step: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(20.Sdp)
                .clip(CircleShape)
                .background(IptvAccent)
                .border(1.dp, white, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step,
                style = headline600(fontSize = 12.Ssp, color = white)
            )
        }
        SpaceW(8)
        Text(
            text = label,
            style = headline500(fontSize = 12.Ssp, color = IptvTextPrimary),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TipBox(
    text: String,
    color: Color = IptvAccent
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.Sdp))
            .background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(10.Sdp))
            .padding(horizontal = 10.Sdp, vertical = 8.Sdp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "💡", fontSize = 13.Ssp)
        SpaceW(8)
        Text(
            text = text,
            style = headline400(fontSize = 10.Ssp, color = color.copy(alpha = 0.85f)),
            lineHeight = 16.Ssp
        )
    }
}

@Composable
private fun SectionDivider(label: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(IptvDivider)
        )
        Text(
            text = "  $label  ",
            style = headline600(fontSize = 11.Ssp, color = IptvTextTertiary)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(IptvDivider)
        )
    }
}

@Composable
private fun SuggestionCard(
    suggestion: Suggestion,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.Sdp))
            .background(if (isSelected) IptvAccent.copy(alpha = 0.12f) else IptvSurface)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) IptvAccent else IptvDivider,
                shape = RoundedCornerShape(12.Sdp)
            )
            .onClick { onClick() }
            .padding(10.Sdp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.Sdp)
                    .clip(RoundedCornerShape(10.Sdp))
                    .background(
                        if (isSelected) IptvAccent.copy(alpha = 0.2f)
                        else IptvBgSecondary
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = suggestion.emoji, fontSize = 20.Ssp)
            }

            SpaceW(12)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = suggestion.name,
                    style = headline500(
                        fontSize = 12.Ssp,
                        color = if (isSelected) IptvAccent else IptvTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(1f)
                )
                SpaceH(2)
                Text(
                    text = suggestion.channelHint,
                    style = headline600(
                        fontSize = 10.Ssp,
                        color = if (isSelected) {
                            IptvAccent.copy(alpha = 0.7f)
                        } else {
                            IptvTextTertiary
                        }
                    ),
                    modifier = Modifier.fillMaxWidth(1f)
                )
            }
            SpaceW(8)
            Box(
                modifier = Modifier
                    .size(20.Sdp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) IptvAccent
                        else white.opacity(0.25f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        if (isSelected) {
                            R.drawable.ic_app_check
                        } else {
                            R.drawable.ic_arrow_right
                        }
                    ),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.Sdp).padding(2.Sdp)
                )
            }
        }
    }
}

// ── File upload zone ──────────────────────────────────────────────────────────

@Composable
private fun FileUploadZone(
    selectedFileName: String?,
    onPickFile: () -> Unit,
    onClearFile: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (selectedFileName != null) 70.Sdp else 110.Sdp)
            .clip(RoundedCornerShape(12.Sdp))
            .background(IptvSurface)
            .dashedBorder(
                1.5.dp,
                if (selectedFileName != null) IptvSuccess else IptvDivider,
                12.Sdp
            )
            .onClick(selectedFileName == null) { onPickFile() },
        contentAlignment = Alignment.Center
    ) {
        if (selectedFileName != null) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.Sdp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.Sdp)
                        .clip(RoundedCornerShape(8.Sdp))
                        .background(IptvSuccess.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_app_video),
                        contentDescription = null,
                        tint = IptvSuccess,
                        modifier = Modifier.size(20.Sdp)
                    )
                }
                SpaceW(12)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = selectedFileName,
                        style = headline600(fontSize = 13.Ssp, color = IptvTextPrimary),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(R.string.ready_to_import),
                        style = headline400(fontSize = 10.Ssp, color = IptvSuccess)
                    )
                }
                SpaceW(8)
                Box(
                    modifier = Modifier
                        .size(28.Sdp)
                        .clip(CircleShape)
                        .background(IptvError.copy(alpha = 0.12f))
                        .onClick { onClearFile() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = null,
                        tint = IptvError,
                        modifier = Modifier.size(14.Sdp)
                    )
                }
            }
        } else {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(44.Sdp)
                        .clip(RoundedCornerShape(12.Sdp))
                        .background(IptvAccent.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_app_storage),
                        contentDescription = null,
                        tint = IptvAccent,
                        modifier = Modifier.size(24.Sdp)
                    )
                }
                SpaceH(10)
                Text(
                    text = stringResource(R.string.click_to_upload_file),
                    style = headline600(fontSize = 13.Ssp, color = IptvTextPrimary)
                )
                SpaceH(3)
                Text(
                    text = stringResource(R.string.supports_m3u_and_m3u8_formats),
                    style = headline400(fontSize = 10.Ssp, color = IptvTextTertiary)
                )
            }
        }
    }
}

// ── Gallery grid cells ────────────────────────────────────────────────────────

@Composable
private fun GalleryAddButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.Sdp))
            .background(IptvSurface)
            .dashedBorder(1.5.dp, IptvAccent.copy(alpha = 0.5f), 10.Sdp)
            .onClick { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(34.Sdp)
                    .clip(CircleShape)
                    .background(IptvAccent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = null,
                    tint = IptvAccent,
                    modifier = Modifier.size(20.Sdp)
                )
            }
            SpaceH(5)
            Text(
                text = stringResource(R.string.add_videos),
                style = headline400(fontSize = 9.Ssp, color = colorSecondary),
                textAlign = TextAlign.Center,
                lineHeight = 13.Ssp
            )
        }
    }
}

@Composable
private fun GalleryVideoItem(item: MediaModel, onRemove: (MediaModel) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.Sdp))
            .background(IptvBgSecondary)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(IptvSurface, IptvBgSecondary))),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_app_play),
                contentDescription = null,
                tint = colorSecondary.copy(alpha = 0.4f),
                modifier = Modifier.size(22.Sdp)
            )
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
                .padding(horizontal = 6.Sdp, vertical = 5.Sdp)
        ) {
            Text(
                text = item.name,
                style = headline400(fontSize = 9.Ssp, color = IptvTextPrimary),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }


        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.Sdp)
                .size(20.Sdp)
                .clip(CircleShape)
                .background(IptvError.copy(alpha = 0.9f))
                .onClick { onRemove(item) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(11.Sdp)
            )
        }
    }
}

// ── Input field ───────────────────────────────────────────────────────────────

@Composable
private fun InputFieldWithIcon(
    value: String,
    hint: String,
    leadingIcon: Int,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.Sdp)
            .clip(RoundedCornerShape(12.Sdp))
            .background(white.opacity(7f))
            .border(
                1.dp,
                if (value.isNotEmpty()) IptvAccent.copy(alpha = 0.5f) else IptvDivider,
                RoundedCornerShape(12.Sdp)
            )
            .padding(horizontal = 12.Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(leadingIcon),
            contentDescription = null,
            tint = if (value.isNotEmpty()) IptvAccent else IptvTextTertiary,
            modifier = Modifier.size(16.Sdp)
        )

        SpaceW(10)

        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    style = headline400(fontSize = 13.Ssp, color = IptvTextTertiary)
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = headline400(fontSize = 13.Ssp, color = IptvTextPrimary),
                cursorBrush = SolidColor(IptvAccent),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Done
                )
            )
        }

        if (value.isNotEmpty()) {
            SpaceW(8)
            Box(
                modifier = Modifier
                    .size(18.Sdp)
                    .clip(CircleShape)
                    .background(IptvTextTertiary.copy(alpha = 0.3f))
                    .onClick { onValueChange("") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = null,
                    tint = colorSecondary,
                    modifier = Modifier.size(11.Sdp)
                )
            }
        }
    }
}

// ── CTA button ────────────────────────────────────────────────────────────────

@Composable
private fun AddButton(
    label: String,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AppNeonButton(
        text = label,
        onClick = onClick,
        enabled = isEnabled,
        textStyle = headline600(fontSize = 13.Ssp, color = white),
        modifier = modifier.height(42.Sdp)
    )
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0D0D1A)
@Composable
private fun AddScreenPreview() {
    AppViewTheme {
        AddScreenContent(
            statusBarHeight = 24.dp,
            selectedFileUri = null,
            selectedFileName = null,
            selectedSuggestionUrl = null,
            galleryMedia = listOf(
                null,
                MediaModel(1, "Holiday Trip", 120_000, "/path/1", 10 * 1024 * 1024),
                MediaModel(2, "Birthday Party", 300_000, "/path/2", 25 * 1024 * 1024)
            ),
            onBack = {},
            onPickFile = {},
            onPickGallery = {},
            onClearSelectedFile = {},
            onSetSelectedSuggestion = {},
            onRemoveGalleryMedia = {},
            onSaveUrlPlaylist = { _, _ -> },
            onSaveFilePlaylist = {},
            onSaveGalleryPlaylist = {}
        )
    }
}
