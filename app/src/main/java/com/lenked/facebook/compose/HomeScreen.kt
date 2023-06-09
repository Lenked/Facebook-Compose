package com.lenked.facebook.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lenked.facebook.compose.ui.theme.ButtonGray
import com.lenked.facebook.compose.ui.theme.Facebook_composeTheme

@Composable
fun HomeScreen(
    navigateToSignIn: () -> Unit,
){
    val viewModel = viewModel<HomeScreenViewModel>()
    val state by viewModel.state.collectAsState()
    when (state){
        is HomeScreenState.Loaded -> HomeScreenContents()
        HomeScreenState.Loading -> LoadingScreen()
        HomeScreenState.SignInRequired -> LaunchedEffect(Unit) {
            navigateToSignIn()
        }
    }
    HomeScreenContents()
}

@Composable
fun LoadingScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HomeScreenContents() {
    Box(
        Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        LazyColumn(contentPadding = PaddingValues(bottom = 40.dp)) {
            item {
                TopAppBar()
            }
            item {
                TabBar()
            }
            item {
                StatusUpdateBar(avatarUrl = "https://lh3.googleusercontent.com/a/AGNmyxa2XIHwYHufIw3N8qo7gjBNScP8I3eLqMubaIx8=s432",
                    onTextChange = {
                        // TODO()
                    },
                    onSendClick = {
                        // TODO ()
                    }
                )
            }
        }
    }
}


@Composable
private fun TopAppBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .clip(CircleShape)
                .background(ButtonGray)
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(R.string.search)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .clip(CircleShape)
                .background(ButtonGray)
        ) {
            Icon(
                imageVector = Icons.Rounded.ChatBubble,
                contentDescription = stringResource(R.string.search)
            )
        }
    }
}

data class TabItem(
    val icon: ImageVector,
    val contentDescription: String,
)

@Composable
fun TabBar() {
    Surface {
        var tabIndex by remember {
            mutableStateOf(0)
        }
        TabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.primary,
        ) {
            val tabs = listOf(
                TabItem(Icons.Rounded.Home, stringResource(R.string.home)),
                TabItem(Icons.Rounded.Tv, stringResource(R.string.reels)),
                TabItem(Icons.Rounded.Store, stringResource(R.string.marketplace)),
                TabItem(Icons.Rounded.Newspaper, stringResource(R.string.news)),
                TabItem(Icons.Rounded.Notifications, stringResource(R.string.notifications)),
                TabItem(Icons.Rounded.Menu, stringResource(R.string.menu)),
            )

            tabs.forEachIndexed { i, item ->
                Tab(selected = tabIndex == i,
                    onClick = { tabIndex = i },
                    modifier = Modifier.heightIn(48.dp)) {
                    Icon(item.icon,
                        contentDescription = item.contentDescription,
                        tint = if (i == tabIndex) {
                            MaterialTheme.colors.primary
                        } else {
                            MaterialTheme.colors.onSurface.copy(alpha = 0.44f)
                        })
                }
            }

        }
    }
}


@Composable
fun StatusUpdateBar(
    avatarUrl: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
) {
    Surface {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(avatarUrl)
                    .crossfade(true).placeholder(R.drawable.ic_placeholder).build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape))
                var text by remember {
                    mutableStateOf("")
                }
                TextField(colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent),
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = {
                        text = it
                        onTextChange(it)
                    },
                    placeholder = {
                        Text(stringResource(R.string.whats_on_your_mind))
                    },
                    keyboardActions = KeyboardActions(onSend = {
                        onSendClick()
                        text = ""
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)
                )
            }
            Divider(thickness = Dp.Hairline)
            Row(Modifier.fillMaxWidth()) {
                StatusAction(Icons.Rounded.VideoCall,
                    stringResource(R.string.live),
                    modifier = Modifier.weight(1f))
                VerticalDivider(Modifier.height(48.dp), thickness = Dp.Hairline)
                StatusAction(Icons.Rounded.PhotoAlbum,
                    stringResource(R.string.photo),
                    modifier = Modifier.weight(1f))
                VerticalDivider(Modifier.height(48.dp), thickness = Dp.Hairline)
                StatusAction(Icons.Rounded.ChatBubble,
                    stringResource(R.string.discuss),
                    modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
    thickness: Dp = 1.dp,
    topIndent: Dp = 0.dp,
) {
    val indentMod = if (topIndent.value != 0f) {
        Modifier.padding(top = topIndent)
    } else {
        Modifier
    }
    val targetThickness = if (thickness == Dp.Hairline) {
        (1f / LocalDensity.current.density).dp
    } else {
        thickness
    }
    // TODO see why this does not work without specifying height()
    Box(
        modifier
            .then(indentMod)
            .fillMaxHeight()
            .width(targetThickness)
            .background(color = color))
}


@Composable
private fun StatusAction(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    TextButton(modifier = modifier,
        onClick = { },
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onSurface)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = text)
            Spacer(Modifier.width(8.dp))
            Text(text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultHomePreview() {
    Facebook_composeTheme {
        HomeScreen(
            navigateToSignIn = {

            }
        )
    }
}