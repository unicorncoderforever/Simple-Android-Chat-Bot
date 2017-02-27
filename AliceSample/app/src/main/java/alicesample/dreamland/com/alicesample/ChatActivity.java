package alicesample.dreamland.com.alicesample;

import android.app.ProgressDialog;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;

import java.util.ArrayList;
import java.util.Locale;

import Adapter.ChatMessage;
import Adapter.ChatMessageAdapter;
import aimlwriter.FileWriter;


public class ChatActivity extends AppCompatActivity implements
        FileWriter.IWritinngCompletionCallback,TextToSpeech.OnInitListener {
    public static Chat chat;
    public Bot bot;
    private ListView mChatListView;
    private FloatingActionButton mSendMessage;
    private EditText mEditTextMessage;
    private ChatMessageAdapter mAdapter;
    private ProgressDialog progress;
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
    }

    private void setupView() {
        tts = new TextToSpeech(this, this);
        mChatListView = (ListView) findViewById(R.id.listView);
        mSendMessage = (FloatingActionButton) findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mChatListView.setAdapter(mAdapter);
        mSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTextMessage.getText().toString();
                String response = chat.multisentenceRespond(mEditTextMessage.getText().toString());
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                sendMessage(message);
                generateReplyMessage(response);
                mEditTextMessage.setText("");
                mChatListView.setSelection(mAdapter.getCount() - 1);
            }
        });
        writingFile();

    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
    }

    private void generateReplyMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
        speakOut(message);
    }


    @Override
    public void onWritingComplete() {
        AIMLProcessor.extension = new PCAIMLProcessorExtension();
        bot = new Bot("MY_BOT", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        String[] args = null;
        progress.dismiss();
    }


    public void writingFile() {
        progress = new ProgressDialog(this);
        progress.setMessage("loading bot files");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
        FileWriter writer = new FileWriter(this, this);
        writer.execute();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed");
        }

    }

    private void speakOut(String  message) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null,message);
        } else{
            tts.speak(message,TextToSpeech.QUEUE_ADD,null);
        }

    }
}
