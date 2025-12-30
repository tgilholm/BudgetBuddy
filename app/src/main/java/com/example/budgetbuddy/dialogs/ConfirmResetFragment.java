package com.example.budgetbuddy.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.budgetbuddy.R;

public class ConfirmResetFragment extends DialogFragment
{
    public static final String REQUEST_KEY = "ConfirmResetDialogRequest";
    public static final String RESPONSE_KEY = "ConfirmResetDialogResponse";

    /**
     * Overrides the base method to create a new <code>AlertDialog</code>.
     * This tells the user to confirm their choice & dispatches the response key
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle result = new Bundle();

        return new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.alert_title)         // Set text fields
                .setMessage(R.string.alert_message)
                .setIcon(R.drawable.warning_icon)       // Set icon
                .setPositiveButton(R.string.alert_reset, (dialog, which) -> {
                    // Reset the app
                    result.putBoolean(RESPONSE_KEY, true);
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                })
                .setNegativeButton(R.string.alert_cancel, (dialog, which) -> {
                    // Cancel the reset
                    result.putBoolean(RESPONSE_KEY, false);
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                })
                .create();
    }

    /**
     * Helper method.
     * Displays the confirmation dialog to the user.
     * @param manager The FragmentManager to use for displaying the dialog.
     */
    public static void show(@NonNull FragmentManager manager) {
        ConfirmResetFragment dialog = new ConfirmResetFragment();
        dialog.show(manager, "ConfirmResetDialogFragment");
    }
}
