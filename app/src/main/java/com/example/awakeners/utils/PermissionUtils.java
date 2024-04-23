package com.example.awakeners.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.awakeners.R;

import java.util.Locale;

public class PermissionUtils {
    /**
     * Отображает диалоговое окно с запросом на три разрешения:
     * 1. Экран блокировки
     * 2. Отображать всплывающие окна, когда запущено в фоновом режиме
     * 3. Всплывающие окна
     * <p>
     * Первые два являются собственными разрешениями оболочки MIUI и используются
     * только в устройствах Xiaomi. Именно они необходимы для того, чтобы полноэкранное
     * уведомление срабатывало при блокировке экрана. Оболочка MIUI сама может решать,
     * давать эти разрешения или не давать. И неизвестно, какими принципами она
     * руководстствуется. Например, если на телефоне уже установлено 200 приложений,
     * она может посчитать, что наше приложение - не такое важное, и ему можно не
     * давать таких широких возможностей. А если наше приложение - первое и единственное
     * из установленных, она может соизволить сама дать эти разрешения. Во многих
     * популярных приложениях (например, мессенджерах) эти приложения также установлены
     * по умолчанию, и на StackOverflow пишут, что это потому, что эти приложения
     * входят в "белый список" системы MIUI. Будильник Юрия Куликова также не запрашивает
     * дополнительных разрешений, поэтому можно предположить, что система каким-то
     * образом отслеживает статистику этого приложения и тоже определяет, что ему
     * можно дать эти разрешения.
     * <p>
     * Отследить эти два разрешения программно никак нельзя (впрочем, как и установить),
     * поэтому для отслеживания используется третье разрешение - всплывающие окна.
     * Вот оно уже является системным разрешением Android, а расположено оно в Xiaomi
     * на том же экране, что и первые два. Поэтому для определения, даны ли разрешения,
     * используется именно это разрешение.
     * <p>
     * Для остальных моделей телефонов никаких дополнительных разрешений не требуется.
     */
    public static void getOverlaysPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if ("xiaomi".equals(Build.MANUFACTURER.toLowerCase(Locale.ROOT)) && !Settings.canDrawOverlays(context)) {
                final Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.alert_layout2, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                mDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = mDialogBuilder.create();
                TextView NoTextView = promptsView.findViewById(R.id.NoTextView);
                TextView YesTextView = promptsView.findViewById(R.id.YesTextView);

                YesTextView.setOnClickListener(v -> {
                    context.startActivity(intent);
                    alertDialog.dismiss();
                });
                NoTextView.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    //((Activity) context).finish();
                });

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }
    }

    public static boolean checkOverlaysPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }
}
