package app.wge.gptx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import mylib.juhe.JuheWge;



public class main extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{

   TextView txt1,txtmc,txtzf,txStock,dayPicTx,weekPicTx,minPicTx,monthPicTx;

    Button okBtn1;
    String strStock="600126sh";
    String[] dataArray=new String[4];
    JuheWge juheWge=new JuheWge();
    //JSONObject jsonObject;

    private ImageView imageView;
    private String picturePath = "http://image.sinajs.cn/newchart/daily/n/sh600026.gif";

    /**
     * 实现键盘触 发事件
     *
     * */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
       if  (keyCode==KeyEvent.KEYCODE_ENTER)
        {
            getDataAction();
        }
        return false;
    }

    /**
    * 实现键盘触 发事件
    *
    * */

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

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageViewDayline);
    }

    /**
     * 初始化图片事件
     * */

    void initPicTxAddListener(){
        dayPicTx= (TextView)findViewById(R.id.textView9) ;
        dayPicTx.setOnClickListener(this);
        weekPicTx= (TextView)findViewById(R.id.textView10) ;
        weekPicTx.setOnClickListener(this);
        monthPicTx= (TextView)findViewById(R.id.textView11) ;
        monthPicTx.setOnClickListener(this);
        minPicTx= (TextView)findViewById(R.id.textView12) ;
        minPicTx.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initPicTxAddListener();  //为图片文本绑定事件

          dataArray[3]="null";   //设置值避免报空指针
        txStock =(EditText) findViewById(R.id.stockIdent);//获得编号控件
      //  txStock.setKeyListener(this);


        strStock=txStock.getText().toString();   //获得要显示的股票编号


        System.out.println(">>>>>>"+strStock+">>>>");

        new Thread(networkTask).start();
/**
 *
 * 加载图片
 * */

        initView();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                final Bitmap bitmap = returnBitMap(picturePath);
                imageView.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();

/**
 * 结束加载
 * */
        System.out.println("开始打印...");


        txt1=(TextView)findViewById(R.id.txXianjia);
        txtmc=(TextView)findViewById(R.id.txMingcheng);
        txtzf=(TextView)findViewById(R.id.txZhangfu);
        okBtn1=(Button)findViewById(R.id.okBtn);


        //jsonObject=JuheDemo.jsonObjectData;
        //txt1.setMaxLines(40);
        setTextValue();
      okBtn1.setOnClickListener(this);
        txStock.setOnKeyListener(this);



    }
    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
           // strStock = txStock.getText().toString();
            System.out.println("strStock=" + strStock);

            dataArray=juheWge.getStockArrayData(strStock);   //给数组获取新数据


            System.out.println("<<<<<<dataarry>>>>"+dataArray[1]);



            // TODO Auto-generated method stub
            final Bitmap bitmap = returnBitMap(juheWge.stockDataPic[1]);
            imageView.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    imageView.setImageBitmap(bitmap);
                }
            });



            // new Thread(networkTask).start();  //重新获得数据
           // setTextValue(); //重新填写新股数据
            //juheDemo.getDataFromStockIdent(strStock);



          /**
            JuheDemo.getRequest6();
            JuheDemo.getRequest5();
            JuheDemo.getRequest4();
            JuheDemo.getRequest3();
            JuheDemo.getRequest2();
            */
        }
    };

   void  setTextValue(){
       if (dataArray==null){
           System.out.println("数据异常!");
           //  return;
       }else {

         if (dataArray[3].equals("fail")){
             Toast.makeText(this,"股票代码不正确或数据异常！",Toast.LENGTH_SHORT).show();
               return;
           }
           //  dataArray=juheWge.getStockArrayData();
           txt1.setText(dataArray[1]);
          // txt1.refreshDrawableState();
           System.out.println("++++"+dataArray[0]+"+++++");
           txtmc.setText(dataArray[0]);
          // txtmc.refreshDrawableState()
           txtzf.setText(dataArray[2]);
          // txtzf.refreshDrawableState();
       }
    }

    @Override
    public void onClick(View v) {

       if (v.getId()==R.id.okBtn)
        getDataAction();

        switch ( v.getId()) {
            case R.id.textView9:
                //执行日线操作

                PicThread picThread=new PicThread(juheWge.stockDataPic[0],imageView);
                picThread.start();
                System.out.println("day");

                break;
            case R.id.textView10:
                //执行周线操作
                System.out.println("week");
                PicThread picThread2=new PicThread(juheWge.stockDataPic[1],imageView);
                picThread2.start();
                break;
            case R.id.textView11:
                //执行月线操作
                System.out.println("month");
                PicThread picThread3=new PicThread(juheWge.stockDataPic[2],imageView);
                picThread3.start();
                break;
            case R.id.textView12:
                //执行分时操作
                System.out.println("min");
                PicThread picThread4=new PicThread(juheWge.stockDataPic[3],imageView);
                picThread4.start();
                break;
            default:
                System.out.println("case执行完毕！");


        }

    }

    void getDataAction(){

        strStock = txStock.getText().toString();
        System.out.println("strStock=" + strStock);

        // dataArray=juheWge.getStockArrayData(strStock);   //给数组获取新数据
        Thread jt= new Thread(networkTask,"juhe 线程");  //重新获得数据
        jt.start();
        try{
            jt.join();}
        catch (Exception e){
            System.out.println("等待失败！");
        }

                // SystemClock.sleep(300);  //让主线程等待120ms,否则获取数据的线程数据还没准备好主线程已执行完毕
        System.out.println("线程执行完后的语句");
        setTextValue(); //重新填写新股数据
        //juheDemo.getDataFromStockIdent(strStock);



    }
}

