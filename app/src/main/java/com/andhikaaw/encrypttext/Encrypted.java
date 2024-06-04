package com.andhikaaw.encrypttext;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public class Encrypted extends Fragment {

    private EditText Plaintext;
    private EditText Key;
    private TextView CipherText;
    private Button btnEncrypt;
    private ImageView btnCopy;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encrypted, container, false);


        Plaintext = (EditText) view.findViewById(R.id.input_text);
        Key =  (EditText) view.findViewById(R.id.input_key);
        CipherText = (TextView) view.findViewById(R.id.Ciphertext);
        btnCopy = (ImageView) view.findViewById(R.id.copy);
        btnEncrypt = (Button) view.findViewById(R.id.btn_encrypt);


        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plaintext = Plaintext.getText().toString();
                String key = Key.getText().toString();
                String kataKunci = kataKunci(plaintext, key);
                SecretKey generateKey = generateAESKeyFromSHA256(key);


                CipherText.setText(encryptedVigenere(plaintext, kataKunci));

                String plaintextScytale = CipherText.getText().toString();
                int keyScytale = key.length();
//
                CipherText.setText(encryptedScytale(plaintextScytale,keyScytale));

                try {
                    CipherText.setText(encryptedAES(CipherText.getText().toString(),generateKey));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
//                CipherText.setText(generateKey.toString());
            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = CipherText.getText().toString();
                copyToClipboard(text);
            }
        });

        return view;
    }
    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", text);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(requireContext(), "Text copied to clipboard", LENGTH_SHORT).show();
    }

    public static String kataKunci(String Plaintext, String Key){
        StringBuilder kataKunci = new StringBuilder();
        for (int i = 0; i < Plaintext.length(); i++) {
            for (int j = 0; j < Key.length(); j++) {
                if (kataKunci.length() == Plaintext.length()) {
                    break;
                }
                kataKunci.append(Key.charAt(j));
            }
        }
        return kataKunci.toString();
    }
//    public static String encryptedVigenere(String Plaintext, String kataKunci){
//        StringBuilder encrypt = new StringBuilder();
//        for (int k = 0; k < Plaintext.length(); k++) {
//            int x = (Plaintext.charAt(k) + kataKunci.charAt(k)) % 26;
//            x += 'A';
//            encrypt.append((char) (x));
//        }
//        return encrypt.toString();
//    }
    public String encryptedVigenere(String text,String key){
        String encrypt = "";
        int keyIndex = 0;
        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == ' '){
                encrypt+=text.charAt(i);
            }
            else{
                int x = (text.charAt(i) + key.charAt(keyIndex))%26;
                x+='A';
                encrypt+=(char)(x);
                keyIndex = (keyIndex += 1) % key.length();
            }
        }
        return encrypt;
    }

    public static String encryptedScytale(String plaintext, int numOfRows){
        StringBuilder Key = new StringBuilder();
        if (numOfRows >= plaintext.length() || numOfRows <= 0) {
            return plaintext;
        } else {
            while (plaintext.length() % numOfRows != 0) {
                plaintext += " ";
            }
            int numOfCols = plaintext.length() / numOfRows;
            for (int i = 0; i < numOfCols; i++) {
                for (int y = i; y < plaintext.length(); y += numOfCols) {
                    Key.append(plaintext.charAt(y));
                }
            }
        }
        return Key.toString();
    }
    public SecretKey generateAESKeyFromSHA256(String keyString){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hashBytes = digest.digest(keyString.getBytes());
        return new SecretKeySpec(hashBytes, "AES");
    }

    public String encryptedAES(String plainText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        return plainText;
    }
}