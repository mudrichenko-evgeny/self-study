package com.mudrichenkoevgeny.selfstudy.ui.screen.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.databinding.ActivityMainBinding
import com.mudrichenkoevgeny.selfstudy.ui.base.activity.BaseActivity
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBar
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarParentType
import com.mudrichenkoevgeny.selfstudy.ui.screen.main.model.MainActivityNavigationData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val layoutResId = R.layout.activity_main

    override val viewModel: MainActivityViewModel by viewModels()

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        viewModel.appInitializationFinishedEvent.observeData {
            initBottomNavigation()
        }
        viewModel.resetQuizEvent.observeSingleLiveEvent {
            onResetQuizEventReceived()
        }
        lifecycleScope.launchWhenCreated {
            Log.d("LIFE_LOGS", "MainActivity: launchWhenCreated")
            viewModel.observeToResetQuizFlow()
        }
    }

    private fun onResetQuizEventReceived() {
        Log.d("LIFE_LOGS", "MainActivity: onResetQuizEventReceived")
        navController?.let { navController ->
            navController.findDestination(R.id.nav_graph_home)?.let { homeDestination ->
                navController.currentDestination?.let { currentDestination ->
                    if (currentDestination.parent == homeDestination) {
                        resetHomeGraph(navController)
                    } else {
                        viewModel.resetHomeGraph = true
                    }
                }
            }
        }
    }

    private fun initBottomNavigation() {
        navController = binding.navHostFragment.getFragment<NavHostFragment>().navController
        navController?.let { navController ->
            val navView = binding.bottomNavigation
            navView.setupWithNavController(navController)
            navView.setOnItemReselectedListener {
                navController.currentDestination?.parent?.startDestinationId?.let { rootDestinationId ->
                    if (rootDestinationId != navController.currentDestination?.id) {
                        val navOptions = NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setPopUpTo(rootDestinationId, true)
                            .build()
                        navController.navigate(rootDestinationId, null, navOptions)
                    }
                }
            }
            navController.addOnDestinationChangedListener { controller, destination, _ ->
                Log.d("LIFE_LOGS", "MainActivity: addOnDestinationChangedListener resetHomeGraph = ${viewModel.resetHomeGraph}")
                if (destination.parent?.id == R.id.nav_graph_home && viewModel.resetHomeGraph) {
                    viewModel.resetHomeGraph = false
                    resetHomeGraph(controller)
                }
            }
        }
    }

    fun showInfoSnackBar(infoSnackBarModel: InfoSnackBarModel) {
        InfoSnackBar.make(
            containerView = binding.overlayContainer,
            infoSnackBarModel = infoSnackBarModel,
            infoSnackBarParentType = InfoSnackBarParentType.ACTIVITY
        ).apply {
            show()
        }
    }

    fun handleMainActivityNavigationData(
        handleMainActivityNavigationData: MainActivityNavigationData
    ) {
        binding.bottomNavigation.selectedItemId =
            handleMainActivityNavigationData.bottomNavigationGraph.getGraphId()
        if (navController?.currentDestination?.id != handleMainActivityNavigationData.destinationId) {
            navController?.navigate(handleMainActivityNavigationData.destinationId)
        }
    }

    fun changeBottomMenuVisibility(visible: Boolean) {
        binding.bottomNavigation.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun resetHomeGraph(navController: NavController) {
        Log.d("LIFE_LOGS", "MainActivity: resetHomeGraph")
        navController.popBackStack(
            destinationId = R.id.homeFragment,
            inclusive = false,
            saveState = false
        )
    }

}