package com.sandipbhattacharya.videoviewdemo;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button button, switchButton, switchLanguageButton, backButton,
            forwardButton, exerciseButton, repeatChunk, checkButton,
            nextExercise, addToVocabularyButton, reminderButton, deleteFromReminderButton;
    TextView textForVideo, textOfExercise, nameOfApp;
    ImageView imageView;
    Scanner scanDirections, scanText, scanTextOnRussian;
    InputStream packageOfDirections, packageOfText, packageOfTextOnRussian;
    Spinner spinner;
    EditText answerField;
    VideoView videoView;
    Thread setTheSubtitles;
    MediaController mediaController;
    Random random;
    String allExercise = "";
    boolean onSubtitles = true;
    boolean isEnglish = true;
    boolean isLearn = false;
    boolean isRemind = false;
    int attemps = 3;
    int chunkTime = 0;
    int curVideo = 1;
    int wordToDelete;
    String correctAnswer = "";
    ArrayList<Integer> chunkDurations = new ArrayList<>();
    ArrayList<String> chunkText = new ArrayList<>();
    ArrayList<String> chunkTextOnRussian = new ArrayList<>();
    ArrayList<String> videoID = new ArrayList<>();
    Set<String> vocabulary = new HashSet<>();
    ArrayList<ArrayList<Integer>> chunkToLearn = new ArrayList<>();
    ArrayList<ArrayList<Integer>> allChunkDurations = new ArrayList<>();
    ArrayList<ArrayList<String>> allChunkText = new ArrayList<>();
    ArrayList<ArrayList<String>> allChunkTextOnRussian = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int video = 0; video < 1; video++) {
            packageOfDirections = getResources().openRawResource(R.raw.durations_of_video1);
            packageOfText = getResources().openRawResource(R.raw.recognized1);
            packageOfTextOnRussian = getResources().openRawResource(R.raw.translation1);
            scanDirections = new Scanner(packageOfDirections);
            scanText = new Scanner(packageOfText);
            scanTextOnRussian = new Scanner(packageOfTextOnRussian);
            chunkDurations = new ArrayList<>();
            chunkText = new ArrayList<>();
            chunkTextOnRussian = new ArrayList<>();
            while (scanDirections.hasNext()) {
                int chunk = Integer.parseInt(scanDirections.nextLine());
                chunkDurations.add(chunk);
            }
            while (scanText.hasNext()) {
                String text = scanText.nextLine();
                chunkText.add(text);
            }
            while (scanTextOnRussian.hasNext()) {
                String text = scanTextOnRussian.nextLine();
                chunkTextOnRussian.add(text);
            }
            allChunkDurations.add(chunkDurations);
            allChunkText.add(chunkText);
            allChunkTextOnRussian.add(chunkTextOnRussian);
        }

        for (int video = 0; video < 1; video++) {
            packageOfDirections = getResources().openRawResource(R.raw.durations_of_video2);
            packageOfText = getResources().openRawResource(R.raw.recognized2);
            packageOfTextOnRussian = getResources().openRawResource(R.raw.translation2);
            scanDirections = new Scanner(packageOfDirections);
            scanText = new Scanner(packageOfText);
            scanTextOnRussian = new Scanner(packageOfTextOnRussian);
            chunkDurations = new ArrayList<>();
            chunkText = new ArrayList<>();
            chunkTextOnRussian = new ArrayList<>();
            while (scanDirections.hasNext()) {
                int chunk = Integer.parseInt(scanDirections.nextLine());
                chunkDurations.add(chunk);
            }
            while (scanText.hasNext()) {
                String text = scanText.nextLine();
                chunkText.add(text);
            }
            while (scanTextOnRussian.hasNext()) {
                String text = scanTextOnRussian.nextLine();
                chunkTextOnRussian.add(text);
            }
            allChunkDurations.add(chunkDurations);
            allChunkText.add(chunkText);
            allChunkTextOnRussian.add(chunkTextOnRussian);
        }
        chunkDurations = allChunkDurations.get(0);
        chunkText = allChunkText.get(0);
        chunkTextOnRussian = allChunkTextOnRussian.get(0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        nameOfApp = (TextView) findViewById(R.id.textView);
        switchButton = (Button) findViewById(R.id.switchSubtitles);
        switchLanguageButton = (Button) findViewById(R.id.language);
        backButton = (Button) findViewById(R.id.btBack);
        forwardButton = (Button) findViewById(R.id.btUpdateSubtitles);
        exerciseButton = (Button) findViewById(R.id.goToExercise);
        repeatChunk = (Button) findViewById(R.id.repeat);
        checkButton = (Button) findViewById(R.id.checkAnswer);
        nextExercise = (Button) findViewById(R.id.nextWord);
        deleteFromReminderButton = (Button) findViewById(R.id.deleteVideoFromReminder);
        addToVocabularyButton = (Button) findViewById(R.id.addToVocabulary);
        reminderButton = (Button) findViewById(R.id.reminder);
        answerField = (EditText) findViewById(R.id.answer);
        switchLanguageButton.setBackgroundColor(Color.WHITE);
        switchLanguageButton.setTextColor(Color.BLUE);
        nextExercise.setVisibility(View.GONE);
        repeatChunk.setVisibility(View.GONE);
        checkButton.setVisibility(View.GONE);
        answerField.setVisibility(View.GONE);
        deleteFromReminderButton.setVisibility(View.GONE);
        addToVocabularyButton.setVisibility(View.GONE);
        videoID.add("1");
        videoID.add("2");
        getSupportActionBar().hide();
        textForVideo = findViewById(R.id.subtitles);
        textOfExercise = findViewById(R.id.exerciseText);
        textOfExercise.setVisibility(View.GONE);
        videoView = findViewById(R.id.videoView);
        textForVideo.setTextSize(20);
        textForVideo.setTextColor(Color.BLACK);
        spinner = findViewById(R.id.chooseVideo);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, videoID);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        videoView.setVideoPath("android.resource://"+getPackageName()+"/"+ R.raw.video1);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        upgradeSubtitles();
    }

    public void standartView() {
        changeVideo();
        switchButton.setVisibility(View.VISIBLE);
        switchLanguageButton.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.VISIBLE);
        mediaController.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        forwardButton.setVisibility(View.VISIBLE);
        exerciseButton.setVisibility(View.VISIBLE);
        reminderButton.setVisibility(View.VISIBLE);
        nextExercise.setVisibility(View.GONE);
        repeatChunk.setVisibility(View.GONE);
        checkButton.setVisibility(View.GONE);
        answerField.setVisibility(View.GONE);
        deleteFromReminderButton.setVisibility(View.GONE);
        addToVocabularyButton.setVisibility(View.GONE);
        textForVideo.setTextSize(20);
        textForVideo.setTextColor(Color.BLACK);
        textOfExercise.setText("");
        videoView.seekTo(0);
        answerField.setText("");
        upgradeSubtitles();
    }


    public void getBack(View view)
    {
        changeVideo();
        if (onSubtitles) {
            int position = videoView.getCurrentPosition();
            for (int dur = 0; dur < chunkDurations.size(); dur++) {
                if (position < chunkDurations.get(dur)) {
                    if (dur < 2) {
                        videoView.seekTo(0);
                        break;
                    }
                    videoView.seekTo(chunkDurations.get(dur));
                    break;
                }
            }
        }
    }

    public void getUpdate(View view)
    {
        changeVideo();
        if (onSubtitles) {
            upgradeSubtitles();
        }
    }

    public void switchSubtitlesButton(View view)
    {
        changeVideo();
        onSubtitles = !onSubtitles;
        if (onSubtitles) {
            switchButton.setBackgroundColor(Color.BLUE);
            switchButton.setTextColor(Color.WHITE);
            switchButton.setText("off Subtitles");
            upgradeSubtitles();
        }
        else {
            switchButton.setBackgroundColor(Color.WHITE);
            switchButton.setTextColor(Color.BLACK);
            switchButton.setText("on Subtitles");
            textForVideo.setText("");
        }
    }

    public void switchLanguage(View view)
    {
        changeVideo();
        isEnglish = !isEnglish;
        if (isEnglish) {
            switchLanguageButton.setBackgroundColor(Color.WHITE);
            switchLanguageButton.setTextColor(Color.BLUE);
            switchLanguageButton.setText("en");
        }
        else {
            switchLanguageButton.setBackgroundColor(Color.WHITE);
            switchLanguageButton.setTextColor(Color.RED);
            switchLanguageButton.setText("ru");
        }
        upgradeSubtitles();
        onSubtitles = true;
        switchButton.setBackgroundColor(Color.BLUE);
        switchButton.setTextColor(Color.WHITE);
        switchButton.setText("off Subtitles");
    }

    public void startTheExercise(View view) {
        isRemind = false;
        doTheExercise();
    }

    public void doTheExercise() {
        isLearn = !isLearn;
        if (isLearn) {
            switchButton.setVisibility(View.GONE);
            switchLanguageButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            forwardButton.setVisibility(View.GONE);
            reminderButton.setVisibility(View.GONE);
            addToVocabularyButton.setVisibility(View.GONE);
            nextExercise.setVisibility(View.VISIBLE);
            textOfExercise.setVisibility(View.VISIBLE);
            repeatChunk.setVisibility(View.VISIBLE);
            checkButton.setVisibility(View.VISIBLE);
            answerField.setVisibility(View.VISIBLE);
            textOfExercise.setTextSize(20);
            textOfExercise.setTextColor(Color.BLACK);
            textForVideo.setTextColor(Color.BLACK);
            textOfExercise.setText("№1. Insert correct word");
            random = new Random();
            attemps = 3;
            if (!isRemind) {
                chunkTime = Math.max(random.nextInt(chunkDurations.size()) - 1, 0);
            }
            else {
                chunkTime = chunkToLearn.get(0).get(1);
                curVideo = chunkToLearn.get(0).get(2);
            }
            videoView.seekTo(chunkDurations.get(chunkTime));
            removeWordForExercise();
            exerciseButton.setText("Return to video");
        }
        else {
            standartView();
            exerciseButton.setText("Try yourself");
        }
    }

    public void upgradeSubtitles() {
        changeVideo();
        int position = videoView.getCurrentPosition();
        for (int dur = 0; dur < chunkDurations.size(); dur++) {
            if (position < chunkDurations.get(dur)) {
                if (dur == 0) {
                    if (isEnglish) {
                        textForVideo.setText(chunkText.get(0));
                    }
                    else {
                        textForVideo.setText(chunkTextOnRussian.get(0));
                    }
                    break;
                }
                if (isEnglish) {
                    textForVideo.setText(chunkText.get(dur));
                }
                else {
                    textForVideo.setText(chunkTextOnRussian.get(dur));
                }
                break;
            }
        }
    }

    public void removeWordForExercise() {
        int position = videoView.getCurrentPosition();
        int toDelete = 0;
        for (int dur = 0; dur < chunkDurations.size(); dur++) {
            if (position < chunkDurations.get(dur)) {
                toDelete = dur;
                break;
            }
        }
        String textForExercise = chunkText.get(toDelete);
        ArrayList<String> wordsOfText = new ArrayList<>();
        String word = "";
        for (int letter = 0; letter < textForExercise.length(); letter++) {
            if (textForExercise.charAt(letter) == ' ') {
                wordsOfText.add(word);
                word = "";
                continue;
            }
            if (textForExercise.charAt(letter) != '.') {
                word += textForExercise.charAt(letter);
            }
        }
        if (word.length() != 0) {
            wordsOfText.add(word);
        }
        if (word.equals("*sound*")) {
            chunkTime = Math.max(random.nextInt(chunkDurations.size()) - 1, 0);
            videoView.seekTo(chunkDurations.get(chunkTime));
            removeWordForExercise();
            return;
        }
        if (!isRemind) {
            wordToDelete = random.nextInt(wordsOfText.size());
        }
        else {
            wordToDelete = chunkToLearn.get(0).get(0);
        }
        correctAnswer = wordsOfText.get(wordToDelete);
        allExercise = "";
        for (int wordToAdd = 0; wordToAdd < wordsOfText.size(); wordToAdd++) {
            if (wordToAdd != wordToDelete) {
                allExercise += wordsOfText.get(wordToAdd);
            }
            else {
                allExercise += "_______";
            }
            if (wordToAdd + 1 != wordsOfText.size()) {
                allExercise += " ";
            }
        }
        textForVideo.setText(allExercise);
    }


    public void repeatVideo(View view) {
        videoView.seekTo(chunkDurations.get(chunkTime));
        textForVideo.setText(allExercise);
    }

    public void checkTheExercise(View view) {
        String userAnswer = "";
        for (int letter = 0; letter < answerField.length(); letter++) {
            if (answerField.getText().toString().charAt(letter) != ' '
            & answerField.getText().toString().charAt(letter) != '\n' &
                    answerField.getText().toString().charAt(letter) != '\t') {
                userAnswer += answerField.getText().toString().charAt(letter);
            }
        }
        if ((correctAnswer.toLowerCase()).equals(userAnswer.toLowerCase())) {
            addToVocabularyButton.setVisibility(View.VISIBLE);
            if (isRemind) {
                deleteFromReminderButton.setVisibility(View.VISIBLE);
            }
            textForVideo.setText("Correct");
            textForVideo.setTextColor(Color.GREEN);
        }
        else {
            if (attemps > 0) {
                Toast.makeText(this, "Incorrect. Try again", Toast.LENGTH_SHORT).show();
                attemps--;
            }
            else {
                addToVocabularyButton.setVisibility(View.VISIBLE);
                if (isRemind) {
                    deleteFromReminderButton.setVisibility(View.VISIBLE);
                }
                Toast.makeText(this, "Correct answer: " + correctAnswer, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addWordToVocabulary(View view) {
        ArrayList<Integer> chunkToLearnID = new ArrayList<>();
        boolean isToAdd = true;
        for (int exercise = 0; exercise < chunkToLearn.size(); exercise++) {
            if (chunkToLearn.get(exercise).get(0) == wordToDelete) {
                if (chunkToLearn.get(exercise).get(1) == chunkTime) {
                    if (chunkToLearn.get(exercise).get(2) == curVideo) {
                        isToAdd = false;
                        break;
                    }
                }
            }
        }
        if (isToAdd) {
            chunkToLearnID.add(wordToDelete);
            chunkToLearnID.add(chunkTime);
            chunkToLearnID.add(curVideo);
            deleteFromReminderButton.setVisibility(View.VISIBLE);
            chunkToLearn.add(chunkToLearnID);
        }
        vocabulary.add(correctAnswer.toLowerCase());
    }

    public void deleteFromVocabulary(View view) {
        chunkToLearn.remove(0);
        deleteFromReminderButton.setVisibility(View.GONE);
        if (chunkToLearn.size() != 0) {
            isLearn = !isLearn;
            Toast.makeText(this, "chunk has been studied. Go to the next chunk", Toast.LENGTH_SHORT).show();
            doTheExercise();
        }
        else {
            standartView();
            isRemind = false;
            Toast.makeText(this, "You repeated everything", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextWordToDelete(View view) {
        if (isRemind) {
            Toast.makeText(this, "You have not completed the exercise. Click 'Learned'", Toast.LENGTH_SHORT).show();
        }
        else {
            isLearn = !isLearn;
            doTheExercise();
            deleteFromReminderButton.setVisibility(View.GONE);
        }
    }

    public void changeVideo() {
        if (spinner.getSelectedItem().toString().equals("1") & curVideo == 2) {
            chunkDurations = allChunkDurations.get(0);
            chunkText = allChunkText.get(0);
            chunkTextOnRussian = allChunkTextOnRussian.get(0);
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+ R.raw.video1);
            chunkToLearn.clear();
            curVideo = 1;
        }
        if (spinner.getSelectedItem().toString().equals("2") & curVideo == 1) {
            chunkDurations = allChunkDurations.get(1);
            chunkText = allChunkText.get(1);
            chunkTextOnRussian = allChunkTextOnRussian.get(1);
            System.out.println(chunkDurations.get(0));
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+ R.raw.video2);
            chunkToLearn.clear();
            curVideo = 2;
        }
    }

    public void remindExercise(View view) {
        if (chunkToLearn.size() != 0) {
            isRemind = true;
            doTheExercise();
        }
        else {
            Toast.makeText(this, "You have nothing to repeat", Toast.LENGTH_SHORT).show();
        }
    }

}