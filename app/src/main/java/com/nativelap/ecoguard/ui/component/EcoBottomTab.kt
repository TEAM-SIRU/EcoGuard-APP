package com.nativelap.ecoguard.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nativelap.ecoguard.R

enum class EcoBottomTab(
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val selectedIconRes: Int,
    @param:DrawableRes val unselectedIconRes: Int,
) {
    HOME(
        labelRes = R.string.nav_home,
        selectedIconRes = R.drawable.ic_home,
        unselectedIconRes = R.drawable.ic_home_inactive,
    ),
    AREA(
        labelRes = R.string.nav_area,
        selectedIconRes = R.drawable.ic_map_active,
        unselectedIconRes = R.drawable.ic_map,
    ),
    ACTIVITY(
        labelRes = R.string.nav_activity,
        selectedIconRes = R.drawable.ic_list_active,
        unselectedIconRes = R.drawable.ic_list,
    ),
    MY_PAGE(
        labelRes = R.string.nav_my_page,
        selectedIconRes = R.drawable.ic_profile_active,
        unselectedIconRes = R.drawable.ic_profile,
    ),
}
