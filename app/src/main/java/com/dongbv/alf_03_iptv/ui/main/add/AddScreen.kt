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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.ui.component.AppNeonButton
import com.dongbv.alf_03_iptv.viewmodel.AddItemViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgSecondary
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvError
import com.dongbv.alf_03_iptv.widget.IptvSuccess
import com.dongbv.alf_03_iptv.widget.IptvSurface
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextSecondary
import com.dongbv.alf_03_iptv.widget.IptvTextTertiary
import com.dongbv.alf_03_iptv.widget.IptvWarning
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
    Suggestion("Family TV",   "https://iptv-org.github.io/iptv/categories/family.m3u",  "👨‍👩‍👧", "Family-friendly content for all ages",       "~200 channels"),
    Suggestion("Sports 24/7", "https://iptv-org.github.io/iptv/categories/sports.m3u",  "⚽",      "Live sports, matches & events worldwide",    "~300 channels"),
    Suggestion("World News",  "https://iptv-org.github.io/iptv/categories/news.m3u",    "📰",      "International news in many languages",        "~500 channels"),
)

private val fileSuggestions = listOf(
    Suggestion("Vietnam IPTV", "https://iptv-org.github.io/iptv/countries/vn.m3u", "🇻🇳", "Vietnamese TV channels",         "~100 channels"),
    Suggestion("Arabic IPTV",  "https://iptv-org.github.io/iptv/countries/sa.m3u", "🌙",  "Arabic content & entertainment", "~200 channels"),
    Suggestion("India IPTV",   "https://iptv-org.github.io/iptv/countries/in.m3u", "🎬",  "Indian channels & movies",       "~500 channels"),
)

private val tabLabels    = listOf("URL",    "File",           "Gallery")
private val tabIcons     = listOf(R.drawable.ic_app_url, R.drawable.ic_app_file, R.drawable.ic_app_storage)
private val tabSubtitles = listOf(
    "Paste an M3U link from the internet",
    "Import a local .m3u / .m3u8 file",
    "Add videos from your device gallery"
)

// ── Root composable (with ViewModel) ─────────────────────────────────────────

@Composable
fun AddScreen(
    viewModel: AddItemViewModel,
    statusBarHeight: Dp,
    onBack: () -> Unit,
    onPickFile: () -> Unit,
    onPickGallery: () -> Unit,
    onShowLoading: (Boolean) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.saveState.collect { state ->
            when (state) {
                is AddItemViewModel.SaveState.Loading -> onShowLoading(true)
                is AddItemViewModel.SaveState.Success -> {
                    onShowLoading(false)
                    Toast.makeText(context, context.getString(R.string.playlist_saved_successfully), Toast.LENGTH_SHORT).show()
                    onBack()
                }
                is AddItemViewModel.SaveState.Error -> {
                    onShowLoading(false)
                    Toast.makeText(context, context.getString(state.messageResId), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val selectedFileUri     by viewModel.selectedFileUri.collectAsStateWithLifecycle()
    val selectedFileName    by viewModel.selectedFileName.collectAsStateWithLifecycle()
    val selectedSuggestionUrl by viewModel.selectedSuggestionUrl.collectAsStateWithLifecycle()
    val galleryMedia        by viewModel.galleryMedia.collectAsStateWithLifecycle()

    AddScreenContent(
        statusBarHeight       = statusBarHeight,
        selectedFileUri       = selectedFileUri,
        selectedFileName      = selectedFileName,
        selectedSuggestionUrl = selectedSuggestionUrl,
        galleryMedia          = galleryMedia,
        onBack                = onBack,
        onPickFile            = onPickFile,
        onPickGallery         = onPickGallery,
        onClearSelectedFile   = { viewModel.clearSelectedFile() },
        onSetSelectedSuggestion = { viewModel.setSelectedSuggestion(it) },
        onRemoveGalleryMedia  = { viewModel.removeGalleryMedia(it) },
        onSaveUrlPlaylist     = { name, url -> viewModel.saveUrlPlaylist(name, url) },
        onSaveFilePlaylist    = { name ->
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
    var selectedTab  by remember { mutableIntStateOf(0) }
    var playlistName by remember { mutableStateOf("") }
    var urlInput     by remember { mutableStateOf("") }

    val videoCount  = galleryMedia.filterNotNull().size
    val isAddEnabled = playlistName.isNotBlank() && when (selectedTab) {
        0    -> urlInput.isNotBlank()
        1    -> selectedFileUri != null || selectedSuggestionUrl != null
        else -> videoCount > 0
    }

   
    val missingHint: String? = when {
        isAddEnabled -> null
        playlistName.isBlank() -> "Enter a playlist name to continue"
        selectedTab == 0 && urlInput.isBlank() -> "Paste a URL or tap a suggestion below"
        selectedTab == 1 && selectedFileUri == null && selectedSuggestionUrl == null ->
            "Upload a file or tap a suggestion below"
        selectedTab == 2 && videoCount == 0 -> "Select at least one video from your gallery"
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
                fontSize = 11.Ssp,
                color = IptvTextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
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
                    onSuggestionClick = { name, url -> playlistName = name; onSetSelectedSuggestion(url) },
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
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddButton(
                label = stringResource(R.string.add_playlist),
                isEnabled = isAddEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (selectedTab) {
                    0    -> onSaveUrlPlaylist(playlistName, urlInput)
                    1    -> onSaveFilePlaylist(playlistName)
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
                        fontSize = 11.Ssp,
                        color = IptvTextTertiary,
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
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.Sdp)
                .clip(CircleShape)
                .background(IptvSurface)
                .onClick { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_app_back),
                contentDescription = null,
                tint = IptvTextPrimary,
                modifier = Modifier.size(18.Sdp)
            )
        }

        Text(
            text = title,
            fontSize = 15.Ssp,
            fontWeight = FontWeight.SemiBold,
            color = IptvTextPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        Box(Modifier.size(38.Sdp))
    }
}

// ── Tab bar ───────────────────────────────────────────────────────────────────

@Composable
private fun AddTabBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.Sdp))
            .background(IptvSurface)
            .padding(4.Sdp),
        horizontalArrangement = Arrangement.spacedBy(4.Sdp)
    ) {
        tabLabels.forEachIndexed { index, label ->
            val isSelected = index == selectedTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.Sdp))
                    .background(
                        if (isSelected)
                            Brush.horizontalGradient(listOf(IptvAccent, Color(0xFFFF6B6B)))
                        else
                            Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
                    )
                    .onClick { onTabSelected(index) }
                    .padding(vertical = 10.Sdp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(tabIcons[index]),
                        contentDescription = null,
                        tint = if (isSelected) Color.White else IptvTextTertiary,
                        modifier = Modifier.size(13.Sdp)
                    )
                    SpaceW(5)
                    Text(
                        text = label,
                        fontSize = 12.Ssp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else IptvTextSecondary
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
            .padding(horizontal = 16.dp)
    ) {
       
        StepSection(step = "1", label = stringResource(R.string.playlist_name))
        SpaceH(8.Sdp)
        InputFieldWithIcon(
            value = playlistName,
            hint = "e.g. My Sports Playlist",
            leadingIcon = R.drawable.ic_app_edit,
            onValueChange = onPlaylistNameChange
        )

        SpaceH(20.Sdp)

       
        StepSection(step = "2", label = "Paste your M3U URL")
        SpaceH(8.Sdp)
        InputFieldWithIcon(
            value = urlInput,
            hint = "https://example.com/playlist.m3u",
            leadingIcon = R.drawable.ic_app_url,
            onValueChange = onUrlChange,
            keyboardType = KeyboardType.Uri
        )

        SpaceH(12.Sdp)

        TipBox(text = "Search “public iptv m3u playlist” on Google to find free channel lists.")

        SpaceH(20.Sdp)

        SectionDivider(label = "Or try a free playlist")
        SpaceH(12.Sdp)

        urlSuggestions.forEachIndexed { i, s ->
            SuggestionCard(
                suggestion = s,
                isSelected = urlInput == s.url,
                onClick = { onSuggestionClick(s.name, s.url) }
            )
            if (i < urlSuggestions.lastIndex) SpaceH(10.Sdp)
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
            .padding(horizontal = 16.dp)
    ) {
       
        StepSection(step = "1", label = stringResource(R.string.playlist_name))
        SpaceH(8.Sdp)
        InputFieldWithIcon(
            value = playlistName,
            hint = "e.g. My Local Channels",
            leadingIcon = R.drawable.ic_app_edit,
            onValueChange = onPlaylistNameChange
        )

        SpaceH(20.Sdp)

       
        StepSection(step = "2", label = "Upload your playlist file")
        SpaceH(8.Sdp)

        FileUploadZone(
            selectedFileName = selectedFileName,
            onPickFile = onPickFile,
            onClearFile = onClearFile
        )

        SpaceH(12.Sdp)

        TipBox(text = "Download an .m3u or .m3u8 file from your IPTV provider, then upload it here.")

        SpaceH(20.Sdp)

        SectionDivider(label = "Or use a sample playlist")
        SpaceH(12.Sdp)

        fileSuggestions.forEachIndexed { i, s ->
            SuggestionCard(
                suggestion = s,
                isSelected = selectedSuggestionUrl == s.url,
                onClick = { onSuggestionClick(s.name, s.url) }
            )
            if (i < fileSuggestions.lastIndex) SpaceH(10.Sdp)
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
            .padding(horizontal = 16.dp)
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
            StepSection(step = "2", label = "Select your videos")
            if (videoCount > 0) {
                SpaceW(8)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(IptvSuccess.copy(alpha = 0.18f))
                        .padding(horizontal = 10.Sdp, vertical = 3.Sdp)
                ) {
                    Text(
                        text = "$videoCount selected",
                        fontSize = 10.Ssp,
                        color = IptvSuccess,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        SpaceH(10.Sdp)

        TipBox(
            text = "Tap the + card to open your gallery and pick multiple videos at once.",
            color = IptvWarning
        )

        SpaceH(12.Sdp)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.Sdp),
            horizontalArrangement = Arrangement.spacedBy(8.Sdp),
            contentPadding = PaddingValues(bottom = 16.dp)
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
                .background(Brush.horizontalGradient(listOf(IptvAccent, Color(0xFFFF6B6B)))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step,
                fontSize = 10.Ssp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
        SpaceW(8)
        Text(
            text = label,
            fontSize = 13.Ssp,
            fontWeight = FontWeight.SemiBold,
            color = IptvTextPrimary
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
            .padding(horizontal = 12.Sdp, vertical = 10.Sdp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "💡", fontSize = 13.Ssp)
        SpaceW(8)
        Text(
            text = text,
            fontSize = 11.Ssp,
            color = color.copy(alpha = 0.85f),
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
            fontSize = 11.Ssp,
            color = IptvTextTertiary,
            fontWeight = FontWeight.Medium
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
            .padding(12.Sdp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
           
            Box(
                modifier = Modifier
                    .size(42.Sdp)
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
                    fontSize = 13.Ssp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) IptvAccent else IptvTextPrimary
                )
                SpaceH(2)
                Text(
                    text = suggestion.description,
                    fontSize = 11.Ssp,
                    color = IptvTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                SpaceH(4)
                Text(
                    text = suggestion.channelHint,
                    fontSize = 10.Ssp,
                    color = if (isSelected) IptvAccent.copy(alpha = 0.7f) else IptvTextTertiary,
                    fontWeight = FontWeight.Medium
                )
            }

            SpaceW(8)

           
            Box(
                modifier = Modifier
                    .size(26.Sdp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) IptvAccent
                        else IptvBgSecondary
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        painter = painterResource(R.drawable.ic_app_check),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.Sdp)
                    )
                } else {
                    Text(text = "›", fontSize = 16.Ssp, color = IptvTextTertiary)
                }
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
            .clip(RoundedCornerShape(14.Sdp))
            .background(IptvSurface)
            .dashedBorder(
                1.5.dp,
                if (selectedFileName != null) IptvSuccess else IptvDivider,
                14.Sdp
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
                        painter = painterResource(R.drawable.ic_app_file),
                        contentDescription = null,
                        tint = IptvSuccess,
                        modifier = Modifier.size(20.Sdp)
                    )
                }
                SpaceW(12)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = selectedFileName,
                        fontSize = 13.Ssp,
                        color = IptvTextPrimary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Ready to import",
                        fontSize = 10.Ssp,
                        color = IptvSuccess
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
                    fontSize = 13.Ssp,
                    color = IptvTextPrimary,
                    fontWeight = FontWeight.Medium
                )
                SpaceH(3)
                Text(
                    text = "Supports  .m3u  and  .m3u8  formats",
                    fontSize = 10.Ssp,
                    color = IptvTextTertiary
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
                text = "Add\nvideos",
                fontSize = 9.Ssp,
                color = IptvTextSecondary,
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
                tint = IptvTextSecondary.copy(alpha = 0.4f),
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
                .padding(horizontal = 6.dp, vertical = 5.dp)
        ) {
            Text(
                text = item.name,
                fontSize = 9.Ssp,
                color = IptvTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

       
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
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
            .clip(RoundedCornerShape(12.Sdp))
            .background(IptvSurface)
            .border(1.dp, IptvDivider, RoundedCornerShape(12.Sdp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
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
                Text(text = hint, fontSize = 13.Ssp, color = IptvTextTertiary)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = IptvTextPrimary, fontSize = 13.Ssp),
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
                    tint = IptvTextSecondary,
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
        textStyle = TextStyle(fontSize = 14.Ssp, fontWeight = FontWeight.Bold),
        modifier = modifier.height(50.Sdp)
    )
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0D0D1A)
@Composable
private fun AddScreenPreview() {
    AppViewTheme {
        AddScreenContent(
            statusBarHeight       = 24.dp,
            selectedFileUri       = null,
            selectedFileName      = null,
            selectedSuggestionUrl = null,
            galleryMedia          = listOf(
                null,
                MediaModel(1, "Holiday Trip", 120_000, "/path/1", 10 * 1024 * 1024),
                MediaModel(2, "Birthday Party", 300_000, "/path/2", 25 * 1024 * 1024)
            ),
            onBack                = {},
            onPickFile            = {},
            onPickGallery         = {},
            onClearSelectedFile   = {},
            onSetSelectedSuggestion = {},
            onRemoveGalleryMedia  = {},
            onSaveUrlPlaylist     = { _, _ -> },
            onSaveFilePlaylist    = {},
            onSaveGalleryPlaylist = {}
        )
    }
}
