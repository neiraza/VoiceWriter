package jp.co.fttx.ict.syss.android.voicewriter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import jp.co.fttx.ict.syss.android.voicewriter.R;

import java.util.ArrayList;

/**
 * using voice is outputted.
 *
 * @author oguri
 */
public class VoiceWriter extends Activity {
    private static final int REQUEST_CODE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button recognizerButton = (Button) findViewById(R.id.recognizer);
        Button webSearchButton = (Button) findViewById(R.id.websearch);

        recognizerButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                            "Using voice is outputted.");
                    startActivityForResult(intent, REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(VoiceWriter.this,
                            "Activity could not be found。", Toast.LENGTH_LONG).show();
                }
            }
        });

        webSearchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                            "Using voice is outputted.");
                    startActivityForResult(intent, REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(VoiceWriter.this,
                            "Activity could not be found。", Toast.LENGTH_LONG).show();
                }
            }
        });
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
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private static void showDialog(final Activity activity, String title, String text) {
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
}
