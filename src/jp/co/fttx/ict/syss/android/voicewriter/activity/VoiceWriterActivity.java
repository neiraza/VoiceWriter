package jp.co.fttx.ict.syss.android.voicewriter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.Button;
import android.widget.Toast;
import jp.co.fttx.ict.syss.android.voicewriter.R;

import java.util.ArrayList;

/**
 * using voice is outputted.
 *
 * @author oguri
 */
public class VoiceWriterActivity extends Activity implements OnClickListener {
    private static final int REQUEST_CODE = 0;
    private static final String TAG = "VoiceWriterActivity";
    private static final String ACTION_INTERCEPT = "com.adamrocker.android.simeji.ACTION_INTERCEPT";
    private static final String REPLACE_KEY = "replace_key";
    private Intent dst;
    private String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cf. 今回はどこから呼ばれても関係ないけど～
        dst = getIntent();
        String action = dst.getAction();
        if (action != null && ACTION_INTERCEPT.equals(action)) {
            /* called by Simeji */
            message = dst.getStringExtra(REPLACE_KEY);
        } else {
            /* not called by Simeji */
            message = dst.getStringExtra(REPLACE_KEY);
        }
        setContentView(R.layout.main);
        findViewById(R.id.recognizer).setOnClickListener(this);
        findViewById(R.id.websearch).setOnClickListener(this);
    }

    /*
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recognizer:
                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                            "Using voice is outputted.");
                    startActivityForResult(intent, REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(VoiceWriterActivity.this,
                            "Activity could not be found。", Toast.LENGTH_LONG).show();
                    sendBack(message);
                }
                break;
            case R.id.websearch:
                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    startActivityForResult(intent, REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Error");
                    Toast.makeText(VoiceWriterActivity.this,
                            "Activity could not be found。", Toast.LENGTH_LONG).show();
                    sendBack(message);
                }
                break;
            default:
                sendBack(message);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String speakedString = "";
            ArrayList<String> speechToChar = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            for (String aSpeechToChar : speechToChar) {
                speakedString += aSpeechToChar;
            }
            // Padded with spaces If the shorter string.
            for (int i = (20 - speakedString.length()); i > 0; i--)
                speakedString += " ";

            showDialog(this, "", speakedString);
            sendBack(speakedString);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void showDialog(final Activity activity, String title, String text) {
        AlertDialog.Builder ad = new AlertDialog.Builder(activity);
        ad.setTitle(title);
        ad.setMessage(text);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                activity.setResult(Activity.RESULT_OK);
            }
        });
        ad.create();
        ad.show();
    }

    private void sendBack(String result) {
        Intent data = new Intent();
        data.putExtra(REPLACE_KEY, result);
        setResult(RESULT_OK, data);
        finish();
    }
}
