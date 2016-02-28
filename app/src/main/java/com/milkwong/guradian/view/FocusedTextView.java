package com.milkwong.guradian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Leo on 28/02/16.
 */

public class FocusedTextView extends TextView
{
  public FocusedTextView(Context context) {
    super(context);
  }

  public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public FocusedTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean isFocused() {
    return true;
  }
}
