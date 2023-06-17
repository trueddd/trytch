package com.github.trueddd.truetripletwitch.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.bumble.appyx.core.integrationpoint.ActivityIntegrationPoint
import com.bumble.appyx.core.integrationpoint.IntegrationPointProvider

open class NodeActivity : FragmentActivity(), IntegrationPointProvider {

    override lateinit var appyxIntegrationPoint: ActivityIntegrationPoint
        protected set

    protected open fun createIntegrationPoint(savedInstanceState: Bundle?) =
        ActivityIntegrationPoint(
            activity = this,
            savedInstanceState = savedInstanceState
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appyxIntegrationPoint = createIntegrationPoint(savedInstanceState)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        appyxIntegrationPoint.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        appyxIntegrationPoint.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        appyxIntegrationPoint.onSaveInstanceState(outState)
    }
}
