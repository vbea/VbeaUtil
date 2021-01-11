package com.vbes.util.view;

import android.app.Activity;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vbes.util.R;

public class DialogEditView {
    private View view;
    private EditText editText;
    private TextView txtTips;
    public DialogEditView(Activity context) {
        view = context.getLayoutInflater().inflate(R.layout.dialog_edit_layout, null, true);
        editText = view.findViewById(R.id.vbe_dialog_edit);
        txtTips = view.findViewById(R.id.vbe_dialog_tips);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public void setTips(String text) {
        txtTips.setVisibility(View.VISIBLE);
        txtTips.setText(text);
    }

    /**
     *
     * @param type android.text.InputType;
     */
    public void setInputType(int type) {
        editText.setInputType(type);
    }

    public void setMaxLength(int length) {
        InputFilter[] filters = {new InputFilter.LengthFilter(length)};
        editText.setFilters(filters);
    }

    public void setHint(String hint) {
        editText.setHint(hint);
    }

    public View getView() {
        return view;
    }
}
