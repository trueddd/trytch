package com.github.trueddd.trytch.ui.screens.stream.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.trueddd.trytch.R
import com.github.trueddd.trytch.ui.theme.AppTheme
import com.github.trueddd.twitch.data.User

data object About : PageTab(R.string.streamer_screen_about_tab)

@Preview
@Composable
fun AboutContent(user: User = User.test()) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(AppTheme.SecondaryText, RoundedCornerShape(8.dp))
    ) {
        Text(
            text = user.description,
            color = AppTheme.PrimaryText,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}
