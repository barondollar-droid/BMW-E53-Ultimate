package com.bmwe53.ultimate;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(new Dashboard());
    }

    class Dashboard extends View {
        Paint p = new Paint(3);
        String[] pages = {"DASHBOARD", "VEHICLE", "CONTROL", "DIAGNOSTICS", "SETTINGS"};
        int page = 0;
        Dashboard() { super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE, null); }
        void text(Canvas c, String s, float x, float y, float size, int color) {
            p.setColor(color); p.setTextSize(size); p.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
            c.drawText(s, x, y, p);
        }
        void panel(Canvas c, float l, float t, float r, float b) {
            p.setColor(Color.rgb(20,27,37)); c.drawRoundRect(l,t,r,b,22,22,p);
            p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(2); p.setColor(Color.rgb(43,57,74));
            c.drawRoundRect(l,t,r,b,22,22,p); p.setStyle(Paint.Style.FILL);
        }
        @Override protected void onDraw(Canvas c) {
            float w=getWidth(), h=getHeight();
            c.drawColor(Color.rgb(8,11,16));
            text(c,"BMW E53",32,48,25,Color.WHITE);
            text(c,"ULTIMATE  •  DEMO INTERFACE",32,76,13,Color.rgb(110,170,220));
            float navY=h-74;
            for(int i=0;i<pages.length;i++){
                float x=24+i*(w-48)/5f;
                p.setColor(i==page?Color.rgb(32,115,190):Color.rgb(23,31,42));
                c.drawRoundRect(x,navY,x+(w-60)/5f,navY+48,12,12,p);
                text(c,pages[i],x+12,navY+30,12,Color.WHITE);
            }
            if(page==0){
                panel(c,28,105,w*.48f,h-100); panel(c,w*.51f,105,w-28,h-100);
                text(c,"VEHICLE OVERVIEW",52,145,17,Color.WHITE);
                text(c,"SPEED",58,205,13,Color.LTGRAY); text(c,"0",58,300,82,Color.WHITE);
                text(c,"km/h",145,300,20,Color.rgb(100,170,230));
                text(c,"ENGINE RPM",58,365,13,Color.LTGRAY); text(c,"0",58,425,48,Color.WHITE);
                text(c,"COOLANT",w*.54f,160,14,Color.LTGRAY);
                text(c,"-- °C",w*.54f,205,35,Color.WHITE);
                text(c,"STATUS",w*.54f,280,14,Color.LTGRAY);
                text(c,"DEMO MODE",w*.54f,325,24,Color.rgb(255,190,75));
                text(c,"Live CAN data is not connected",w*.54f,365,15,Color.LTGRAY);
            } else {
                panel(c,28,105,w-28,h-100);
                text(c,pages[page],55,160,22,Color.WHITE);
                String[] lines = page==1 ? new String[]{"Doors: demo only","Windows: demo only","Vehicle status: not connected"} :
                    page==2 ? new String[]{"Central locking: not connected","Window control: disabled in demo","No commands are sent to the vehicle"} :
                    page==3 ? new String[]{"Diagnostics interface placeholder","No fault codes are being read","Connect a verified CAN interface later"} :
                    new String[]{"Display: landscape","Theme: BMW dark blue","CAN integration: not configured"};
                for(int i=0;i<lines.length;i++) text(c,lines[i],58,225+i*55,19,Color.LTGRAY);
            }
            text(c,"DEMO • NO LIVE VEHICLE DATA • NO VEHICLE COMMANDS",32,h-92,11,Color.rgb(130,145,160));
        }
        @Override public boolean onTouchEvent(android.view.MotionEvent e) {
            if(e.getAction()==MotionEvent.ACTION_UP && e.getY()>getHeight()-90) {
                float cell=(getWidth()-48)/5f;
                page=Math.max(0,Math.min(4,(int)((e.getX()-24)/cell)));
                invalidate(); return true;
            }
            return true;
        }
    }
}
