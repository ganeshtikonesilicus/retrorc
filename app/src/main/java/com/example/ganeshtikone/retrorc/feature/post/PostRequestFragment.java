package com.example.ganeshtikone.retrorc.feature.post;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ganeshtikone.retrorc.R;
import com.example.ganeshtikone.retrorc.network.RequestService;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostRequestFragment extends Fragment {

    /**
     *
     */
    private AppCompatEditText editTextUserName;

    /**
     *
     */
    private AppCompatEditText editTextPassword;

    /**
     *
     */
    private AppCompatEditText editTextAge;

    /**
     *
     */
    private AppCompatEditText editTextMobile;

    /**
     *
     */
    private AppCompatTextView textViewResult;


    /**
     *
     */
    private Realm realm;


    public PostRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post_request, container, false);
        initUI(rootView);
        return rootView;
    }

    /**
     * Initialise User Interface
     * @param rootView inflated view object
     */
    private void initUI(View rootView) {
        editTextUserName = rootView.findViewById(R.id.editTextUserName);
        editTextPassword = rootView.findViewById(R.id.editTextPassword);
        editTextAge = rootView.findViewById(R.id.editTextAge);
        editTextMobile = rootView.findViewById(R.id.editTextMobile);

        textViewResult = rootView.findViewById(R.id.textViewResult);

        /*

         */
        AppCompatButton buttonSignUp = rootView.findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(v -> makePostRequest());
    }

    /**
     *
     */
    private void makePostRequest() {

        // -- Get values from EditText --
        String username = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String age = editTextAge.getText().toString();
        String mobile = editTextMobile.getText().toString().trim();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username",username);
            jsonObject.put("password", password);
            jsonObject.put("age", age);
            jsonObject.put("mobile", mobile);


            RequestService requestService = new RequestService();
            requestService.createUser(jsonObject.toString())
                    .subscribe(this::handleResponse, this::handleError);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Response from server
     * @param response
     */
    private void handleResponse(String response) {
        textViewResult.setText(response);
    }

    /**
     * Error Response from Server or Request
     * @param throwable
     */
    private void handleError(Throwable throwable) {
        textViewResult.setText(throwable.getLocalizedMessage());
    }

}
