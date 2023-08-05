package com.github.trueddd.trytch.ui.screens.stream.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.github.trueddd.trytch.navigation.AppBackStack
import com.github.trueddd.trytch.ui.screens.profile.UserProfile
import com.github.trueddd.trytch.ui.theme.AppTheme

class StreamerPageScreen(
    private val streamerPageViewModel: StreamerPageViewModel,
    private val appBackStack: AppBackStack,
    buildContext: BuildContext,
) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        val state by streamerPageViewModel.stateFlow.collectAsState()
        StreamerPage(
            state = state,
            onBackClicked = { appBackStack.pop() },
        )
    }
}

@Preview
@Composable
private fun StreamerPage(
    state: StreamerPageState = StreamerPageState.test(),
    onBackClicked: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Primary)
    ) {
        UserProfile(
            user = state.streamerUser,
            onBackButtonClicked = onBackClicked,
        )
        var selectedTab by remember { mutableStateOf(0) }
        val tabs = remember {
            listOf(
                About(state.streamerUser),
                Clips(state.streamerUser),
                Videos(state.streamerUser),
            )
        }
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = AppTheme.Primary,
            divider = {
                Divider(color = AppTheme.PrimaryText)
            },
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = AppTheme.Accent
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, pageTab ->
                ContentTab(index = index, selectedIndex = selectedTab, pageTab = pageTab) {
                    selectedTab = index
                }
            }
        }
        tabs[selectedTab].Content()
    }
}

@Composable
private fun ContentTab(
    index: Int,
    selectedIndex: Int,
    pageTab: PageTab,
    onSelected: () -> Unit
) {
    Tab(
        selected = selectedIndex == index,
        onClick = onSelected,
        unselectedContentColor = AppTheme.PrimaryText,
        selectedContentColor = AppTheme.Accent,
        text = {
            Text(text = stringResource(pageTab.nameResId))
        }
    )
}
