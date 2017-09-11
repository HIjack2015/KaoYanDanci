package cn.jk.kaoyandanci.ui.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.aesthetic.Aesthetic;

import cn.jk.kaoyandanci.R;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/7/2.
 */

public class ColorPickerPreference extends Preference {

    ImageView imageView;

    public ColorPickerPreference(Context context) {
        this(context, null, 0);
        init(context, null);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.ate_preference_custom);
        setWidgetLayoutResource(R.layout.ate_preference_widget);
        setPersistent(false);

    }

    public void setColor(int color) {
        imageView.setBackgroundColor(color);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        String key = getKey();
        Context context = getContext();
        String primaryKey = context.getString(R.string.primary_color_key);
        String accentKey = context.getString(R.string.accent_color_key);
        String textPrimaryKey = context.getString(R.string.text_primary_color_key);
        String textSecondKey = context.getString(R.string.text_second_color_key);
        imageView = (ImageView) view.findViewById(R.id.circle);
        Observable<Integer> color = Aesthetic.get().colorPrimary();
        if (key.equals(primaryKey)) {
            color = Aesthetic.get().colorPrimary();
        } else if (key.equals(accentKey)) {
            color = Aesthetic.get().colorAccent();
        } else if (key.equals(textPrimaryKey)) {
            color = Aesthetic.get().textColorPrimary();
        } else if (key.equals(textSecondKey)) {
            color = Aesthetic.get().textColorSecondary();
        }
        color.take(1).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                imageView.setBackgroundColor(integer);

            }
        });
    }

}