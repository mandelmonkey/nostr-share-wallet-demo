package io.zebedee.nostrshare;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.message);

        // Get the intent that started this activity
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                String msg = data.getQueryParameter("msg");
                if (msg != null) {

                    try {
                        JSONObject jsonObject = new JSONObject(msg);

                        String text = jsonObject.getString("text");
                        if(text != null) {
                           textView.setText(text);
                        }

                        String imageBase64 = jsonObject.getString("imageBase64");
                        if(imageBase64 != null) {
                            byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                            ImageView imageViewBase64 = findViewById(R.id.base64ImageView);
                            imageViewBase64.setImageBitmap(decodedBitmap);
                        }

                        String imageUrl = jsonObject.getString("imgUrl");
                        if(imageUrl != null) {
                            ImageView imageView = findViewById(R.id.urlImageView);
                            Glide.with(this).load(imageUrl).into(imageView);
                        }

                        String videoUrl = jsonObject.getString("videoUrl");
                        if(videoUrl != null) {
                            ExoPlayer exoPlayer = new ExoPlayer.Builder(this).build();
                            PlayerView playerView = findViewById(R.id.videoUrlView);
                            exoPlayer.setRepeatMode(exoPlayer.REPEAT_MODE_ALL);
                            playerView.setPlayer(exoPlayer);
                            MediaItem mediaItem = MediaItem.fromUri(videoUrl);
                           exoPlayer.addMediaItem(mediaItem);

                           exoPlayer.prepare();
                            exoPlayer.play();
                        }

                        // Now do something with the data.
                    } catch (JSONException e) {
                        // JSON parsing error
                        textView.setText(msg);
                    }


                }
            }
        }
    }
}
