package com.hexakill.medstime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class HeaderManager {

    public static void setupHeader(Activity activity) {
        ImageButton menuButton = activity.findViewById(R.id.menuButton);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> showMenu(activity, v));
        }
    }

    private static void showMenu(Activity activity, View anchor) {
        Context wrapper = new ContextThemeWrapper(activity, R.style.AppPopupMenuStyle);

        PopupMenu popupMenu = new PopupMenu(wrapper, anchor);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popupMenu.getMenu());

        Class<?> current = activity.getClass();

        // Removed menu_medicines check
        if (current == SettingsActivity.class) {
            popupMenu.getMenu().findItem(R.id.menu_settings).setEnabled(false);
        } else if (current == HelpActivity.class) {
            popupMenu.getMenu().findItem(R.id.menu_help).setEnabled(false);
        } else if (current == AboutActivity.class) {
            popupMenu.getMenu().findItem(R.id.menu_about).setEnabled(false);
        }

        popupMenu.setOnMenuItemClickListener(item -> handleMenuClick(activity, item));
        popupMenu.show();
    }

    private static boolean handleMenuClick(Activity activity, MenuItem item) {
        int id = item.getItemId();

        // Completely removed the menu_medicines navigation block

        if (id == R.id.menu_settings) {
            activity.startActivity(new Intent(activity, SettingsActivity.class));
            return true;

        } else if (id == R.id.menu_help) {
            activity.startActivity(new Intent(activity, HelpActivity.class));
            return true;

        } else if (id == R.id.menu_about) {
            activity.startActivity(new Intent(activity, AboutActivity.class));
            return true;
        }

        return false;
    }
}
