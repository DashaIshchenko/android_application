package com.localmesh.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.localmesh.R;
import com.localmesh.utils.FileUtil;

public class FilePickerActivity extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 1001;
    private static final int REQUEST_AUDIO = 1002;
    private static final int REQUEST_FILE = 1003;
    private static final int REQUEST_RECORD_AUDIO = 1004;

    private String selectedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file_attachment);

        // Кнопка "Галерея" - выбор изображения
        findViewById(R.id.btnGallery).setOnClickListener(v -> openGallery());

        // Кнопка "Аудио" - запись или выбор аудиофайла
        findViewById(R.id.btnAudio).setOnClickListener(v -> showAudioOptions());

        // Кнопка "Файл" - выбор любого файла
        findViewById(R.id.btnFile).setOnClickListener(v -> openFilePicker());

        // Кнопка "Отмена"
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void showAudioOptions() {
        // Можно сделать диалог выбора: запись или выбор файла
        // Для простоты - сразу открываем запись
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_RECORD_AUDIO);
        } else {
            openAudioFilePicker();
        }
    }

    private void openAudioFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, REQUEST_AUDIO);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Все типы файлов
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    // Получаем реальный путь к файлу
                    selectedFilePath = FileUtil.getPathFromUri(this, uri);
                    
                    if (selectedFilePath != null) {
                        // Проверяем размер файла (5MB лимит)
                        if (!FileUtil.isFileSizeValid(selectedFilePath, 5)) {
                            String size = FileUtil.getReadableFileSize(selectedFilePath);
                            Toast.makeText(this, 
                                "Файл слишком большой: " + size + " (максимум 5MB)", 
                                Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Возвращаем результат в основную Activity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("file_path", selectedFilePath);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Ошибка выбора файла", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }
}
