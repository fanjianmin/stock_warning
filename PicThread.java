package app.wge.gptx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/10/22.
 */
public class PicThread extends Thread {
   String picturePath;
    ImageView imageView1;
    public PicThread(String a, ImageView imageView){
        picturePath=a;
        imageView1=imageView;
    }

    @Override
    public void run() {
        super.run();
        // TODO Auto-generated method stub
        final Bitmap bitmap = returnBitMap(picturePath);
        imageView1.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                imageView1.setImageBitmap(bitmap);
            }
        });

    }


    private Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
