package in.yellowsoft.LetUKnow;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class MyCustomProgressDialog extends ProgressDialog {

    public MyCustomProgressDialog(Context context) {
        super(context);
    }

    public static ProgressDialog ctor(Context context) {
        MyCustomProgressDialog dialog = new MyCustomProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.view_custom_progress_dialog);


    }


  }
