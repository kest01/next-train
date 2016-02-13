package ru.kest.nexttrain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ru.kest.nexttrain.widget.R;
import ru.kest.nexttrain.widget.TrainsWidget;
import ru.kest.nexttrain.widget.util.Constants;

import static ru.kest.nexttrain.widget.util.Constants.*;

public class PopUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // Should do a proper argument verification here
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Constants.HOME_TO_WORK)) {
            displayDialog(extras);
        }
    }

    private void displayDialog(final Bundle extras) {
        String details = extras.getString(DETAILS);
        new AlertDialog.Builder(this)
                .setTitle("Расписание электричек")
                .setMessage("Следить за рейсом " + details + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //                Intent onClickIntent = new Intent(context, TrainsWidget.class);
                        Intent notificationIntent = new Intent(PopUpActivity.this, TrainsWidget.class);
                        notificationIntent.setAction(CREATE_NOTIFICATION);
                        notificationIntent.putExtra(RECORD_HASH, extras.getInt(RECORD_HASH));
                        sendBroadcast(notificationIntent);

                        // Handle a positive answer
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle a negative answer
                        dialog.dismiss();
                        finish();
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pop_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
