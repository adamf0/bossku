package app.adam.basiclibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

class Text {
    private SpannableString full_text;
    private String target;
    private TextView textView;
    private boolean underline=false;
    private int color_text;

    protected Activity act;

    public Text(Activity act) {
        this.act = act;
    }

    public void setFull_text(String full_text){
        this.full_text = SpannableString.valueOf(full_text);
    }
    public void setTarget(String target){
        this.target = target;
    }
    public void setTextView(TextView textView){
        this.textView = textView;
    }
    public void setUnderline(boolean underline){
        this.underline = underline;
    }
    public void setColor(String color){
        this.color_text = Color.parseColor(color);
    }
    public void setColor(int color){
        this.color_text = color;
    }
    public void addClick(final TextListener textListener){
        applySpan(full_text, target, new ClickableSpan() {
           public void onClick(@NonNull View widget) {
                textListener.onClick();
           }
           public void updateDrawState(@NonNull TextPaint ds) {
              super.updateDrawState(ds);
              ds.setColor(color_text);
              if (underline) {
                 ds.setUnderlineText(true);
              }
           }
        });
    }
    public void implement(){
        textView.setText(full_text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void setTimer(int minute, final TimerListener timerListener){
        new CountDownTimer(minute * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                @SuppressLint("DefaultLocale")
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                textView.setText(hms);
            }
            public void onFinish() {
                timerListener.onFinishTick();
            }
        }.start();
    }

    private void applySpan(SpannableString spannable, String target, ClickableSpan span) {
        final String spannableString = spannable.toString();
        final int start = spannableString.indexOf(target);
        final int end = start + target.length();
        spannable.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
