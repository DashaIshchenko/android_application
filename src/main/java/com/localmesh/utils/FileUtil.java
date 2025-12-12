package com.localmesh.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


 //Утилита для работы с файлами: кодирование/декодирование Base64,
 //сжатие изображений, работа с аудио

public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final int MAX_IMAGE_SIZE = 1024; // Максимальная ширина/высота в пикселях
    private static final int COMPRESSION_QUALITY = 80; // Качество JPEG в процентах
    

     //Конвертирует файл в Base64 строку

    public static String fileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];
        
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }
        
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    

     //Сохраняет Base64 строку как файл

    public static void base64ToFile(String base64Data, String outputPath) throws IOException {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(bytes);
        }
    }


     //Сжимает изображение и конвертирует в Base64

    public static String compressImageToBase64(String imagePath) {
        try {
            // Декодируем изображение с учётом размера
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            
            // Рассчитываем коэффициент сжатия
            int scale = 1;
            while (options.outWidth / scale > MAX_IMAGE_SIZE || 
                   options.outHeight / scale > MAX_IMAGE_SIZE) {
                scale *= 2;
            }
            
            // Декодируем со сжатием
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            
            if (bitmap == null) {
                Log.e(TAG, "Не удалось декодировать изображение: " + imagePath);
                return null;
            }
            
            // Сжимаем в JPEG
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, baos);
            byte[] bytes = baos.toByteArray();
            
            // Освобождаем память
            bitmap.recycle();
            
            return Base64.encodeToString(bytes, Base64.DEFAULT);
            
        } catch (Exception e) {
            Log.e(TAG, "Ошибка сжатия изображения", e);
            return null;
        }
    }

     //Получает длительность аудиофайла (в миллисекундах)

    public static long getAudioDuration(String audioPath) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(audioPath);
            
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            retriever.release();
            
            return duration != null ? Long.parseLong(duration) : 0;
        } catch (Exception e) {
            Log.e(TAG, "Ошибка получения длительности аудио", e);
            return 0;
        }
    }
    

     //Определяет тип файла по расширению

    public static String getFileType(String fileName) {
        if (fileName == null) return "unknown";
        
        fileName = fileName.toLowerCase();
        
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || 
            fileName.endsWith(".png") || fileName.endsWith(".gif")) {
            return "image";
        } else if (fileName.endsWith(".mp3") || fileName.endsWith(".wav") || 
                  fileName.endsWith(".ogg") || fileName.endsWith(".m4a")) {
            return "audio";
        } else if (fileName.endsWith(".txt") || fileName.endsWith(".md")) {
            return "text";
        } else if (fileName.endsWith(".pdf")) {
            return "document";
        } else {
            return "file";
        }
    }
    

     //Получает имя файла из пути

    public static String getFileName(String filePath) {
        if (filePath == null) return "file";
        
        int lastSeparator = filePath.lastIndexOf(File.separator);
        return lastSeparator >= 0 ? filePath.substring(lastSeparator + 1) : filePath;
    }

     //Получает размер файла в читаемом формате

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0 B";
        
        final String[] units = {"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        
        return String.format("%.1f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    public static boolean isFileSizeValid(String filePath, int maxSizeMB) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }
            
            long fileSizeBytes = file.length();
            long maxSizeBytes = maxSizeMB * 1024L * 1024L; // конвертируем МБ в байты
            
            return fileSizeBytes <= maxSizeBytes;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getReadableFileSize(String filePath) {
        try {
            File file = new File(filePath);
            long size = file.length();
            
            if (size <= 0) return "0 B";
            
            final String[] units = {"B", "KB", "MB", "GB"};
            int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
            
            // Ограничиваем максимум MB
            digitGroups = Math.min(digitGroups, 2);
            
            return String.format("%.1f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
            
        } catch (Exception e) {
            return "неизвестно";
        }
    }

    public static void showFileSizeError(Context context, String filePath, int maxSizeMB) {
        String currentSize = getReadableFileSize(filePath);
        String message = String.format(
            "Файл слишком большой: %s\nМаксимальный размер: %d MB",
            currentSize, maxSizeMB
        );
        
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
/////////////////////
    public static String getPathFromUri(Context context, Uri uri) {
        if (uri == null) return null;
        
        String scheme = uri.getScheme();
        if (scheme == null) return null;
        if ("file".equals(scheme)) {
            return uri.getPath();
        }
        if ("content".equals(scheme)) {
            try {
                String[] projection = { "_data" };
                try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow("_data");
                        String path = cursor.getString(columnIndex);
                        if (path != null) {
                            return path;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Не удалось получить путь через _data", e);
            }
            return copyFileToTemp(context, uri);
        }
        return null;
    }
    

    private static String copyFileToTemp(Context context, Uri uri) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        
        try {
            String fileName = getFileNameFromUri(context, uri);
            if (fileName == null) {
                fileName = "file_" + System.currentTimeMillis();
            }

            File tempFile = new File(context.getCacheDir(), fileName);

            inputStream = context.getContentResolver().openInputStream(uri);
            outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            Log.d(TAG, "Файл скопирован во временную папку: " + tempFile.getAbsolutePath());
            return tempFile.getAbsolutePath();
            
        } catch (Exception e) {
            Log.e(TAG, "Ошибка копирования файла из Uri", e);
            return null;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "Ошибка закрытия потоков", e);
            }
        }
    }
    
    public static String getFileNameFromUri(Context context, Uri uri) {
        if (uri == null) return null;
        
        String result = null;

        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = context.getContentResolver().query(
                    uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    String[] columnNames = {
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        OpenableColumns.DISPLAY_NAME,
                        "_display_name"
                    };
                    
                    for (String column : columnNames) {
                        int index = cursor.getColumnIndex(column);
                        if (index != -1) {
                            result = cursor.getString(index);
                            if (result != null) break;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Ошибка получения имени файла из Uri", e);
            }
        }
        if (result == null) {
            String path = uri.getPath();
            if (path != null) {
                int lastSlash = path.lastIndexOf('/');
                result = (lastSlash != -1) ? path.substring(lastSlash + 1) : path;
            }
        }
        
        return result;
    }

    public static String getSimplePathFromUri(Context context, Uri uri) {
        try {
            return copyFileToTemp(context, uri);
        } catch (Exception e) {
            Log.e(TAG, "Ошибка получения пути", e);
            return null;
        }
    }

    public static boolean isImageFile(String filePath) {
        if (filePath == null) return false;
        String type = getFileType(filePath);
        return "image".equals(type);
    }

    public static boolean isAudioFile(String filePath) {
        if (filePath == null) return false;
        String type = getFileType(filePath);
        return "audio".equals(type);
    }
    

    public static String processFileForSending(String filePath, int maxImageSizeKB) {
        if (filePath == null) return null;
        
        try {
            if (isImageFile(filePath)) {
                return compressImageToBase64(filePath);
            } else {
                return fileToBase64(filePath);
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка обработки файла для отправки", e);
            return null;
        }
    }
}
