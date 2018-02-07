package com.murey.poster.postermaster.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.view.activity.AbstractActivity;
import com.murey.poster.postermaster.view.activity.ProjectDetailsActivity;

import java.util.Calendar;

public class DialogUtils {

    public static void displayDialogSureToQuitApp(final AbstractActivity abstractActivity) {
        new MaterialDialog.Builder(abstractActivity)
                .title(R.string.dialog_quit_app_title)
                .content(R.string.dialog_quit_app_content)
                .cancelable(true)
                .backgroundColorRes(R.color.colorDialogBackground)
                .contentColor(Color.WHITE)
                .titleColorRes(R.color.colorDialogTitle)
                .positiveColorRes(R.color.colorDialogButton)
                .neutralColorRes(R.color.colorDialogButton)
                .negativeColorRes(R.color.colorDialogButton)

                .negativeText(R.string.dialog_cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })

                .positiveText(R.string.dialog_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        abstractActivity.quitApplication();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void displayDialogSureToDisconnectApp(final AbstractActivity abstractActivity) {
        new MaterialDialog.Builder(abstractActivity)
                .title(R.string.dialog_disconnect_app_title)
                .content(R.string.dialog_disconnect_app_content)
                .cancelable(true)
                .backgroundColorRes(R.color.colorDialogBackground)
                .contentColor(Color.WHITE)
                .titleColorRes(R.color.colorDialogTitle)
                .positiveColorRes(R.color.colorDialogButton)
                .neutralColorRes(R.color.colorDialogButton)
                .negativeColorRes(R.color.colorDialogButton)

                .negativeText(R.string.dialog_cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })

                .positiveText(R.string.dialog_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        abstractActivity.disconnectUser();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void displayDialogAlertPermission(final AbstractActivity abstractActivity){
        new MaterialDialog.Builder(abstractActivity)
                .title(R.string.dialog_alert_permission_title)
                .content(R.string.dialog_alert_permission_content)
                .cancelable(false)
                .backgroundColorRes(R.color.colorDialogBackground)
                .contentColor(Color.WHITE)
                .titleColorRes(R.color.colorDialogTitle)
                .positiveColorRes(R.color.colorDialogButton)
                .neutralColorRes(R.color.colorDialogButton)
                .negativeColorRes(R.color.colorDialogButton)

                .positiveText(android.R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", abstractActivity.getPackageName(), null);
                        intent.setData(uri);
                        abstractActivity.startActivityForResult(intent, PermissionUtils.MY_PERMISSIONS_REQUEST_STORAGE);
                    }
                })
                .show();
    }

    public static MaterialDialog displayDialogErrorConnection(final AbstractActivity abstractActivity) {
        MaterialDialog dialog = new MaterialDialog.Builder(abstractActivity)
                .title(R.string.dialog_error_connection_title)
                .content(R.string.dialog_error_connection_content)
                .cancelable(true)
                .backgroundColorRes(R.color.colorDialogBackground)
                .contentColor(Color.WHITE)
                .titleColorRes(R.color.colorDialogTitle)
                .positiveColorRes(R.color.colorDialogButton)
                .neutralColorRes(R.color.colorDialogButton)
                .negativeColorRes(R.color.colorDialogButton)

                .negativeText(R.string.dialog_ok)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })

                .show();
        return dialog;
    }

    public static MaterialDialog displayDialogNonValidUser(final AbstractActivity abstractActivity) {
        MaterialDialog dialog = new MaterialDialog.Builder(abstractActivity)
                .title(R.string.dialog_non_valid_user_title)
                .content(R.string.dialog_non_valid_user_content)
                .cancelable(true)
                .backgroundColorRes(R.color.colorDialogBackground)
                .contentColor(Color.WHITE)
                .titleColorRes(R.color.colorDialogTitle)
                .positiveColorRes(R.color.colorDialogButton)
                .neutralColorRes(R.color.colorDialogButton)
                .negativeColorRes(R.color.colorDialogButton)

                .negativeText(R.string.dialog_ok)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })

                .show();
        return dialog;
    }

    public static MaterialDialog getWaitingDialog(Context context, boolean isCancelable) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title(R.string.dialog_wait_title)
                .customView(R.layout.dialog_wait_content, false)
                .cancelable(isCancelable)
                .backgroundColorRes(R.color.colorDialogBackground)
                .contentColor(Color.WHITE)
                .titleColorRes(R.color.colorDialogTitle)
                .positiveColorRes(R.color.colorDialogButton)
                .neutralColorRes(R.color.colorDialogButton)
                .negativeColorRes(R.color.colorDialogButton)
                .build();

        ProgressBar pcSavingAuth = (ProgressBar) materialDialog.findViewById(R.id.pcSavingSession);
        pcSavingAuth.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        materialDialog.show();
        return materialDialog;
    }

    public static MaterialDialog displayDialogSetStudentNote(final ProjectDetailsActivity projectDetailsActivity, final Users student, final int projectId, final Users appUser) {

        final MaterialDialog materialDialog = new MaterialDialog.Builder(projectDetailsActivity)
                .title(R.string.dialog_set_student_note_title)
                .customView(R.layout.dialog_set_student_content, false)
                .cancelable(true)
                .backgroundColorRes(R.color.colorDialogBackground)
                .contentColor(Color.WHITE)
                .titleColorRes(R.color.colorDialogTitle)
                .positiveColorRes(R.color.colorDialogButton)
                .neutralColorRes(R.color.colorDialogButton)
                .negativeColorRes(R.color.colorDialogButton)
                .build();

        TextView tvMyNoteFor = (TextView) materialDialog.findViewById(R.id.tvMyNoteFor);
        RatingBar ratingBar =  (RatingBar) materialDialog.findViewById(R.id.ratingBar);
        final TextView tvRatingBar =  (TextView) materialDialog.findViewById(R.id.tvRatingBar);
        View bpCancel = materialDialog.findViewById(R.id.bpCancel);
        View bpOk = materialDialog.findViewById(R.id.bpOK);

        tvMyNoteFor.setText(String.format("%s %s %s",
                projectDetailsActivity.getResources().getString(R.string.dialog_set_student_note_my_note_for),
                student.getForename(),
                student.getSurname())
        );

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
                tvRatingBar.setText(String.valueOf(rating*4));
            }
        });
        ratingBar.setRating(3.5f);
        tvRatingBar.setText(String.valueOf(ratingBar.getRating()*4));

        bpOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!appUser.isModeVisitor()){
                    projectDetailsActivity.newNoteFromDialog(Float.valueOf(tvRatingBar.getText().toString()), projectId, student.getIdServerEseoUser());
                }else{
                    projectDetailsActivity.newNoteFromVisitorDialog(Float.valueOf(tvRatingBar.getText().toString()), projectId, student.getIdServerEseoUser());
                }
                materialDialog.dismiss();
            }
        });

        bpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });

        materialDialog.show();
        return materialDialog;
    }
}
