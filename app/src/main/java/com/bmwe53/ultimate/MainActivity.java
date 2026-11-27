package com.bmwe53.ultimate;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(new Dashboard());
    }

    class Dashboard extends View {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        String[] pages = {"ПРИБОРЫ", "АВТОМОБИЛЬ", "УПРАВЛЕНИЕ", "ДИАГНОСТИКА", "НАСТРОЙКИ"};
        int page = 0, theme = 0;
        final int[][] accents = {{40,170,255},{255,55,55},{30,220,145},{255,165,35}};
        Dashboard() { super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE, null); }
        int accent(){ int[] a=accents[theme]; return Color.rgb(a[0],a[1],a[2]); }
        void paint(int color){ p.setColor(color); p.setStyle(Paint.Style.FILL); p.setStrokeWidth(1); }
        void text(Canvas c,String s,float x,float y,float size,int color){ paint(color); p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD)); p.setTextSize(size); c.drawText(s,x,y,p); }
        void center(Canvas c,String s,float x,float y,float size,int color){ paint(color); p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD)); p.setTextSize(size); c.drawText(s,x-p.measureText(s)/2,y,p); }
        void panel(Canvas c,float l,float t,float r,float b){ paint(Color.rgb(19,27,39)); c.drawRoundRect(l,t,r,b,20,20,p); p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(1.5f); p.setColor(Color.rgb(43,58,77)); c.drawRoundRect(l,t,r,b,20,20,p); p.setStyle(Paint.Style.FILL); }
        void gauge(Canvas c,float cx,float cy,float radius,String title,String value,String unit,int max,int divisions){
            int a=accent(); paint(Color.rgb(35,47,63)); p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(13); c.drawArc(cx-radius,cy-radius,cx+radius,cy+radius,135,270,false,p);
            p.setColor(a); p.setStrokeWidth(4); c.drawArc(cx-radius,cy-radius,cx+radius,cy+radius,135,270,false,p);
            p.setStrokeWidth(2.2f);
            for(int i=0;i<=divisions;i++){
                float angle=(float)Math.toRadians(135+270f*i/divisions); float r1=radius-13, r2=radius-27;
                p.setColor(i>divisions*0.82?Color.rgb(255,75,65):Color.rgb(205,218,230));
                c.drawLine(cx+(float)Math.cos(angle)*r1,cy+(float)Math.sin(angle)*r1,cx+(float)Math.cos(angle)*r2,cy+(float)Math.sin(angle)*r2,p);
                if(divisions<=8){ String n=String.valueOf(max*i/divisions); float tx=cx+(float)Math.cos(angle)*(radius-43),ty=cy+(float)Math.sin(angle)*(radius-43)+5; center(c,n,tx,ty,11,Color.LTGRAY); }
            }
            center(c,title,cx,cy-radius*.48f,15,Color.LTGRAY); center(c,value,cx,cy+8,Math.max(28,radius*.34f),Color.WHITE); center(c,unit,cx,cy+radius*.34f,13,a);
            paint(Color.WHITE); c.drawCircle(cx,cy,5,p);
        }
        @Override protected void onDraw(Canvas c){
            float w=getWidth(),h=getHeight(); c.drawColor(Color.rgb(7,11,17));
            text(c,"BMW E53",24,34,22,Color.WHITE); text(c,"ULTIMATE  /  DIGITAL COCKPIT",24,56,10,accent());
            float navY=h-52;
            if(page==0){
                float top=72, bottom=navY-12, areaH=bottom-top;
                float r=Math.min(w*.205f,areaH*.39f); float cy=top+areaH*.43f;
                float leftCx=w*.265f, rightCx=w*.735f;
                panel(c,14,top-12,w-14,bottom);
                gauge(c,leftCx,cy,r,"СКОРОСТЬ","0","km/h",280,14);
                gauge(c,rightCx,cy,r,"ОБОРОТЫ","0","об/мин",8000,8);
                float cardY=cy+r+12, cardH=Math.max(48,bottom-cardY-8), gap=10, cardW=(w-48)/3f;
                String[] labels={"АКПП 5HP24","ОХЛАЖДЕНИЕ","НАПРЯЖЕНИЕ"}; String[] vals={"-- °C","-- °C","--.- V"};
                for(int i=0;i<3;i++){float x=24+i*(cardW+gap); panel(c,x,cardY,x+cardW,cardY+cardH); center(c,labels[i],x+cardW/2,cardY+20,10,Color.LTGRAY); center(c,vals[i],x+cardW/2,cardY+Math.min(cardH-8,48),21,Color.WHITE);}
                text(c,"DEMO MODE • ДАННЫЕ АВТОМОБИЛЯ НЕ ПОДКЛЮЧЕНЫ",24,h-62,9,Color.rgb(145,158,174));
            } else {
                panel(c,18,75,w-18,navY-12); text(c,pages[page],38,112,19,Color.WHITE);
                if(page==1){text(c,"Двери: нет связи",40,165,16,Color.LTGRAY);text(c,"Стёкла: нет связи",40,205,16,Color.LTGRAY);text(c,"Состояние автомобиля: DEMO",40,245,16,Color.LTGRAY);}
                else if(page==2){text(c,"Команды автомобилю отключены",40,165,16,Color.LTGRAY);text(c,"CAN-интерфейс не настроен",40,205,16,Color.LTGRAY);}
                else if(page==3){text(c,"Чтение ошибок пока не реализовано",40,165,16,Color.LTGRAY);text(c,"Подключение к блоку CAN отсутствует",40,205,16,Color.LTGRAY);}
                else {text(c,"Цвет оформления:",40,165,16,Color.LTGRAY);String[] names={"BMW BLUE","M RED","MINT","AMBER"}; for(int i=0;i<4;i++){float x=40+i*(w-100)/4f; paint(Color.rgb(accents[i][0],accents[i][1],accents[i][2])); c.drawCircle(x+12,220,10,p); text(c,names[i],x+28,224,10,Color.WHITE);} text(c,"Нажми на цвет для выбора",40,270,13,Color.LTGRAY); text(c,"Шкалы: 280 км/ч и 8000 об/мин",40,315,14,Color.LTGRAY);}
            }
            float cell=w/5f; for(int i=0;i<5;i++){float x=i*cell+4; paint(i==page?accent():Color.rgb(24,34,47)); c.drawRoundRect(x,navY,x+cell-8,h-7,10,10,p); center(c,pages[i],x+(cell-8)/2,navY+25,Math.max(8,Math.min(10,w/85f)),Color.WHITE);}
        }
        @Override public boolean onTouchEvent(MotionEvent e){ if(e.getAction()==MotionEvent.ACTION_UP){float w=getWidth(),h=getHeight(),navY=h-52;if(e.getY()>navY){page=Math.max(0,Math.min(4,(int)(e.getX()/(w/5f))));invalidate();return true;} if(page==4 && e.getY()>185 && e.getY()<245){int idx=(int)((e.getX()-40)/((w-100)/4f));theme=Math.max(0,Math.min(3,idx));invalidate();return true;} } return true; }
    }
}
