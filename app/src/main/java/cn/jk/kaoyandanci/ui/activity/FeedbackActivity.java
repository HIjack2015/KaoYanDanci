package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.util.Config;
import cn.jk.kaoyandanci.util.Constant;
import cn.jk.kaoyandanci.util.NetWorkSingleton;
import cn.jk.kaoyandanci.util.ToastUtil;

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.contentEdt)
    EditText contentEdt;
    @BindView(R.id.contactInfoEdt)
    EditText contactInfoEdt;
    @BindView(R.id.submitBtn)
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("意见反馈");
    }

    @OnClick(R.id.submitBtn)
    public void onViewClicked() {
        final String contactInfo = contactInfoEdt.getText().toString();

        final String content = contentEdt.getText().toString();
        if (content == null || content.isEmpty()) {
            ToastUtil.showShort(context, "反馈内容还没填写");
            return;
        }
        final String guid = Config.getGUID();

        final String project = Constant.PROJECT_ID;


        RequestQueue requestQueue = NetWorkSingleton.getInstance(context).getRequestQueue();
        StringRequest feedbackRequest = new StringRequest(Request.Method.POST, Constant.FEEDBACK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtil.showShort(context, response);
                        submitBtn.setClickable(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShort(context, error.toString());
                submitBtn.setClickable(true);
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (contactInfo != null && !contactInfo.isEmpty()) {
                    params.put("contactInfo", contactInfo);
                }
                params.put("guid", guid);
                params.put("project", project);
                params.put("content", content + new Date().toString());

                return params;
            }

            ;
        };
        requestQueue.add(feedbackRequest);
        submitBtn.setClickable(false);
    }
}
