package com.andhikaaw.encrypttext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Decrypted extends Fragment {

    private EditText CipherText;
    private EditText Key;
    private TextView PlainText;
    private Button btnDecrpyt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decrypted, container, false);

        CipherText = (EditText) view.findViewById(R.id.input_ciphertext);
        Key = (EditText) view.findViewById(R.id.input_key);
        PlainText = (TextView) view.findViewById(R.id.Plaintext);
        btnDecrpyt = (Button) view.findViewById(R.id.btn_decrypt);

        btnDecrpyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ciphertextAes = CipherText.getText().toString();
                String keyAes = Key.getText().toString();
                SecretKey key = generateAESKeyFromSHA256(keyAes);

                try {
                    PlainText.setText(decryptedAES(ciphertextAes,key));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                String ciphertextScytale = PlainText.getText().toString();
                int keyScytale = Key.length();

                PlainText.setText(decryptedScytale(ciphertextScytale,keyScytale));

                String ciphertextVigenere = PlainText.getText().toString();
                String keyVigenere = Key.getText().toString();
                String kataKunci = kataKunciVigenere(ciphertextVigenere,keyVigenere);
//                PlainText.setText(decryptedVigenere(ciphertextVigenere, kataKunci));
                PlainText.setText(decryptedVigenere(ciphertextVigenere,kataKunci));
            }
        });
        return view;
    }

    public static SecretKey generateAESKeyFromSHA256(String keyString){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hashBytes = digest.digest(keyString.getBytes());
        return new SecretKeySpec(hashBytes, "AES");
    }

    public String decryptedAES(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encryptedBytes = Base64.getDecoder().decode(encryptedText);
        }
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }


    public static String decryptedScytale(String ciphertext, int numOfRows) {
//        String encryptText = Encrypted.encryptedScytale(ciphertext, numOfRows);
        String decodedString = "";
        int numOfCols = ciphertext.length()/numOfRows;
        decodedString = encryptText(ciphertext, numOfCols);

        return decodedString;
    }

    private static String encryptText(String ciphertext, int numOfCols) {
        Encrypted.encryptedScytale(ciphertext, numOfCols);

        return Encrypted.encryptedScytale(ciphertext, numOfCols);
    }

    private static String kataKunciVigenere(String ciphertext, String Key){
        StringBuilder kataKunciVigenere = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i++) {
            for (int j = 0; j < Key.length(); j++) {
                if (kataKunciVigenere.length() == ciphertext.length()) {
                    break;
                }
                kataKunciVigenere.append(Key.charAt(j));
            }
        }
        return kataKunciVigenere.toString();
    }

    public String decryptedVigenere(String text,String key){
        String encrypt = "";
        int keyIndex = 0;
        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == ' '){
                encrypt+=text.charAt(i);
            }
            else{
                int x = ((text.charAt(i) +26)- key.charAt(keyIndex))%26;
                x+='A';
                encrypt+=(char)(x);
                keyIndex = (keyIndex += 1) % key.length();
            }
        }
        return encrypt;
    }
}