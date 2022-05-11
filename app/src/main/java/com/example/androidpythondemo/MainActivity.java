package com.example.androidpythondemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String[] permissions = new String[] { Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    
    private final Python py = Python.getInstance();

    private PyObject backend;
    
    private Button encryptButton;
    private TextView instructions;
    private EditText message;

    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                Log.d("isGranted", isGranted.toString());
                Log.d("isGranted Values", isGranted.values().toString());

                if (isGranted.get("android.permission.READ_EXTERNAL_STORAGE")
                        && isGranted.get("android.permission.WRITE_EXTERNAL_STORAGE")) {
                    backend = py.getModule("backend");
                } else {
                    Toast.makeText(this, "You need to provide read/write permission for storage of the json file.",
                            Toast.LENGTH_LONG).show();
                    
                    this.finishAffinity();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            backend = py.getModule("backend");
        } else {
            requestPermissionLauncher.launch(permissions);
        }

        encryptButton = findViewById(R.id.button);
        instructions = findViewById(R.id.textView);
        message = findViewById(R.id.editTextInput);

        encryptButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                String messageText = message.getText().toString();

                String encryptedText = backend.callAttr("hill_cipher", messageText).toString();

                instructions.setText("The encrypted message is:");
                message.setText(encryptedText);
        }
    }
}
